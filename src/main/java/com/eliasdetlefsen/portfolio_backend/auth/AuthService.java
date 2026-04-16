package com.eliasdetlefsen.portfolio_backend.auth;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eliasdetlefsen.portfolio_backend.user.UserMeResponse;
import com.eliasdetlefsen.portfolio_backend.user.UserService;

@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserMeResponse me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = userService.getUserById(UUID.fromString(authentication.getName()));
        return new UserMeResponse(user.getEmail(), user.getRole());
    }

    public LoginResponse login(LoginRequest request) {
        var user = userService.getUserByEmail(request.email());

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsExceptions("Invalid password");
        }

        String token = jwtService.generateToken(user);
        return new LoginResponse(token);
    }
}
