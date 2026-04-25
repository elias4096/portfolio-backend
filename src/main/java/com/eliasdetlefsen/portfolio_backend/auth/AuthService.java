package com.eliasdetlefsen.portfolio_backend.auth;

import java.util.UUID;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eliasdetlefsen.portfolio_backend.exception.InvalidCredentialsExceptions;
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

    public ResponseCookie login(LoginRequest request) {
        var user = userService.getUserByEmail(request.email());

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsExceptions("Invalid password");
        }

        String token = jwtService.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("access_token", token)
                // Prevent JavaScript access.
                .httpOnly(true)
                // Todo: change to true here.
                .secure(false)
                // Prevent cross-site requests sending cookie.
                .sameSite("Strict")
                // Send cookie to all endpoints.
                .path("/")
                // Token expiration.
                .maxAge(60 * 60)
                .build();

        return cookie;
    }
}
