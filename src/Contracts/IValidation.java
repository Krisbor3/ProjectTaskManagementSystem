package Contracts;

import Exceptions.ConstraintViolationException;

public interface IValidation<T> {
    public void validate(T entity) throws ConstraintViolationException;
}
