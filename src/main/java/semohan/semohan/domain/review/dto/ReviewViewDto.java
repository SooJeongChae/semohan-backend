package semohan.semohan.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semohan.semohan.domain.review.domain.Review;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewViewDto {

    private long id;

    private LocalDateTime writeTime;

    private String nickname;

    private String content;

    private boolean likeRestaurant;

    private boolean likeMenu;

    public static ReviewViewDto toDto (Review entity) {
        return new ReviewViewDto( entity.getId(), entity.getWriteTime(), entity.getMember().getNickname(), entity.getContent(), entity.isLikeRestaurant(), entity.isLikeMenu());
    }
}
