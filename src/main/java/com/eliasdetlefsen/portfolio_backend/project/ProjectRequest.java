package com.eliasdetlefsen.portfolio_backend.project;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(
        @NotBlank String title,
        @NotBlank Integer displayOrder) {
}
