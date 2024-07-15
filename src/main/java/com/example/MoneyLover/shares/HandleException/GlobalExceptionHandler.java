package com.example.MoneyLover.shares.HandleException;

import com.example.MoneyLover.shares.Entity.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ResponseException _res;


    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<ApiResponse<String>> handleException(BadRequestException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiResponse<String> apiResponse = _res.createErrorResponse(e.getMessage(), status.value());
        return new ResponseEntity<>(apiResponse, status);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        // Extract more specific information if possible
        String message = "Duplicate entry error: " + e.getMostSpecificCause().getMessage();
        return new ResponseEntity<>(_res.createErrorResponse(message, HttpStatus.CONFLICT.value()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ApiResponse<String>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return new ResponseEntity<>(_res.createErrorResponse(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED.value()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ApiResponse<String>> handleException(Exception e) {
        if(e.getClass().equals(io.jsonwebtoken.security.SignatureException.class))
        {
            return new ResponseEntity<>(_res.createErrorResponse("JWT token is invalid or expired", HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
        }
        if(e.getClass().getName().equals("org.springframework.security.access.AccessDeniedException")) {
            return new ResponseEntity<>(_res.createErrorResponse("No permission!!!", HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
        }

        System.out.println(e.getClass().getName());
        if(e.getClass().getName().equals("org.springframework.security.authentication.InsufficientAuthenticationException")) {
            return new ResponseEntity<>(_res.createErrorResponse("Authenticated!!!", HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(_res.createErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        HttpStatus status = HttpStatus.valueOf(400);
        ApiResponse<Map<String, String>> apiResponse = _res.createErrorResponse("Invalid field!!!", status.value(), errors);
        return new ResponseEntity<>(apiResponse, status);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ApiResponse<String>> BadCredentialsException(BadCredentialsException e) {
        return new ResponseEntity<>(_res.createErrorResponse("Authenticated!!!", HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiResponse<String>> AccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>(_res.createErrorResponse("No permission!!!", HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<ApiResponse<String>> AccessDeniedException(ExpiredJwtException e) {
        return new ResponseEntity<>(_res.createErrorResponse(e.getMessage(), HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({MalformedJwtException.class})
    public ResponseEntity<ApiResponse<String>> AccessDeniedException(MalformedJwtException e) {
        return new ResponseEntity<>(_res.createErrorResponse("JWT token is invalid or expired", HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
    }
}
