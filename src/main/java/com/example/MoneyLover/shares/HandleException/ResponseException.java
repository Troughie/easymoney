package com.example.MoneyLover.shares.HandleException;

import com.example.MoneyLover.shares.Entity.ApiResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseException {
    public  <T> ApiResponse<T> createErrorResponse(String errorMessage, int statusCode) {
        return new ApiResponse<>(false, errorMessage, statusCode, null);
    }

    public  <T> ApiResponse<T> createErrorResponse(String errorMessage, int statusCode, T data) {
        return new ApiResponse<>(false, errorMessage, statusCode, data);
    }

    public  <T> ApiResponse<T> createSuccessResponse(String successMessage, int statusCode, T data) {
        return new ApiResponse<>(true, successMessage, statusCode, data);
    }

    public  <T> ApiResponse<T> createSuccessResponse( int statusCode, T data) {
        return new ApiResponse<>(true, "Successfully", statusCode, data);
    }

    public  <T> ApiResponse<T> createSuccessResponse(String successMessage, int statusCode) {
        return new ApiResponse<>(true, successMessage, statusCode, null);
    }

    public  <T> ResponseEntity<T> responseEntity(T data, int code)
    {
        return new ResponseEntity<>(data, HttpStatusCode.valueOf(code));
    }
}
