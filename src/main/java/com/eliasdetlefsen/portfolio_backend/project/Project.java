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

    @Column(name = "slug", nullable = false, length = 255)
    private String slug;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

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

    public Project(String title, Integer displayOrder) {
        this.id = UUID.randomUUID();
        this.slug = "";
        this.title = title;
        this.displayOrder = displayOrder;
    }

    public void update(String title, Integer displayOrder) {
        this.title = title;
        this.displayOrder = displayOrder;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }
}
