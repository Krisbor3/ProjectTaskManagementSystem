package Services;

import Contracts.IResultService;
import Exceptions.ConstraintViolationException;
import Exceptions.InvalidEntityDataException;
import Exceptions.NonexistingEntityException;
import Implementations.JDBC;
import Infrastructure.ResultRepository;
import Models.Result;
import Util.ResultValidator;

import java.util.Collection;

public class ResultService implements IResultService {
   private final ResultRepository repo;
    private final ResultValidator validator;

    public ResultService(ResultRepository repo, ResultValidator validator) {
        this.repo = repo;
        this.validator = validator;
    }

    @Override
    public Collection<Result> getAll() {
        return repo.findAll();
    }

    @Override
    public Result getById(Long id) throws NonexistingEntityException {
        Result result = repo.findById(id);
        if (result==null){
            throw new NonexistingEntityException("Result with ID='" + id + "' does not exist.");
        }
        return result;
    }

    @Override
    public Result add(Result entity) throws InvalidEntityDataException {
        try {
            validator.validate(entity);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(
                    String.format("Error creating result "),
                    ex
            );
        }
        return repo.create(entity);
    }

    @Override
    public Result update(Result entity) throws NonexistingEntityException, InvalidEntityDataException {
        try {
            validator.validate(entity);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(
                    String.format("Error updating result"),
                    ex
            );
        }
        return repo.update(entity);
    }

    @Override
    public Result deleteById(Long id) throws NonexistingEntityException {
        return repo.deleteById(id);
    }

    @Override
    public long count() {
        return repo.count();
    }
}
