package semohan.semohan.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_MEMBER(HttpStatus.FORBIDDEN, "아이디 또는 비밀번호가 틀렸습니다"),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    INCORRECT_VERIFICATION_CODE(HttpStatus.UNAUTHORIZED, "인증 번호가 일치하지 않습니다."),

    INVALID_FORM_DATA(HttpStatus.BAD_REQUEST,"유효하지 않은 형식의 데이터 입니다."),
    INVALID_REPEATED_PASSWORD(HttpStatus.BAD_REQUEST,"비밀번호를 정확히 입력해주세요."),
    MISMATCH_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "입력된 전화번호가 등록된 전화번호와 일치하지 않습니다."),
    ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "이미 가입된 전화번호입니다."),
    ALREADY_USED_ID(HttpStatus.BAD_REQUEST, "이미 사용중인 아이디입니다."),
    ALREADY_USED_NICKNAME(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),
    ALREADY_USED_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "이미 가입된 핸드폰 번호입니다."),

    INVALID_RESTAURANT(HttpStatus.BAD_REQUEST,"유효하지 않은 식당입니다."),

    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),

    ENCODING_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "인코딩하는 과정에 오류가 발생했습니다."),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}