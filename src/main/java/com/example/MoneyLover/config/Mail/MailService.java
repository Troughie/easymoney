package com.example.MoneyLover.config.Mail;

import com.example.MoneyLover.infra.Transaction.Dto.TransEmail;
import com.example.MoneyLover.infra.User.Dto.EmailForgotResponse;
import jakarta.mail.MessagingException;

public interface MailService {
    void sendMailForgot(MailDto<EmailForgotResponse> request) throws MessagingException;

    void sendMailTransaction(MailDto<TransEmail> request) throws MessagingException;
}
