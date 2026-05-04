package com.eliasdetlefsen.portfolio_backend.exception;

public class InvalidCredentialsExceptions extends RuntimeException {
    public InvalidCredentialsExceptions() {
        super("Invalid credentials");
    }
}
