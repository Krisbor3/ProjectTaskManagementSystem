package Implementations;

import Exceptions.NonexistingEntityException;
import Infrastructure.ResultRepository;
import Models.Result;
import Models.Task;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ResultRepositoryImp implements ResultRepository {
    private static long nextId = 0;
    private Map<Long, Result> results = new HashMap<>();
    @Override
    public Collection<Result> findAll() {
        return results.values();
    }

    @Override
    public Result findById(Long id) {
        return results.get(id);
    }

    @Override
    public Result create(Result entity) {
        entity.setId(++nextId);
        results.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Result update(Result entity) throws NonexistingEntityException {
        Result old = findById(entity.getId());
        if(old == null) {
            throw new NonexistingEntityException("Result with ID='" + entity.getId() + "' does not exist.");
        }
        results.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Result deleteById(Long id) throws NonexistingEntityException {
        Result old = results.remove(id);
        if(old == null) {
            throw new NonexistingEntityException("Result with ID='" + id + "' does not exist.");
        }
        return old;
    }

    @Override
    public long count() {
        return results.size();
    }
}
