package semohan.semohan.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semohan.semohan.domain.review.domain.Review;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewViewDto {

    private String nickname;

    private String content;

    private boolean likeRestaurant;

    private boolean likeMenu;

    public static ReviewViewDto toDto (Review entity) {
        return new ReviewViewDto( entity.getMember().getNickname(), entity.getContent(), entity.isLikeRestaurant(), entity.isLikeRestaurant());
    }
}
