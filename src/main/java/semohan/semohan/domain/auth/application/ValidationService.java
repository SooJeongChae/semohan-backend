package semohan.semohan.domain.auth.application;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class ValidationService {
    private static final String PASSWORD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String CODE_CHARS = "0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int CODE_LENGTH = 6;
    private static final int PASSWORD_LENGTH = 8;

    // 인증 번호 생성
    public String createCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CODE_CHARS.charAt(RANDOM.nextInt(CODE_CHARS.length())));
        }
        return code.toString();
    }

    // 임시 비밀번호 생성
    public String createTemporaryPassword() {
        StringBuilder code = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            code.append(PASSWORD_CHARS.charAt(RANDOM.nextInt(PASSWORD_CHARS.length())));
        }
        return code.toString();
    }

    // 인증 번호 검증
    public boolean verifyCode(String inputCode, String generatedCode) {
        if (inputCode == null || generatedCode == null) {
            return false;
        }
        // 입력한 인증 번호와 생성된 인증 번호 비교
        return inputCode.equals(generatedCode);
    }
}
