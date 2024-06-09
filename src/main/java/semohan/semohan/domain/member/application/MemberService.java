package semohan.semohan.domain.member.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import semohan.semohan.domain.member.domain.Member;
import semohan.semohan.domain.member.dto.MemberUpdateDto;
import semohan.semohan.domain.member.dto.MemberViewDto;
import semohan.semohan.domain.member.repository.MemberRepository;
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

        // 입력한 닉네임이랑 같은 닉네임 있으면 예외
        if (memberRepository.findMemberByNickname(memberUpdateDto.getNickname()).isPresent()) {
            if (!member.equals(memberRepository.findMemberByNickname(memberUpdateDto.getNickname()).orElseThrow())) {
                throw new CustomException(ErrorCode.ALREADY_USED_NICKNAME); // 본인이 사용하는 닉네임 아닐 때만 예외 발생
            }
        }
        // entity에 변경된 닉네임 set
        member.setNickname(memberUpdateDto.getNickname());

        // 입력한 핸드폰 번호랑 같은 핸드폰번호 있으면 예외
        if (memberRepository.findMemberByPhoneNumber(memberUpdateDto.getPhoneNumber()).isPresent()) {
            if (!member.equals(memberRepository.findMemberByPhoneNumber(memberUpdateDto.getPhoneNumber()).orElseThrow())) {
                throw new CustomException(ErrorCode.ALREADY_USED_PHONE_NUMBER);
            }
        }
        // entity에 변경된 핸드폰번호 set
        member.setPhoneNumber(memberUpdateDto.getPhoneNumber());

        // entity에 변경된 생일 set
        member.setBirthday(memberUpdateDto.getBirthday());

        return true;
    }
}
