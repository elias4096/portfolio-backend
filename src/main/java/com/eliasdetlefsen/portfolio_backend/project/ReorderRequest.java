package com.eliasdetlefsen.portfolio_backend.project;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record ReorderRequest(
        @NotBlank UUID id,
        @NotBlank Integer displayOrder) {
}
