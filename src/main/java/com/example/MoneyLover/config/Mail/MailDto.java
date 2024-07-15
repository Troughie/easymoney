package com.example.MoneyLover.config.Mail;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailDto<T> {
    @JsonAlias(value = "to_email")
    private String toEmail;

    private String subject;

    private T data;

    private String htmlName;
}
