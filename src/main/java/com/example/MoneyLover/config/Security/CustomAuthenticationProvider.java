package com.example.MoneyLover.config.Security;

import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.User.Repository.UserRepository;
import lombok.SneakyThrows;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder valiPasswordEncoder;



    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String email = authentication.getName();
        final String password = authentication.getCredentials().toString();
            User user =userRepo.findTopByEmail(email);
            if(user==null || !valiPasswordEncoder.matches(password,user.getPassword()))
            {
                throw new BadRequestException("Invalid email or password");
            }
            return authenticateAgainstThirdPartyAndGetAuthentication(email, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {

        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private static UsernamePasswordAuthenticationToken authenticateAgainstThirdPartyAndGetAuthentication(String name, String password) {
        final List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));

        // Tạo một đối tượng User mới với thông tin tên đăng nhập và mật khẩu
        User user = new User();
        user.setUsername(name);
        user.setPassword(password);

        // Sử dụng đối tượng User để tạo đối tượng Principal
        UserDetails principal = new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), grantedAuths);
        return new UsernamePasswordAuthenticationToken(principal, password, grantedAuths);
    }
}
