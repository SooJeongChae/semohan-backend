package semohan.semohan.domain.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import semohan.semohan.domain.restaurant.domain.Restaurant;


@Data
@AllArgsConstructor
public class RestaurantDto {
    private long id;

    private String name;

    private String s3Url;

    public static RestaurantDto toDto(Restaurant entity) {
        return new RestaurantDto(entity.getId(), entity.getName(), entity.getImage().getS3Url());
    }
}
