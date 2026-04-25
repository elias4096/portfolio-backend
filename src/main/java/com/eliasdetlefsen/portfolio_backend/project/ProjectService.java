package com.eliasdetlefsen.portfolio_backend.project;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.eliasdetlefsen.portfolio_backend.exception.ProjectNotFoundException;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<ProjectResponse> getAll() {
        return projectRepository.findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(ProjectResponse::from)
                .toList();
    }

    public ProjectResponse getById(UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Failed to get, no project with id: " + id));

        return ProjectResponse.from(project);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProjectResponse create(ProjectRequest request) {
        Project project = new Project(
                request.displayOrder(),
                request.markdown());

        return ProjectResponse.from(projectRepository.save(project));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProjectResponse update(UUID id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Failed to update, no project with id: " + id));

        project.update(request.displayOrder(), request.markdown());

        return ProjectResponse.from(projectRepository.save(project));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(UUID id) {
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotFoundException("Failed to delete, no project with id: " + id);
        }

        projectRepository.deleteById(id);
    }
}
