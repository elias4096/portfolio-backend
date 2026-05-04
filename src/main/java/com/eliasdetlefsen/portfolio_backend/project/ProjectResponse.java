package com.eliasdetlefsen.portfolio_backend.project;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record ProjectResponse(
        @NotBlank UUID id,
        @NotBlank Integer displayOrder,
        @NotBlank String markdown,
        @NotBlank UUID imageUuid) {

    public static ProjectResponse from(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getDisplayOrder(),
                project.getMarkdown(),
                project.getImageUuid());
    }
}
