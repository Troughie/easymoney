package com.example.MoneyLover.shares.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ApiResponse<T> {
    private boolean success;

    private String message;

    private ZonedDateTime timestamp;

    private int code;

    private T data;


    @Builder
    public ApiResponse(boolean success, String message, int code, T data) {
        this.success = success;
        this.message = message;
        this.timestamp = ZonedDateTime.now(ZoneId.of("Z"));
        this.code = code;
        this.data = data;
    }
}
