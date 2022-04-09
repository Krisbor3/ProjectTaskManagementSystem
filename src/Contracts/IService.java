package Contracts;

import Exceptions.InvalidEntityDataException;
import Exceptions.NonexistingEntityException;
import Models.Users.User;

import java.util.Collection;

public interface IService<T> {
    Collection<T> getAll();
    T getById(Long id) throws NonexistingEntityException;
    T add(T entity) throws InvalidEntityDataException;
    T update(T entity) throws NonexistingEntityException, InvalidEntityDataException;
    T deleteById(Long id) throws NonexistingEntityException;
    long count();
}
