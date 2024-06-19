package semohan.semohan.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import semohan.semohan.domain.review.domain.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findReviewsByRestaurantId(Long restaurantId);
    List<Review> findReviewsByMemberId(Long memberId);
    Optional<Review> findReviewById(Long id);
    boolean existsByMemberIdAndMenuId(Long memberId, Long menuId);
}