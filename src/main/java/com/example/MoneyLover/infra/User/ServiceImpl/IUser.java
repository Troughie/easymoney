package com.example.MoneyLover.infra.User.ServiceImpl;

import com.example.MoneyLover.config.Jwt.JwtService;
import com.example.MoneyLover.config.Mail.MailDto;
import com.example.MoneyLover.config.Mail.MailService;
import com.example.MoneyLover.config.Redis.RedisService;
import com.example.MoneyLover.infra.User.Dto.*;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.User.Mapper.UserMapper;
import com.example.MoneyLover.infra.User.Repository.UserRepository;
import com.example.MoneyLover.infra.User.Service.UserService;
import com.example.MoneyLover.shares.Entity.ApiResponse;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import com.example.MoneyLover.utils.GenerateString;
import com.example.MoneyLover.utils.typeGenerate;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class IUser implements UserService {

    private final UserRepository userRepo;

    private final ResponseException _res;

    private final  AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final MailService mailService;

    private final GenerateString generateString;

    private final RedisService redisService;

    private final PasswordEncoder passwordEncoder;
    private JwtResponse toJwtResponse(String accessToken,String refreshToken,User user){
        return new JwtResponse(accessToken,refreshToken, UserMapper.INSTANCE.toUserResponse(user));
    }
    @Override
    public User loadUserByPhone(String phone) throws BadRequestException {
        return userRepo.findByEmail(phone).orElseThrow(()->new BadRequestException("Phone number not found!!!"));
    }

    @Override
    public User getUserByPhone(String email) {
        return userRepo.findTopByEmail(email);
    }

    public ApiResponse<?> login(SignInDto signInDto)
    {
        String email = signInDto.getEmail();
        String password =signInDto.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        User user = userRepo.findTopByEmail(email);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return _res.createSuccessResponse("Login successfully", 200,toJwtResponse(accessToken,refreshToken,user));
    }

    public ApiResponse<?> register(SignInDto signInDto)
    {
        try {
            User user1 = userRepo.findTopByEmail(signInDto.getEmail());
            if (user1 != null) {
                return _res.createErrorResponse("Email already exists", 500);
            }

            User user = UserMapper.INSTANCE.registerUser(signInDto);
            user.setPassword(passwordEncoder.encode(signInDto.getPassword()));
            user.setUsername(signInDto.getUsername().isBlank()?GenerateString.generateString(10,typeGenerate.string):signInDto.getUsername());
            userRepo.save(user);
            return _res.createSuccessResponse("Register successfully", 200,user);
        }catch(Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    public ApiResponse<?> forgot(EmailForgot emailForgot)
    {
        try {
            User user = userRepo.findTopByEmail(emailForgot.getEmail());

            String otp = GenerateString.generateString(8, typeGenerate.number);
            String session = GenerateString.generateString(14,typeGenerate.string);
            //set otp to  cache
            redisService.setValue("otp"+emailForgot.getEmail(),otp,5, TimeUnit.MINUTES);
            redisService.setValue("session"+user.getId(),session,11, TimeUnit.MINUTES);
            //send email
            EmailForgotResponse emailForgotResponse = new EmailForgotResponse(emailForgot.getEmail(),otp);
            MailDto<EmailForgotResponse> maildto = new MailDto<EmailForgotResponse>(emailForgot.getEmail(),"Forgot password in money lover app",emailForgotResponse,"mail/mailer.html");
            mailService.sendMailForgot(maildto);
            return _res.createSuccessResponse("Success", 200,new Object[]{user.getId(),session});
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);

        }
    }

    public ApiResponse<?> validSession(ValidSession validSession){
        try {
        String session =redisService.getValue("session"+validSession.getAccount(),String.class);
        if(!validSession.getSession().equalsIgnoreCase(session)){
            return _res.createSuccessResponse( 200,false);
        }

        return _res.createSuccessResponse( 200,true);
        }catch (Exception e){
            return _res.createErrorResponse(e.getMessage(), 500);
        }
    }

    public ApiResponse<?> submitOtp(OtpRequest otpRequest) throws BadRequestException {
        try {
            User user = userRepo.findTopById(otpRequest.getAccount());
            String otpCurrent =redisService.getValue("otp"+user.getEmail(),String.class);
            String otpSent = otpRequest.getOtp();
            if(!otpSent.equals(otpCurrent)){
                throw new BadRequestException("Otp invalid or expired!!");
            }
            redisService.setValue(user.getEmail(),user.getEmail(),5,TimeUnit.MINUTES);
            return _res.createSuccessResponse("Success", 200);
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(), 500);
        }
    }

    public ApiResponse<?> changePasswordForgot(PasswordNew passwordNew)
    {
        try {
            User user = userRepo.findTopById(passwordNew.getAccount());
            if(!user.getEmail().equals(redisService.getValue(user.getEmail(),String.class)))
            {
                throw new BadRequestException("Expired time to create new password!!!");
            }

            user.setPassword(new BCryptPasswordEncoder().encode(passwordNew.getPassword()));
            userRepo.save(user);
            removeCache(user.getEmail());
            return _res.createSuccessResponse("Success", 200);

        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(), 500);
        }
    }

    public ApiResponse<?> changePassword(PasswordChange passwordChange)
    {
        try {
            String email =passwordChange.getEmail();
            String newPassword = passwordChange.getNewPassword();
            String oldPassword = passwordChange.getOldPassword();
            String confirmPassword = passwordChange.getConfirmPassword();
            User user = userRepo.findTopByEmail(email);
            if(!passwordEncoder.matches(oldPassword, user.getPassword()))
            {
                throw new BadRequestException("Old password not matches!!");
            }

            if(!newPassword.equals(confirmPassword))
            {
                throw new BadRequestException("Password confirm not match new password!!");
            }

            user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            userRepo.save(user);
            return _res.createSuccessResponse("Success", 200);

        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(), 500);
        }
    }

    private void removeCache(String email)
    {
        redisService.removeValue("otp"+email);
        redisService.removeValue(email);
    }

    public ApiResponse<?> refresh(RefreshToken refreshToken) {
        String email = jwtService.extractEmail(refreshToken.getRefreshToken());
        User user = userRepo.findTopByEmail(email);
        if (jwtService.isValidToken(refreshToken.getRefreshToken(), user)) {
            String accessToken = jwtService.generateToken(user);
            String refreshTokenn = jwtService.generateRefreshToken(user);
            return _res.createSuccessResponse("Refresh successfully", HttpStatus.CREATED.value(), toJwtResponse(accessToken, refreshTokenn,null));
        }
        return _res.createErrorResponse("Refresh failure", HttpStatus.BAD_REQUEST.value());
    }

    @Override
    public ApiResponse<?> getUser(String code) {
        User user = userRepo.findUserByEmailOrId(code);
        if(user != null)
        {
            return _res.createSuccessResponse("Success", 200,UserMapper.INSTANCE.toUserResponse(user));
        }
        return _res.createSuccessResponse("Success", 200);
    }

    public ApiResponse<?> getUserAll(User user,String code){
        try{
            List<User> users = userRepo.findAllContain(user,code);
            return _res.createSuccessResponse("Success", 200,users.stream().map(UserMapper.INSTANCE::toUserResponse).toList());
        }catch (Exception e){
            return _res.createErrorResponse(e.getMessage(), 500);
        }
    }
}
