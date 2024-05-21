package semohan.semohan.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handle(CustomException e) {
        return ErrorResponseDto.toResponseEntity(e.getErrorCode(), e.getMessage());
    }


}
