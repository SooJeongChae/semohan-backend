package semohan.semohan.domain.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import semohan.semohan.domain.restaurant.domain.Restaurant;

@Data
@AllArgsConstructor
public class RestaurantDetailDto {
    private long id;

    private String name;

    private String phoneNumber;

    private String businessHours;

    private String price;

    private String address;

    private String s3Url;

    private int likesRestaurant;

    static public RestaurantDetailDto toDto(Restaurant entity) {
        return new RestaurantDetailDto(entity.getId(), entity.getName(), entity.getPhoneNumber(), entity.getBusinessHours(), entity.getPrice(), entity.getAddress().getFullAddress(), entity.getImage().getS3Url(), entity.getLikesRestaurant());
    }
}
