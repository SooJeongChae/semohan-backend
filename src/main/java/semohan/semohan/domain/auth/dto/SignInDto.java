package semohan.semohan.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInDto {

    @NotNull
    private String username;

    @NotNull
    private String password;
}