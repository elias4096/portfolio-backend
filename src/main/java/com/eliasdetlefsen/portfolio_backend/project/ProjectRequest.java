package com.eliasdetlefsen.portfolio_backend.project;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(
                @NotBlank String markdown,
                @NotBlank UUID imageUuid) {
}
