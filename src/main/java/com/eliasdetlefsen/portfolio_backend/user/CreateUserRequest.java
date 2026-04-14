package com.eliasdetlefsen.portfolio_backend.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
                @NotBlank @Email String email,
                @NotBlank String password,
                @NotBlank UserRole role) {
}
