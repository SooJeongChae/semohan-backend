package semohan.semohan.domain.auth.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import semohan.semohan.domain.auth.domain.Member;
import semohan.semohan.domain.auth.dto.MemberUpdateDto;
import semohan.semohan.domain.auth.dto.MemberViewDto;
import semohan.semohan.domain.auth.repository.MemberRepository;
import semohan.semohan.global.exception.CustomException;
import semohan.semohan.global.exception.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberViewDto getMemberInfo(long id) {
        return MemberViewDto.toDto(memberRepository.findMemberById(id).orElseThrow());
    }

    @Transactional
    public boolean updateMemberInfo(Long id, MemberUpdateDto memberUpdateDto) {

        // id로 owner 가져오기
        Member member = memberRepository.findMemberById(id).orElseThrow();

        // entity에 변경된 비밀번호 set
        if(memberUpdateDto.getPassword().equals(memberUpdateDto.getRepeatedPassword())) {
            member.setPassword(memberUpdateDto.getPassword());
        } else {
            throw new CustomException(ErrorCode.INVALID_REPEATED_PASSWORD);
        }

        // entity에 변경된 닉네임 set
        member.setNickname(memberUpdateDto.getNickname());

        // entity에 변경된 핸드폰번호 set
        member.setPhoneNumber(memberUpdateDto.getPhoneNumber());

        // entity에 변경된 생일 set
        member.setBirthday(memberUpdateDto.getBirthday());

        return true;
    }
}
