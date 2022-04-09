package Contracts;

import Exceptions.InvalidEntityDataException;
import Exceptions.NonexistingEntityException;
import Models.Users.User;

import java.util.Collection;

public interface IUserService extends IService<User>{
    public void getAllFromDB();
    public User updateUserInDB(User user);
    public User deleteByIdInDB(User user);
}
