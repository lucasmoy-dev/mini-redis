package com.miniredis.infrastructure.adapters.in.rest.exception;

import com.miniredis.domain.exception.RedisException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RedisException.class)
    public ResponseEntity<String> handleRedisException(RedisException ex) {
        return ResponseEntity.ok(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.ok("ERR " + ex.getMessage());
    }
}
