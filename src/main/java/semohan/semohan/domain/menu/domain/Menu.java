package semohan.semohan.domain.menu.domain;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import semohan.semohan.domain.restaurant.domain.Restaurant;

import java.util.Date;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Temporal(TemporalType.DATE)    // 시간 정보 없이 날짜만 저장
    private Date mealDate;

    @NotNull
    private String mainMenu; // '|'로 구분된 문자열

    @NotNull
    private String subMenu; // '|'로 구분된 문자열

    @NotNull
    private int mealType;   // 0, 1, 2로 구분. 0: 올데이, 1: 점심, 2: 저녁

    @NotNull
    @ManyToOne
    private Restaurant restaurant;
}