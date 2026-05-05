package com.eliasdetlefsen.portfolio_backend.auth;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eliasdetlefsen.portfolio_backend.exception.InvalidCredentialsExceptions;
import com.eliasdetlefsen.portfolio_backend.user.User;
import com.eliasdetlefsen.portfolio_backend.user.UserMeResponse;
import com.eliasdetlefsen.portfolio_backend.user.UserService;

@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${spring.profiles.active:default}")
    private String configuration;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserMeResponse me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserById(UUID.fromString(authentication.getName()));
        return new UserMeResponse(user.getEmail(), user.getRole());
    }

    public ResponseCookie login(LoginRequest request) {
        User user = userService.getUserByEmail(request.email());

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsExceptions();
        }

        String token = jwtService.generateToken(user);

        return ResponseCookie.from("access_token", token)
                .secure(configuration.equals("prod") ? true : false)
                .httpOnly(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(60 * 60)
                .build();
    }

    public ResponseCookie logout() {
        SecurityContextHolder.clearContext();

        return ResponseCookie.from("access_token", "")
                .secure(configuration.equals("prod") ? true : false)
                .httpOnly(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();
    }
}
