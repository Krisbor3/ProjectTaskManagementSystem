package Contracts;

import Exceptions.NonexistingEntityException;
import Models.Users.User;

public interface EntityDialog<E>{
    E input();
}
