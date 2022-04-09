package Implementations;

import Exceptions.NonexistingEntityException;
import Infrastructure.UserRepository;
import Models.Users.User;

import java.util.*;

public class UserRepositoryImp implements UserRepository {
    private static long nextId = 0;
    private Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User findById(Long id) {
        return users.get(id);
    }

    @Override
    public User create(User user) {
        user.setId(++nextId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) throws NonexistingEntityException {
        User old = findById(user.getId());
        if(old == null) {
            throw new NonexistingEntityException("User with ID='" + user.getId() + "' does not exist.");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User deleteById(Long id) throws NonexistingEntityException {
        User old = users.remove(id);
        if(old == null) {
            throw new NonexistingEntityException("User with ID='" + id + "' does not exist.");
        }
        return old;
    }

    @Override
    public long count() {
        return users.size();
    }
}
