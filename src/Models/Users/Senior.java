package Models.Users;

import Enums.ROLE;

public class Senior extends User{
    public Senior() {
    }

    public Senior(String firstName, String lastName, String email, String username, String password) {
        super(firstName, lastName, email, username, password, ROLE.SENIOR);
    }
}
