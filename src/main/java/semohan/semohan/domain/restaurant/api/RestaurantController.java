package semohan.semohan.domain.restaurant.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semohan.semohan.domain.restaurant.application.RestaurantService;
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
    public ResponseEntity<RestaurantDetailDto> getRestaurantDetail(@PathVariable long id) {
        return ResponseEntity.ok(restaurantService.getRestaurant(id));
    }

}
