package semohan.semohan.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semohan.semohan.domain.menu.domain.Menu;

import java.util.Arrays;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PinViewDto {
    private String restaurantName;

    private List<String> mainMenu;

    private List<String> subMenu;

    // mainMenu와 subMenu를 리스트로 반환
    public static PinViewDto toDto(Menu entity) {
        return PinViewDto.builder()
                .restaurantName(entity.getRestaurant().getName())
                .mainMenu(Arrays.asList(entity.getMainMenu().split("\\|")))
                .subMenu(Arrays.asList(entity.getSubMenu().split("\\|")))
                .build();
    }
}
