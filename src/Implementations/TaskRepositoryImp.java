package Implementations;

import Exceptions.NonexistingEntityException;
import Infrastructure.TaskRepository;
import Models.Task;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TaskRepositoryImp implements TaskRepository {
    private static long nextId = 0;
    private Map<Long, Task> tasks = new HashMap<>();

    @Override
    public Collection<Task> findAll() {
        return tasks.values();
    }

    @Override
    public Task findById(Long id) {
        return tasks.get(id);
    }

    @Override
    public Task create(Task task) {
        task.setId(++nextId);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task update(Task task) throws NonexistingEntityException {
        Task old = findById(task.getId());
        if(old == null) {
            throw new NonexistingEntityException("Task with ID='" + task.getId() + "' does not exist.");
        }
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task deleteById(Long id) throws NonexistingEntityException {
        Task old = tasks.remove(id);
        if(old == null) {
            throw new NonexistingEntityException("Task with ID='" + id + "' does not exist.");
        }
        return old;
    }

    @Override
    public long count() {
        return tasks.size();
    }
}
