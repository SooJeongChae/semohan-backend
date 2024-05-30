package semohan.semohan.domain.auth.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import semohan.semohan.domain.auth.domain.Member;
import semohan.semohan.domain.auth.dto.*;
import semohan.semohan.domain.auth.repository.MemberRepository;
import semohan.semohan.global.exception.CustomException;
import semohan.semohan.global.exception.ErrorCode;

import static semohan.semohan.global.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final SmsService smsService;
    private final ValidationService validationService;
    private final RedisService redisService;

    public boolean verifySmsForSignUp(SmsVerificationDto smsVerificationDto) {
        // redis에 저장된 인증코드 꺼내오기
        String verificationCode = redisService.getData(smsVerificationDto.getPhoneNumber());

        // 사용자 입력 코드랑 저장된 인증 코드랑 일치 확인
        if (validationService.verifyCode(smsVerificationDto.getVerificationCode(), verificationCode)) {
            // phoneNumber로 memberRepository 검색 후 없으면 true 리턴
            if (memberRepository.findMemberByPhoneNumber(smsVerificationDto.getPhoneNumber()).isEmpty()) {
                return true;
            } else {
                throw new CustomException(ErrorCode.ALREADY_REGISTERED);  // 해당 번호로 가입된 회원 있을 시 예외 발생
            }
        } else {
            throw new CustomException(ErrorCode.INCORRECT_VERIFICATION_CODE); // 인증번호 불일치하면 예외 발생
        }
    }

    public boolean signUp(SignUpDto signUpDto) {
        // 입력한 id랑 같은 id 있으면 예외
        if (memberRepository.findMemberByUsername(signUpDto.getUsername()).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_USED_ID);
        }

        // 입력한 닉네임이랑 같은 닉네임 있으면 예외
        if (memberRepository.findMemberByNickname(signUpDto.getNickname()).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_USED_NICKNAME);
        }

        // 비밀번호와 비밀번호 재입력 잘 입력했는지 확인
        if (signUpDto.getPassword().equals(signUpDto.getRepeatedPassword())){
            // signUpDto로 member 만들기
            Member member = Member.fromDto(signUpDto);
            memberRepository.save(member);
            return true;  // 성공적으로 저장되면 true 반환
        } else {
            throw new CustomException(INVALID_REPEATED_PASSWORD);
        }
    }

    public long signIn(SignInDto signInDto) {
        // username으로 member 가져오기
        Member member = memberRepository.findMemberByUsername(signInDto.getUsername()).orElseThrow();

        // 비밀번호 확인
        if(!signInDto.getPassword().equals(member.getPassword())) {
            throw new CustomException(INVALID_MEMBER);
        }
        return member.getId();
    }

    public boolean sendVerifySms(String phoneNumber) {
        //수신번호 형태에 맞춰 "-"을 ""로 변환
        String phoneNumberWithoutDashes = phoneNumber.replaceAll("-","");
        String verificationCode = validationService.createCode();
        smsService.sendVerifySms(phoneNumberWithoutDashes, verificationCode);

        //인증코드 유효기간 5분 설정
        redisService.setDataExpire(phoneNumber, verificationCode, 60 * 5L);
        return true;
    }

    public String verifySms(SmsVerificationDto request) {
        // redis에 저장된 인증코드 꺼내오기
        String verificationCode = redisService.getData(request.getPhoneNumber());

        // 사용자 입력 코드랑 저장된 인증 코드랑 일치 확인
        if (validationService.verifyCode(request.getVerificationCode(), verificationCode)) {
            // phoneNumber로 member 가져오기
            Member member = memberRepository.findMemberByPhoneNumber(request.getPhoneNumber()).orElse(null);

            // 일치 유저 없으면 예외
            if (member == null) {
                throw new CustomException(MEMBER_NOT_FOUND);
            } else {
                redisService.deleteData(request.getPhoneNumber());
                return member.getUsername(); // 인증 코드 일치, 유저 있으면 redis 삭제 후 아이디 반환
            }
        } else {
            throw new CustomException(INCORRECT_VERIFICATION_CODE);
        }
    }

    public boolean sendSmsForResetPassword(TemporaryPasswordRequestDto request) {
        // 아이디로 사용자 조회
        Member member = memberRepository.findMemberByUsername(request.getUsername()).orElse(null);
        if (member == null) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
        
        //수신번호 형태에 맞춰 "-"을 ""로 변환
        String phoneNumberWithoutDashes = request.getPhoneNumber().replaceAll("-","");
        String verificationCode = validationService.createCode();
        smsService.sendVerifySms(phoneNumberWithoutDashes, verificationCode);

        //인증코드 유효기간 5분 설정
        redisService.setDataExpire(request.getPhoneNumber(), verificationCode, 60 * 5L);
        return true;
    }

    public boolean sendTempPassword(TemporaryPasswordVerificationDto request) {
        // redis에 저장된 인증코드 꺼내오기
        String verificationCode = redisService.getData(request.getPhoneNumber());

        // 사용자 입력 코드랑 저장된 인증 코드랑 일치 확인
        if (validationService.verifyCode(request.getVerificationCode(), verificationCode)) {
            // 아이디로 사용자 조회
            Member member = memberRepository.findMemberByUsername(request.getUsername()).orElse(null);
            if (member == null) {
                throw new CustomException(MEMBER_NOT_FOUND);
            }
            
            // 입력한 아이디로 찾은 member의 휴대전화 번호와 입력한 휴대전화 번호가 일치하는지 확인
            if (member.getPhoneNumber().equals(request.getPhoneNumber())) {
                String tempPassword = validationService.createTemporaryPassword();  // 임시 패스워드 생성
                member.setPassword(tempPassword);
                memberRepository.save(member); // 변경된 비밀번호 저장

                //수신번호 형태에 맞춰 "-"을 ""로 변환
                String phoneNumberWithoutDashes = request.getPhoneNumber().replaceAll("-","");
                smsService.sendTemporaryPassword(phoneNumberWithoutDashes, tempPassword);
                redisService.deleteData(request.getPhoneNumber());  // redis 저장 데이터 삭제
                return true; // 임시 비밀번호 문자 전송 성공
            } else {
                throw new CustomException(MISMATCH_PHONE_NUMBER);    // 저장된 폰번호와 입력된 폰번호 불일치
            }
        } else {
            throw new CustomException(INCORRECT_VERIFICATION_CODE);
        }
    }
}
