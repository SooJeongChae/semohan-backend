package semohan.semohan.domain.review.domain;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import semohan.semohan.domain.member.domain.Member;
import semohan.semohan.domain.menu.domain.Menu;
import semohan.semohan.domain.restaurant.domain.Restaurant;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String content;

    @CreationTimestamp
    private LocalDateTime writeTime;

    private boolean likeRestaurant;  // 식당 좋아요 여부

    private boolean likeMenu;        // 메뉴 좋아요 여부

    @NotNull
    @ManyToOne
    private Restaurant restaurant;

    @NotNull
    @ManyToOne
    private Menu menu;

    @NotNull
    @ManyToOne
    private Member member;
}