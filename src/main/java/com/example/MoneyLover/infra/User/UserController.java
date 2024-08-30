package com.example.MoneyLover.infra.User;

import com.example.MoneyLover.infra.User.Dto.*;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.User.ServiceImpl.IUser;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private IUser iUser;
    @Autowired
    private ResponseException _res;

    @PostMapping("auth/login")
    public ResponseEntity<?> login(@RequestBody SignInDto signInDto)
    {
        var result = iUser.login(signInDto);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("auth/refreshToken")
    public ResponseEntity<?> refresh(@RequestBody RefreshToken refreshToken)
    {
        var result = iUser.refresh(refreshToken);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("auth/register")
    public ResponseEntity<?> register(@RequestBody @Valid SignInDto signInDto)
    {
        var result = iUser.register(signInDto);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("auth/forgot")
    public ResponseEntity<?> forgot(@RequestBody @Valid EmailForgot emailForgot)
    {
        var result = iUser.forgot(emailForgot);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("auth/submitOtp")
    public ResponseEntity<?> submitOtp(@RequestBody @Valid OtpRequest otpRequest) throws BadRequestException {
        var result = iUser.submitOtp(otpRequest);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("auth/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody @Valid PasswordChange passwordChange)
    {
        var result = iUser.changePassword(passwordChange);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("auth/changePasswordForgot")
    public ResponseEntity<?> changePasswordForgot(@RequestBody @Valid PasswordNew passwordNew)
    {
        var result = iUser.changePasswordForgot(passwordNew);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("auth/validate-session")
    public ResponseEntity<?> validSession(@RequestBody @Valid ValidSession validSession)
    {
        var result = iUser.validSession(validSession);
        return _res.responseEntity(result,result.getCode());
    }


    @PostMapping("user/{code}")
    public ResponseEntity<?> getByCode(@PathVariable String code)
    {
        var result = iUser.getUser(code);
        return _res.responseEntity(result,result.getCode());
    }

    @GetMapping("user/search/{code}")
    public ResponseEntity<?> getAll(@AuthenticationPrincipal User user,@PathVariable String code)
    {
        var result = iUser.getUserAll(user,code);
        return _res.responseEntity(result,result.getCode());
    }
}
