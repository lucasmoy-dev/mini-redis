package com.miniredis.infrastructure.adapters.in.rest.exception;

import com.miniredis.domain.exception.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RedisException.class)
    public ResponseEntity<String> handleRedisException(RedisException ex) {
        log.warn("Redis specific error: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return ResponseEntity.internalServerError().body("ERR " + ex.getMessage());
    }
}
