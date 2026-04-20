package com.eliasdetlefsen.portfolio_backend.project;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record ProjectResponse(
        @NotBlank UUID id,
        @NotBlank String title,
        @NotBlank Integer displayOrder) {
    public static ProjectResponse from(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getDisplayOrder());
    }
}
