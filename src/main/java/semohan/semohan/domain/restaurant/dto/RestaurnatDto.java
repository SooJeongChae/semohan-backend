package semohan.semohan.domain.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import semohan.semohan.domain.restaurant.domain.Restaurant;


@Data
@AllArgsConstructor
public class RestaurnatDto {
    private long id;

    private String name;

    private String s3Url;

    public static RestaurnatDto toDto(Restaurant entity) {
        return new RestaurnatDto(entity.getId(), entity.getName(), entity.getImage().getS3Url());
    }
}
