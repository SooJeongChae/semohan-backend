package semohan.semohan.domain.menu.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import semohan.semohan.domain.member.domain.Member;
import semohan.semohan.domain.member.repository.MemberRepository;
import semohan.semohan.domain.menu.dto.MenuViewDto;
import semohan.semohan.domain.menu.repository.MenuRepository;

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
                .orElseThrow(() -> new IllegalArgumentException("No menu found for restaurant id " + restaurantId + " on date " + today));
    }

    public MenuViewDto getMenuByRestaurantAndDate(long restaurantId, LocalDate mealDate) {
        String mealDateFormatted = mealDate.format(DATE_FORMATTER);
        return menuRepository.findMenuByRestaurantIdAndMealDate(restaurantId, mealDateFormatted)
                .map(MenuViewDto::toDto)
                .orElseThrow(() -> new IllegalArgumentException("No menu found for restaurant id " + restaurantId + " on date " + mealDate));
    }

    public MenuViewDto getPinnedRestaurantMenu(long memberId) {
        Member member = memberRepository.findMemberById(memberId).orElseThrow(() -> new IllegalArgumentException("No member found with id " + memberId));
        LocalDate today = LocalDate.now();
        String todayDate = today.format(DATE_FORMATTER);
        return menuRepository.findMenuByRestaurantIdAndMealDate(member.getPin().getId(), todayDate)
                .map(MenuViewDto::toDto)
                .orElseThrow(() -> new IllegalArgumentException("No menu found for pinned restaurant on date " + today));
    }
}
