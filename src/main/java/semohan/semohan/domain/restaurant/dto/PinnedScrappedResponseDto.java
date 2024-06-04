package semohan.semohan.domain.restaurant.dto;

import lombok.Data;

import java.util.List;

@Data
public class PinnedScrappedResponseDto {
    private RestaurnatDto pinnedRestaurnat;
    private List<RestaurnatDto> scrappedRestaurnats;

    public PinnedScrappedResponseDto(RestaurnatDto pinnedRestaurnat, List<RestaurnatDto> scrappedRestaurnats) {
        this.pinnedRestaurnat = pinnedRestaurnat;
        this.scrappedRestaurnats = scrappedRestaurnats;
    }
}
