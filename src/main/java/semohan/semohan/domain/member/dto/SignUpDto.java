package semohan.semohan.domain.member.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Builder
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {
    @NotNull
    private String username;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 8자리 이상이어야 하며, 영문자와 숫자를 포함해야 합니다.")
    private String password;
    private String repeatedPassword;

    @NotNull
    private String name;

    @Pattern(regexp= "\\d{3}-\\d{4}-\\d{4}", message="알맞은 형식의 휴대폰 번호를 입력해주세요.")
    @NotNull
    private String phoneNumber;

    @NotNull
    private String nickname;

    private Date birthday;
}
