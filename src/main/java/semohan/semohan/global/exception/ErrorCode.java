package semohan.semohan.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_MEMBER(HttpStatus.FORBIDDEN, "아이디 또는 비밀번호가 틀렸습니다"),

    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),

    INVALID_FORM_DATA(HttpStatus.BAD_REQUEST,"유효하지 않은 형식의 데이터 입니다."),
    INVALID_REPEATED_PASSWORD(HttpStatus.BAD_REQUEST,"비밀번호를 정확히 입력해주세요."),

    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");


    // 예시, 참고해서 필요한 에러 코드 작성하기
//    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 데이터입니다."),
//    UNCORRECTABLE_DATA(HttpStatus.CONFLICT, "현재 수정할 수 없는 데이터입니다."),
//    DUPLICATE(HttpStatus.CONFLICT,"중복된 데이터 입니다."),
//    UPLOAD_PROFILE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "프로필 업데이트 중 오류 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}