package semohan.semohan.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semohan.semohan.domain.member.domain.Member;

import java.util.Date;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberViewDto {
    private String name;

    private String username;

    private String nickname;

    private String phoneNumber;

    private Date birthday;

    private int point;

    public static MemberViewDto toDto (Member entity) {
        return new MemberViewDto(entity.getName(), entity.getUsername(), entity.getNickname(), entity.getPhoneNumber(), entity.getBirthday(), entity.getPoint());
    }
}