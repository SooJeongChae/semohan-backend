package semohan.semohan.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreationDto {

    @NotNull
    private String content;

    @NotNull
    private int mealType;   // 0, 1, 2로 구분. 0: 올데이, 1: 점심, 2: 저녁

    private boolean likeRestaurant;

    private boolean likeMenu;
}