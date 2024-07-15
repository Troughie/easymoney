package com.example.MoneyLover.config.Jwt;

import com.example.MoneyLover.infra.User.Entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public interface JwtService {
    String generateToken(User user);

    String generateRefreshToken(User user);

    String extractEmail(String token);



    boolean isValidToken(String token, User user);
}
