package semohan.semohan.domain.review.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semohan.semohan.domain.member.domain.Member;
import semohan.semohan.domain.member.repository.MemberRepository;
import semohan.semohan.domain.menu.domain.Menu;
import semohan.semohan.domain.menu.repository.MenuRepository;
import semohan.semohan.domain.restaurant.domain.Restaurant;
import semohan.semohan.domain.restaurant.repository.RestaurantRepository;
import semohan.semohan.domain.review.domain.Review;
import semohan.semohan.domain.review.dto.ReviewCreationDto;
import semohan.semohan.domain.review.dto.ReviewViewDto;
import semohan.semohan.domain.review.repository.ReviewRepository;
import semohan.semohan.global.exception.CustomException;
import semohan.semohan.global.exception.ErrorCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MenuRepository menuRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    public List<ReviewViewDto> getReviewsByRestaurantId(long restaurantId) {
        List<Review> reviews = reviewRepository.findReviewsByRestaurantId(restaurantId);
        return reviews.stream().map(ReviewViewDto::toDto).collect(Collectors.toList());
    }

    public List<ReviewViewDto> getMyReviews(long id) {
        List<Review> reviews = reviewRepository.findReviewsByMemberId(id);
        return reviews.stream().map(ReviewViewDto::toDto).collect(Collectors.toList());
    }

    public List<ReviewViewDto> getReviewsByMemberId(String memberId) {
        Member member = memberRepository.findMemberByUsername(memberId).orElseThrow();
        List<Review> reviews = reviewRepository.findReviewsByMemberId(member.getId());
        return reviews.stream().map(ReviewViewDto::toDto).collect(Collectors.toList());
    }

    @Transactional
    public boolean createReview(ReviewCreationDto reviewCreationDto, long restaurantId, long memberId) {

        int mealType = reviewCreationDto.getMealType();
        LocalDate today = LocalDate.now();
        String mealDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 오늘 날짜와 mealType, restaurantId를 이용해 메뉴 조회
        Menu menu = menuRepository.findMenuByRestaurantIdAndMealDateAndMealType(restaurantId, mealDate, mealType)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        long menuId = menu.getId();

        // 리뷰 중복 여부 확인
        if (reviewRepository.existsByMemberIdAndMenuId(memberId, menuId)) {
            throw new CustomException(ErrorCode.ALREADY_WRITE_REVIEW);
        }

        Review review = new Review();
        review.setContent(reviewCreationDto.getContent());
        review.setLikeRestaurant(reviewCreationDto.isLikeRestaurant());
        review.setLikeMenu(reviewCreationDto.isLikeMenu());

        review.setMenu(menu);

        Restaurant restaurant = restaurantRepository.findRestaurantById(restaurantId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_RESTAURANT));
        review.setRestaurant(restaurant);

        Member member = memberRepository.findMemberById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        review.setMember(member);

        reviewRepository.save(review);

        // 식당 좋아요
        if (review.isLikeRestaurant()) {
            int currentLikes = restaurant.getLikesRestaurant();
            restaurant.setLikesRestaurant(currentLikes + 1);
            restaurantRepository.save(restaurant);
        }

        // 메뉴 좋아요
        if (review.isLikeMenu()) {
            int currentLikes = menu.getLikesMenu();
            menu.setLikesMenu(currentLikes + 1);
            menuRepository.save(menu);
        }

        // 회원 포인트 50(일단 임시) 업
        int currentPoints = member.getPoint();
        member.setPoint(currentPoints + 50);
        memberRepository.save(member); // 변경된 포인트 저장

        return true; // 성공적으로 저장된 경우
    }

    @Transactional
    public boolean deleteReview(long id, long memberId) {
        Review review = reviewRepository.findReviewById(id).orElseThrow();
        Member member = memberRepository.findMemberById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Restaurant restaurant = review.getRestaurant();
        Menu menu = review.getMenu();

        if (review.getMember().equals(member)) {
            reviewRepository.delete(review);

            // 식당 좋아요 감소
            if (review.isLikeRestaurant()) {
                int currentLikes = restaurant.getLikesRestaurant();
                restaurant.setLikesRestaurant(currentLikes - 1);
                restaurantRepository.save(restaurant);
            }

            // 메뉴 좋아요 감소
            if (review.isLikeMenu()) {
                int currentLikes = menu.getLikesMenu();
                menu.setLikesMenu(currentLikes - 1);
                menuRepository.save(menu);
            }

            // 회원 포인트 50(일단 임시) 다운
            int currentPoints = member.getPoint();
            member.setPoint(currentPoints - 50);
            memberRepository.save(member); // 변경된 포인트 저장

            return true;
        }

        return false;
    }
}
