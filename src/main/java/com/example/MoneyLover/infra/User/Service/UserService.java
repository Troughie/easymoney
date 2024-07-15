package com.example.MoneyLover.infra.User.Service;

import com.example.MoneyLover.infra.User.Dto.*;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.shares.Entity.ApiResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User loadUserByPhone(String phone) throws BadRequestException;

    User getUserByPhone(String phone);

    ApiResponse<?> register(SignInDto signInDto);
    ApiResponse<?> forgot(EmailForgot emailForgot);

    ApiResponse<?> submitOtp(OtpRequest otpRequest) throws BadRequestException;

    ApiResponse<?> changePasswordForgot(PasswordNew passwordNew);

    ApiResponse<?> changePassword(PasswordChange passwordChange);

    ApiResponse<?> refresh(RefreshToken refreshToken) ;
}
