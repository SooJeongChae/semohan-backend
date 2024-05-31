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
public class MenuViewDto {
    private List<String> mainMenu;

    private List<String> subMenu;

    // mainMenu와 subMenu를 리스트로 반환
    public static MenuViewDto toDto(Menu entity) {
        return MenuViewDto.builder()
                .mainMenu(Arrays.asList(entity.getMainMenu().split("\\|")))
                .subMenu(Arrays.asList(entity.getSubMenu().split("\\|")))
                .build();
    }
}
