package semohan.semohan.domain.auth.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import semohan.semohan.domain.auth.application.AuthService;
import semohan.semohan.domain.auth.dto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Validated
    @PostMapping(value = "/sign-up/send")
    public ResponseEntity<Boolean> sendSmsForSignUp(@RequestParam("phoneNumber") @Pattern(regexp= "\\d{3}-\\d{4}-\\d{4}") String phoneNumber) {
        return ResponseEntity.ok(authService.sendVerifySms(phoneNumber));
    }

    @PostMapping(value = "/sign-up/confirm")
    public ResponseEntity<Boolean> verifySmsForSignUp(@RequestBody @Validated SmsVerificationDto smsVerificationDto, HttpServletRequest httpServletRequest) {
        boolean isVerified = authService.verifySmsForSignUp(smsVerificationDto);    // redis에 저장된 인증번호와 사용자 입력 인증번호와 일치하는지
        if (isVerified) {
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute("isVerified", true);
        }   // 일치 여부 세션에 저장
        return ResponseEntity.ok(true);
    }

    @PostMapping(value = "/sign-up/submit")
    public ResponseEntity<Boolean> signUp(@RequestBody @Validated SignUpDto signUpDto, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        Boolean isVerified = (Boolean) session.getAttribute("isVerified");
        // 세션에서 인증 상태 확인
        if (Boolean.TRUE.equals(isVerified)) {
            // service 클래스 반환값 true일 때
            if (authService.signUp(signUpDto)) {
                session.removeAttribute("isVerified"); // 세션에서 인증 상태 제거
                return ResponseEntity.ok(true);
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false); // 세션 확인 불가
    }

    @PostMapping(value = "/sign-in")
    public ResponseEntity<Boolean> signIn(@RequestBody SignInDto signInDto, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute("id", authService.signIn(signInDto));
        return ResponseEntity.ok(true);
    }

    @PostMapping(value = "/sign-out")
    public ResponseEntity<Boolean> signOut(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().invalidate();
        return ResponseEntity.ok(true);
    }

    @Validated
    @PostMapping(value = "/find-id/send")
    public ResponseEntity<Boolean> sendSmsForFindId(@RequestParam("phoneNumber") @Pattern(regexp= "\\d{3}-\\d{4}-\\d{4}") String phoneNumber) {
        return ResponseEntity.ok(authService.sendVerifySms(phoneNumber));
    }

    @PostMapping(value = "/find-id/confirm")
    public ResponseEntity<String> verifySmsForFindId(@RequestBody @Validated SmsVerificationDto request) {
        return ResponseEntity.ok(authService.verifySms(request));
    }

    @PostMapping(value="/request-temporary-password/send")
    public ResponseEntity<Boolean> sendSmsForResetPassword(@RequestBody @Validated TemporaryPasswordRequestDto request) {
        return ResponseEntity.ok(authService.sendSmsForResetPassword(request));
    }

    @PostMapping(value="/request-temporary-password/confirm")
    public ResponseEntity<Boolean> sendTempPassword(@RequestBody @Validated TemporaryPasswordVerificationDto request) {
        return ResponseEntity.ok(authService.sendTempPassword(request));
    }
}
