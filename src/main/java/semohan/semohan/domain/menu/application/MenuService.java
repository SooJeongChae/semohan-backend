package semohan.semohan.domain.menu.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import semohan.semohan.domain.member.domain.Member;
import semohan.semohan.domain.member.repository.MemberRepository;
import semohan.semohan.domain.menu.dto.MenuViewDto;
import semohan.semohan.domain.menu.repository.MenuRepository;
import semohan.semohan.global.exception.CustomException;
import semohan.semohan.global.exception.ErrorCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final MemberRepository memberRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MenuViewDto getTodayMenuByRestaurant(long restaurantId) {
        LocalDate today = LocalDate.now();
        String todayDate = today.format(DATE_FORMATTER);
        return menuRepository.findMenuByRestaurantIdAndMealDate(restaurantId, todayDate)
                .map(MenuViewDto::toDto)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
    }

    public MenuViewDto getMenuByRestaurantAndDate(long restaurantId, LocalDate mealDate) {
        String mealDateFormatted = mealDate.format(DATE_FORMATTER);
        return menuRepository.findMenuByRestaurantIdAndMealDate(restaurantId, mealDateFormatted)
                .map(MenuViewDto::toDto)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
    }

    public MenuViewDto getPinnedRestaurantMenu(long memberId) {
        Member member = memberRepository.findMemberById(memberId).orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_USER));  // 로그인 정보 없으면
        if (member.getPin() == null) {
            throw new CustomException(ErrorCode.PINNED_RESTAURANT_NOT_FOUND);
        }   // pin한 레스토랑 없으면 예외 발생
        LocalDate today = LocalDate.now();
        String todayDate = today.format(DATE_FORMATTER);
        return menuRepository.findMenuByRestaurantIdAndMealDate(member.getPin().getId(), todayDate)
                .map(MenuViewDto::toDto)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
    }
}