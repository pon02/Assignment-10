package com.pon02.Assignment10.controller;

import com.pon02.Assignment10.controller.response.ErrorResponse;
import com.pon02.Assignment10.exception.CarTypeNotFoundException;
import com.pon02.Assignment10.exception.OrderNotFoundException;
import com.pon02.Assignment10.exception.SectionNotFoundException;
import com.pon02.Assignment10.exception.StaffNotFoundException;
import com.pon02.Assignment10.exception.FieldNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

    //Orderが見つからない場合の例外処理
    @ExceptionHandler(value= OrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotFoundException(OrderNotFoundException e, HttpServletRequest request){
        String queryString = request.getQueryString();
        String decodedQuery = queryString != null ? URLDecoder.decode(queryString, StandardCharsets.UTF_8) : null;
        Map<String, String> body = Map.of(
                "status", String.valueOf(HttpStatus.NOT_FOUND.value()),
                "error", HttpStatus.NOT_FOUND.getReasonPhrase(),
                "path", request.getRequestURI() + (decodedQuery != null ? "?" + decodedQuery : ""),
                "timestamp", ZonedDateTime.now().toString(),
                "message", e.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    //CarTypeNameが見つからない場合の例外処理
    @ExceptionHandler(value=CarTypeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCarTypeNotFoundException(CarTypeNotFoundException e, HttpServletRequest request){
        String queryString = request.getQueryString();
        String decodedQuery = queryString != null ? URLDecoder.decode(queryString, StandardCharsets.UTF_8) : null;
        Map<String, String> body = Map.of(
                "status", String.valueOf(HttpStatus.NOT_FOUND.value()),
                "error", HttpStatus.NOT_FOUND.getReasonPhrase(),
                "path", request.getRequestURI() + (decodedQuery != null ? "?" + decodedQuery : ""),
                "timestamp", ZonedDateTime.now().toString(),
                "message", e.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    //Staffが見つからない場合の例外処理
    @ExceptionHandler(value= StaffNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleStaffNotFoundException(StaffNotFoundException e, HttpServletRequest request){
        String queryString = request.getQueryString();
        String decodedQuery = queryString != null ? URLDecoder.decode(queryString, StandardCharsets.UTF_8) : null;
        Map<String, String> body = Map.of(
                "status", String.valueOf(HttpStatus.NOT_FOUND.value()),
                "error", HttpStatus.NOT_FOUND.getReasonPhrase(),
                "path", request.getRequestURI() + (decodedQuery != null ? "?" + decodedQuery : ""),
                "timestamp", ZonedDateTime.now().toString(),
                "message", e.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    //Sectionが見つからない場合の例外処理
    @ExceptionHandler(value= SectionNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSectionNotFoundException(SectionNotFoundException e, HttpServletRequest request){
        String queryString = request.getQueryString();
        String decodedQuery = queryString != null ? URLDecoder.decode(queryString, StandardCharsets.UTF_8) : null;
        Map<String, String> body = Map.of(
                "status", String.valueOf(HttpStatus.NOT_FOUND.value()),
                "error", HttpStatus.NOT_FOUND.getReasonPhrase(),
                "path", request.getRequestURI() + (decodedQuery != null ? "?" + decodedQuery : ""),
                "timestamp", ZonedDateTime.now().toString(),
                "message", e.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    //Fieldが見つからない場合の例外処理
    @ExceptionHandler(value= FieldNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleFieldNotFoundException(FieldNotFoundException e, HttpServletRequest request){
        String queryString = request.getQueryString();
        String decodedQuery = queryString != null ? URLDecoder.decode(queryString, StandardCharsets.UTF_8) : null;
        Map<String, String> body = Map.of(
                "status", String.valueOf(HttpStatus.NOT_FOUND.value()),
                "error", HttpStatus.NOT_FOUND.getReasonPhrase(),
                "path", request.getRequestURI() + (decodedQuery != null ? "?" + decodedQuery : ""),
                "timestamp", ZonedDateTime.now().toString(),
                "message", e.getMessage()
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

    //DELETEリクエストの例外処理
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException e, HttpServletRequest request){
        String queryString = request.getQueryString();
        String decodedQuery = queryString != null ? URLDecoder.decode(queryString, StandardCharsets.UTF_8) : null;
        Map<String, String> body = Map.of(
                "status", String.valueOf(HttpStatus.BAD_REQUEST.value()),
                "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "path", request.getRequestURI() + (decodedQuery != null ? "?" + decodedQuery : ""),
                "timestamp", ZonedDateTime.now().toString(),
                "message", e.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
