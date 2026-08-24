package com.sparta.team4.commerce_payment_system.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 프로젝트에서 의도적으로 발생시킨 비즈니스 예외
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.from(errorCode));
    }

    // 2. @Valid 검증 실패 -> 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        ErrorResponse body = new ErrorResponse(
                ErrorCode.INVALID_REQUEST.name(),
                message
        );

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(body);
    }

    // 3. 내부 방어 코드에서 발생한 잘못된 인자 -> 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        ErrorResponse body = new ErrorResponse(
                ErrorCode.INVALID_REQUEST.name(),
                e.getMessage()
        );

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(body);
    }

    // 4. 예상하지 못한 예외 -> 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception e) {
        // 클라이언트에는 스택트레이스를 노출하지 않고, 서버 로그에는 실제 원인을 남긴다.
        log.error("Unexpected exception", e);

        ErrorResponse body = new ErrorResponse(
                "INTERNAL_ERROR",
                "서버 오류가 발생했습니다."
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }
}