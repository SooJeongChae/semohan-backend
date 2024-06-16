package semohan.semohan.domain.restaurant.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import semohan.semohan.domain.member.domain.Member;
import semohan.semohan.global.s3.Image;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String businessHours;

    @NotNull
    private String price;

    @NotNull
    @Embedded
    private Address address;

    @ManyToOne
    @JoinColumn(name="image")
    private Image image;

    @ManyToMany(mappedBy = "scrap", fetch = FetchType.LAZY)
    private List<Member> members;

    @NotNull
    private int likesRestaurant = 0;  // 좋아요 수
}