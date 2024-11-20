package com.example.wewear_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoHandlerFound(NoHandlerFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "The requested route does not exist");
        response.put("path", ex.getRequestURL());
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
} 