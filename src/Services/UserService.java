package Services;

import Contracts.IUserService;
import Exceptions.ConstraintViolationException;
import Exceptions.InvalidEntityDataException;
import Exceptions.NonexistingEntityException;
import Implementations.JDBC;
import Infrastructure.UserRepository;
import Models.Users.User;
import Util.UserValidator;

import java.sql.SQLException;
import java.util.Collection;

public class UserService implements IUserService {

    private final UserRepository repo;
    private final UserValidator userValidator;

    public UserService(UserRepository repo, UserValidator userValidator) {
        this.repo = repo;
        this.userValidator = userValidator;
    }

    @Override
    public Collection<User> getAll() {
        return repo.findAll();
    }

    @Override
    public User getById(Long id) throws NonexistingEntityException {
        var user = repo.findById(id);
        if (user == null) {
            throw new NonexistingEntityException("User with ID='" + id + "' does not exist.");
        }
        return user;
    }

    @Override
    public User add(User user) throws InvalidEntityDataException {
        try {
            userValidator.validate(user);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(
                    String.format("Error creating user '%s %s'", user.getFirstName(), user.getLastName()),
                    ex
            );
        }
        return repo.create(user);
    }

    @Override
    public User update(User user) throws NonexistingEntityException, InvalidEntityDataException {
        try {
            userValidator.validate(user);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(
                    String.format("Error creating user '%s %s'", user.getFirstName(), user.getLastName()),
                    ex
            );
        }
        return repo.update(user);
    }

    @Override
    public User deleteById(Long id) throws NonexistingEntityException {
        return repo.deleteById(id);
    }

    @Override
    public long count() {
        return repo.count();
    }

    public void getAllFromDB() {
        try {
            JDBC.getAllUsers();
        } catch (SQLException e) {
            String.format("Error getting users from DB!");
            e.printStackTrace();
        }
    }

    public User updateUserInDB(User user){
       JDBC.updateUserToDB(user);
       return user;
    }
    public User deleteByIdInDB(User user){
        JDBC.deleteUserFromDB(user);
        return user;
    }
}
