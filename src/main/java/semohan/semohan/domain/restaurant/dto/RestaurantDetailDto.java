package semohan.semohan.domain.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import semohan.semohan.domain.restaurant.domain.Restaurant;

@Data
@AllArgsConstructor
public class RestaurantDetailDto {
    private long id;

    // 메뉴 Dto 추가 필요

    private String name;

    private String phoneNumber;

    private String businessHours;

    private String price;

    private String address;

    private String s3Url;

    static public RestaurantDetailDto toDto(Restaurant entity) {
        return new RestaurantDetailDto(entity.getId(), entity.getName(), entity.getPhoneNumber(), entity.getBusinessHours(), entity.getPrice(), entity.getAddress().getFullAddress(), entity.getImage().getS3Url());
    };
}
