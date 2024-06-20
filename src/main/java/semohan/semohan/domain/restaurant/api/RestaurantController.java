package semohan.semohan.domain.restaurant.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semohan.semohan.domain.restaurant.application.RestaurantService;
import semohan.semohan.domain.restaurant.dto.PinnedScrappedRequestDto;
import semohan.semohan.domain.restaurant.dto.PinnedScrappedResponseDto;
import semohan.semohan.domain.restaurant.dto.RestaurantDetailDto;
import semohan.semohan.domain.restaurant.dto.RestaurnatDto;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;

    // 근처에 있는 식당
    @GetMapping("/nearby")
    public ResponseEntity<List<RestaurnatDto>> getRestaurantsByLocationInfo(@CookieValue(value = "myCookie", defaultValue = "성북구") String region) {
        return ResponseEntity.ok(restaurantService.searchRestaurants("location", region));
    }

    // 식당 검색
    @GetMapping("/search")
    public ResponseEntity<List<RestaurnatDto>> searchRestaurants(
            @RequestParam(value = "menu", required = false) String menu,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "name", required = false) String name
    ) {
        if(menu != null) {
            return ResponseEntity.ok(restaurantService.searchRestaurants("menu", menu));
        } else if(location != null) {
            return ResponseEntity.ok(restaurantService.searchRestaurants("location", location));
        } else if(name != null) {
            return ResponseEntity.ok(restaurantService.searchRestaurants("name", name));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<RestaurantDetailDto> getRestaurantDetail(@PathVariable("id") long id) {
        return ResponseEntity.ok(restaurantService.getRestaurant(id));
    }

    @GetMapping("/scrap-pin")
    public ResponseEntity<PinnedScrappedResponseDto> getPinnedScrapped(HttpServletRequest httpServletRequest) {
        long id = (long) httpServletRequest.getSession().getAttribute("id");
        return ResponseEntity.ok(restaurantService.getPinnedScrappedRestaurants(id));
    }

    @PostMapping("/scrap-pin/update")
    public ResponseEntity<Boolean> updatePinnedScrapped(HttpServletRequest httpServletRequest, @RequestBody PinnedScrappedRequestDto dto) {
        long id = (long) httpServletRequest.getSession().getAttribute("id");
        return ResponseEntity.ok(restaurantService.updatePinnedScrappedRestaurants(id, dto));
    }

    @PostMapping("/scrap/{restaurantId}")
    public ResponseEntity<Boolean> scrapRestaurant(HttpServletRequest httpServletRequest, @PathVariable("restaurantId") long restaurantId) {
        long memberId = (long) httpServletRequest.getSession().getAttribute("id");
        return ResponseEntity.ok(restaurantService.scrapRestaurant(memberId, restaurantId));
    }

    @PostMapping("/delete-scrap/{restaurantId}")
    public ResponseEntity<Boolean> deleteScrapRestaurant(HttpServletRequest httpServletRequest, @PathVariable("restaurantId") long restaurantId) {
        long memberId = (long) httpServletRequest.getSession().getAttribute("id");
        return ResponseEntity.ok(restaurantService.deleteScrapRestaurant(memberId, restaurantId));
    }
}
