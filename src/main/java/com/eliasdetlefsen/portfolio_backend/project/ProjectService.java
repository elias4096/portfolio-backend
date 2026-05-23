package com.eliasdetlefsen.portfolio_backend.project;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new ProjectNotFoundException());

        return ProjectResponse.from(project);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProjectResponse create(ProjectRequest request) {
        Project project = new Project(
                request.markdown(),
                request.imageUuid());

        return ProjectResponse.from(projectRepository.save(project));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void reorder(List<ReorderRequest> request) {
        for (ReorderRequest r : request) {
            Project project = projectRepository.findById(r.id())
                    .orElseThrow(() -> new ProjectNotFoundException());

            project.setDisplayOrder(r.displayOrder());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProjectResponse update(UUID id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException());

        project.setMarkdown(request.markdown());
        project.setImageUuid(request.imageUuid());

        return ProjectResponse.from(projectRepository.save(project));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(UUID id) {
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotFoundException();
        }

        projectRepository.deleteById(id);
    }
}
