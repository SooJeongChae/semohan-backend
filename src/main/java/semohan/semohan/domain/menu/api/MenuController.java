package semohan.semohan.domain.menu.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semohan.semohan.domain.menu.application.MenuService;
import semohan.semohan.domain.menu.dto.MenuViewDto;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    @GetMapping(value = "/{restaurantId}")
    public ResponseEntity<MenuViewDto> getTodayMenuByRestaurant(@PathVariable("restaurantId") long restaurantId) {
        if (menuService.getTodayMenuByRestaurant(restaurantId) == null) {
            return ResponseEntity.notFound().build();   // 해당 레스토랑 없을 때
        }
        return ResponseEntity.ok(menuService.getTodayMenuByRestaurant(restaurantId));
    }

    @GetMapping(value = "/{restaurantId}/{mealDate}")
    public ResponseEntity<MenuViewDto> getMenuByRestaurantAndDate(@PathVariable("restaurantId")  long restaurantId, @PathVariable("mealDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate mealDate) {
        return ResponseEntity.ok(menuService.getMenuByRestaurantAndDate(restaurantId, mealDate));
    }

    @GetMapping(value = "/pin")
    public ResponseEntity<MenuViewDto> getPinnedRestaurantMenu(HttpServletRequest request) {
        long memberId = (Long) request.getSession().getAttribute("id");
        return ResponseEntity.ok(menuService.getPinnedRestaurantMenu(memberId));
    }

    @GetMapping("/hot-menu")
    public ResponseEntity<List<String>> getTop3Menus() {
        return ResponseEntity.ok(menuService.getTop3Menus());
    }
}
