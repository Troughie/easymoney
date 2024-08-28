package com.example.MoneyLover.config.Mail;

import com.example.MoneyLover.infra.Transaction.Dto.TransEmail;
import com.example.MoneyLover.infra.User.Dto.EmailForgotResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Async
    public void sendMailForgot(MailDto<EmailForgotResponse> request) throws MessagingException {

        Context context = new Context();
        context.setVariable("otp",request.getData().getOtp());
        context.setVariable("email",maskEmail(request.getData().getEmail()));

        sendMailBase(request,context);
    }

    @Override
    @Async
    public void sendMailTransaction(MailDto<TransEmail> request) throws MessagingException {

        Context context = new Context();
        context.setVariable("name",request.getData().getName());
        context.setVariable("amount",request.getData().getAmount());
        context.setVariable("note",request.getData().getNotes());

        sendMailBase(request,context);
    }

    private void sendMailBase(MailDto<?> request,Context context) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom(fromMail);
        mimeMessageHelper.setTo(request.getToEmail());
        mimeMessageHelper.setSubject(request.getSubject());
        String processedString = templateEngine.process(request.getHtmlName(), context);
        mimeMessageHelper.setText(processedString, true);
        mailSender.send(mimeMessage);
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf("@");
        if (atIndex > 2) {
            String prefix = email.substring(0, 2);
            String suffix = email.substring(atIndex - 1);
            return prefix + "****" + suffix;
        }
        return email;
    }
}
