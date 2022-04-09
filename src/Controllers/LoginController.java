package Controllers;

import Implementations.JDBC;
import Models.Users.User;
import Views.LoginDialog;
import Views.Menu;
import Views.UserDialog;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class LoginController {
    private User userS;

    public User getUserS() {
        return userS;
    }

    public void setUserS(User userS) {
        this.userS = userS;
    }

    public void call() {
        var menu = new Menu("Login Menu", List.of(
                new Menu.Option("Login", () -> {
                    User user = new LoginDialog().input();
                    SetUserSession(user);
                    return String.format("You are now successfully logged in!");
                }),
                new Menu.Option("Register", () -> {
                    User user = new UserDialog().input();
                    User created = JDBC.addUserToDB(user);
                    return String.format("'%s %s' successfully registered!",
                            created.getFirstName(),created.getLastName());
                })
        ));
        menu.show();
    }

    public void SetUserSession(User user) {
       userS= user;
    }
}
