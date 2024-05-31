package semohan.semohan.domain.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import semohan.semohan.domain.member.dto.SignUpDto;
import semohan.semohan.domain.restaurant.domain.Restaurant;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String nickname;

    private Date birthday;

    private int point;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "member_restaurant_scrap",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_id")
    )
    private List<Restaurant> scrap;

    @OneToOne(fetch = FetchType.LAZY) /*FetchType.LAZY: 해당 엔티티가 필요할 때까지 로딩 지연시키는 옵션*/
    private Restaurant pin;

    public static Member fromDto(SignUpDto signUpDto) {
        Member member = new Member();
        member.username = signUpDto.getUsername();
        member.password = signUpDto.getPassword();
        member.name = signUpDto.getName();
        member.phoneNumber = signUpDto.getPhoneNumber();
        member.nickname = signUpDto.getNickname();
        member.birthday = signUpDto.getBirthday();
        member.point = 0;
        return member;
    }
}
