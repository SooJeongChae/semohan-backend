package semohan.semohan.domain.restaurant.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import semohan.semohan.domain.menu.domain.Menu;
import semohan.semohan.domain.menu.repository.MenuRepository;
import semohan.semohan.domain.restaurant.domain.Restaurant;
import semohan.semohan.domain.restaurant.dto.RestaurantDetailDto;
import semohan.semohan.domain.restaurant.dto.RestaurnatDto;
import semohan.semohan.domain.restaurant.repository.RestaurantRepository;
import semohan.semohan.global.exception.CustomException;
import semohan.semohan.global.exception.ErrorCode;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    public List<RestaurnatDto> searchRestaurants(String type, String keyword) {
        List<Restaurant> restaurantList;

        switch (type) {
            // TODO: menu 추가
            case "menu":
                List<Menu> menuList = menuRepository.findMenusByDateAndName(Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now().plusDays(1)), keyword);
                restaurantList = menuList.stream()
                        .map(menu -> menu.getRestaurant())
                        .collect(Collectors.toList());
                break;
            case "name":
                restaurantList = restaurantRepository.findAllByNameContaining(keyword);
                break;
            case "location":
                restaurantList = restaurantRepository.findByAddress_AddressContaining(keyword);
                break;
            default:
                throw new CustomException(ErrorCode.INVALID_FORM_DATA);
        }

        return restaurantList.stream()
                .map(restaurant -> RestaurnatDto.toDto(restaurant))
                .collect(Collectors.toList());
    }

    public RestaurantDetailDto getRestaurant(long id) {
        return RestaurantDetailDto.toDto(restaurantRepository.getReferenceById(id));
    }
}