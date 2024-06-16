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

    private boolean likeRestaurant;

    private boolean likeMenu;
}