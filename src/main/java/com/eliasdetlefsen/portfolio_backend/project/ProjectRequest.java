package com.eliasdetlefsen.portfolio_backend.project;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(
        @NotBlank Integer displayOrder,
        @NotBlank String markdown,
        @NotBlank UUID imageUuid) {
}
