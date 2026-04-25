package com.eliasdetlefsen.portfolio_backend.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eliasdetlefsen.portfolio_backend.user.UserMeResponse;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = { "http://localhost:5173", "https://bryrmiginte.se" })
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    public UserMeResponse me() {
        return authService.me();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        ResponseCookie responseCookie = authService.login(request);
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        return ResponseEntity.ok().build();
    }
}
