package com.eliasdetlefsen.portfolio_backend.auth;

public class InvalidCredentialsExceptions extends RuntimeException {
    public InvalidCredentialsExceptions(String message) {
        super(message);
    }
}
