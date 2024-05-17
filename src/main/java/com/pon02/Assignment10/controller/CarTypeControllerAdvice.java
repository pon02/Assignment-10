package com.pon02.Assignment10.controller;

import com.pon02.Assignment10.exception.CarTypeNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;
import java.util.Map;

@ControllerAdvice
public class CarTypeControllerAdvice {

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
}
