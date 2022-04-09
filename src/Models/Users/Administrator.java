package Models.Users;

import Enums.ROLE;

public class Administrator extends User {
    public Administrator(String firstName, String lastName, String email, String username, String password) {
        super(firstName, lastName, email, username, password, ROLE.ADMINISTRATOR);
    }
}
