package com.example.MoneyLover.config.Jwt;

import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.User.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private HandlerExceptionResolver exceptionResolver;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UserService userService;

    @Autowired
    public JwtAuthenticationFilter(HandlerExceptionResolver exceptionResolver) {
        this.exceptionResolver = exceptionResolver;
    }
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            }
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractEmail(jwt);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                request.setAttribute("userEmail", userEmail);
                User user = userService.loadUserByPhone(userEmail);
                if (jwtService.isValidToken(jwt, user)) {

                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                            user, null, Collections.emptyList()
                    );

                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            }
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, e);
        }
    }
}
