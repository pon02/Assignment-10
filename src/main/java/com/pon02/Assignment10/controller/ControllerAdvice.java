package com.pon02.Assignment10.controller;

import com.pon02.Assignment10.exception.CarTypeNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {

    //CarTypeNameが見つからない場合の例外処理
    @ExceptionHandler(value=CarTypeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCarTypeNotFoundException(CarTypeNotFoundException e, HttpServletRequest request){
        Map<String, String> body = Map.of(
                "status", String.valueOf(HttpStatus.NOT_FOUND.value()),
                "error", HttpStatus.NOT_FOUND.getReasonPhrase(),
                "path", request.getRequestURI(),
                "timestamp", ZonedDateTime.now().toString()
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    //バリデーションエラーの例外処理
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<Map<String, String>> errors = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            Map<String, String> error = new HashMap<>();
            error.put("field", fieldError.getField());
            error.put("message", fieldError.getDefaultMessage());
            errors.add(error);
        });
        ErrorResponse errorResponse =
                new ErrorResponse(HttpStatus.BAD_REQUEST, "validation error", errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    //バリデーションエラーの例外処理で使用するエラーレスポンスのクラス
    public static final class ErrorResponse {
        private final HttpStatus status;
        private final String message;
        private final List<Map<String, String>> errors;

        public ErrorResponse(HttpStatus status, String message, List<Map<String, String>> errors) {
            this.status = status;
            this.message = message;
            this.errors = errors;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public List<Map<String, String>> getErrors() {
            return errors;
        }

    }
}
