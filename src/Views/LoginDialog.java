package Views;

import Contracts.EntityDialog;
import Implementations.JDBC;
import Models.Users.User;

import java.sql.SQLException;
import java.util.Scanner;

public class LoginDialog implements EntityDialog<User> {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public User input() {
        User user =new User();
        while (user.getUsername() == null) {
            System.out.println("Enter Username:");
            String ans = sc.nextLine();

            try {
                user.setUsername(JDBC.getUserByUsername(ans));
                if (user.getUsername()==null){
                    System.out.println("User with this username does not exist!");
                    continue;
                }
                user.setUsername(ans);
            } catch (SQLException e) {
                System.out.println("User with this username does not exist!");
            }
        }
        while (user.getPassword() == null) {
            System.out.println("Enter Password:");
            String ans = sc.nextLine();

            try {
                user =JDBC.getPassword(user.getUsername(),ans);
                if (user.getPassword()==null){
                    System.out.println("Password incorrect!");
                    continue;
                }
                user.setPassword(ans);
            } catch (SQLException e) {
                System.out.println("Password incorrect!");
            }
        }

        return user;
    }
}
