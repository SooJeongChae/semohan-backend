package semohan.semohan.domain.restaurant.dto;

import lombok.Data;

import java.util.List;

@Data
public class PinnedScrappedResponseDto {
    private RestaurantDto pinnedRestaurant;
    private List<RestaurantDto> scrappedRestaurants;

    public PinnedScrappedResponseDto(RestaurantDto pinnedRestaurant, List<RestaurantDto> scrappedRestaurants) {
        this.pinnedRestaurant = pinnedRestaurant;
        this.scrappedRestaurants = scrappedRestaurants;
    }
}
