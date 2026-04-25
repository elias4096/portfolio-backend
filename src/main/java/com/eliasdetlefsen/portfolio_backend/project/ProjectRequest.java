package com.eliasdetlefsen.portfolio_backend.project;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(
                @NotBlank Integer displayOrder,
                @NotBlank String markdown) {
}
