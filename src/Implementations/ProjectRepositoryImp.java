package Implementations;

import Exceptions.NonexistingEntityException;
import Infrastructure.ProjectRepository;
import Models.Project;
import Models.Users.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProjectRepositoryImp implements ProjectRepository {
    private static long nextId = 0;
    private Map<Long, Project> projects = new HashMap<>();

    @Override
    public Collection<Project> findAll() {
        return projects.values();
    }

    @Override
    public Project findById(Long id) {
        return projects.get(id);
    }

    @Override
    public Project create(Project entity) {
        entity.setId(++nextId);
        projects.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Project update(Project entity) throws NonexistingEntityException {
        Project old = findById(entity.getId());
        if(old == null) {
            throw new NonexistingEntityException("Project with ID='" + entity.getId() + "' does not exist.");
        }
        projects.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Project deleteById(Long id) throws NonexistingEntityException {
        Project old = projects.remove(id);
        if(old == null) {
            throw new NonexistingEntityException("Project with ID='" + id + "' does not exist.");
        }
        return old;
    }

    @Override
    public long count() {
        return projects.size();
    }
}
