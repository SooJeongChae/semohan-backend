package semohan.semohan.domain.menu.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import semohan.semohan.domain.member.domain.Member;
import semohan.semohan.domain.member.repository.MemberRepository;
import semohan.semohan.domain.menu.dto.MenuViewDto;
import semohan.semohan.domain.menu.dto.PinViewDto;
import semohan.semohan.domain.menu.repository.MenuRepository;
import semohan.semohan.domain.restaurant.repository.RestaurantRepository;
import semohan.semohan.global.exception.CustomException;
import semohan.semohan.global.exception.ErrorCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MenuViewDto getTodayMenuByRestaurant(long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new CustomException(ErrorCode.INVALID_RESTAURANT);
        }   // id에 해당하는 식당 없을 때 예외 발생

        LocalDate today = LocalDate.now();
        String todayDate = today.format(DATE_FORMATTER);
        return menuRepository.findMenuByRestaurantIdAndMealDate(restaurantId, todayDate)
                .map(MenuViewDto::toDto)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
    }

    public MenuViewDto getMenuByRestaurantAndDate(long restaurantId, LocalDate mealDate) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new CustomException(ErrorCode.INVALID_RESTAURANT);
        }   // id에 해당하는 식당 없을 때 예외 발생

        String mealDateFormatted = mealDate.format(DATE_FORMATTER);
        return menuRepository.findMenuByRestaurantIdAndMealDate(restaurantId, mealDateFormatted)
                .map(MenuViewDto::toDto)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
    }

    public PinViewDto getPinnedRestaurantMenu(long memberId) {
        Member member = memberRepository.findMemberById(memberId).orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_USER));  // 로그인 정보 없으면
        if (member.getPin() == null) {
            throw new CustomException(ErrorCode.PINNED_RESTAURANT_NOT_FOUND);
        }   // pin한 레스토랑 없으면 예외 발생

        LocalDate today = LocalDate.now();
        String todayDate = today.format(DATE_FORMATTER);
        return menuRepository.findMenuByRestaurantIdAndMealDate(member.getPin().getId(), todayDate)
                .map(PinViewDto::toDto)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
    }

    public List<String> getTop3Menus() {
        LocalDate today = LocalDate.now();
        String todayDate = today.format(DATE_FORMATTER);
        List<Object[]> allMenus = menuRepository.findMenusByMealDate(todayDate);

        // 메뉴 카운트를 위한 Map
        Map<String, Integer> menuCountMap = new HashMap<>();

        for (Object[] menuData : allMenus) {
            String mainMenu = (String) menuData[0];
            // 메뉴를 | 기준으로 분리
            String[] menus = mainMenu.split("\\|");
            for (String menu : menus) {
                menuCountMap.put(menu, menuCountMap.getOrDefault(menu, 0) + 1);
            }
        }

        // 카운트 결과를 기준으로 상위 3개 메뉴를 추출
        return menuCountMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // 카운트 기준으로 내림차순 정렬
                .limit(3) // 상위 3개만 추출
                .map(Map.Entry::getKey) // 메뉴 이름만 추출
                .collect(Collectors.toList());
    }
}