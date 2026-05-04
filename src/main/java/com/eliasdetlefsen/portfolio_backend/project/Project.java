package com.eliasdetlefsen.portfolio_backend.project;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "markdown", nullable = false, length = 500)
    private String markdown;

    @Column(name = "image_uuid", nullable = false)
    private UUID imageUuid;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    protected Project() {
    }

    public Project(Integer displayOrder, String markdown, UUID imageUuid) {
        this.id = UUID.randomUUID();
        this.displayOrder = displayOrder;
        this.markdown = markdown;
        this.imageUuid = imageUuid;
    }

    public void update(Integer displayOrder, String markdown, UUID imageUuid) {
        this.displayOrder = displayOrder;
        this.markdown = markdown;
        this.imageUuid = imageUuid;
    }

    public UUID getId() {
        return id;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public String getMarkdown() {
        return markdown;
    }

    public UUID getImageUuid() {
        return imageUuid;
    }
}
