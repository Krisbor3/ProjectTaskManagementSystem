package Services;

import Contracts.IProjectService;
import Exceptions.ConstraintViolationException;
import Exceptions.InvalidEntityDataException;
import Exceptions.NonexistingEntityException;
import Infrastructure.ProjectRepository;
import Models.Project;
import Util.ProjectValidator;

import java.util.Collection;

public class ProjectService implements IProjectService {
    private final ProjectRepository repo;
    private final ProjectValidator projectValidator;

    public ProjectService(ProjectRepository repo, ProjectValidator projectValidator) {
        this.repo = repo;
        this.projectValidator = projectValidator;
    }

    @Override
    public Collection<Project> getAll() {
        return repo.findAll();
    }

    @Override
    public Project getById(Long id) throws NonexistingEntityException {
        Project project = repo.findById(id);
        if (project==null){
            throw new NonexistingEntityException("Project with ID='" + id + "' does not exist.");
        }
        return project;
    }

    @Override
    public Project add(Project project) throws InvalidEntityDataException {
        try {
            projectValidator.validate(project);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(
                    String.format("Error creating project '%s'", project.getTitle()),
                    ex
            );
        }
        return repo.create(project);
    }

    @Override
    public Project update(Project project) throws NonexistingEntityException, InvalidEntityDataException {
        try {
            projectValidator.validate(project);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(
                    String.format("Error creating project '%s'", project.getTitle()),
                    ex
            );
        }
        return repo.update(project);
    }

    @Override
    public Project deleteById(Long id) throws NonexistingEntityException {
        return repo.deleteById(id);
    }

    @Override
    public long count() {
        return repo.count();
    }
}
