package com.eliasdetlefsen.portfolio_backend.auth;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.eliasdetlefsen.portfolio_backend.user.User;

@Service
public class JwtService {
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtService(@Value("${app.jwt.secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).build();
    }

    public String generateToken(User user) {
        Instant now = Instant.now();

        return JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("role", user.getRole().name())
                .withIssuedAt(now)
                .withExpiresAt(now.plusSeconds(60 * 60))
                .sign(algorithm);
    }

    public String getUserIdFromToken(String token) {
        return verifier.verify(token)
                .getSubject();
    }

    public String getUserRoleFromToken(String token) {
        return verifier.verify(token)
                .getClaim("role")
                .asString();
    }
}
