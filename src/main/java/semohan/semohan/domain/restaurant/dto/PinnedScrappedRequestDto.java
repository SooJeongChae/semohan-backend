package semohan.semohan.domain.restaurant.dto;

import lombok.Data;

import java.util.List;

@Data
public class PinnedScrappedRequestDto {
    private Long pinnedRestaurantId;
    private List<Long> scrappedRestaurantIdList;
}
