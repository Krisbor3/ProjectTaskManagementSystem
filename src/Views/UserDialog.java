package Views;

import Contracts.EntityDialog;
import Contracts.IUserService;
import Enums.ROLE;
import Exceptions.NonexistingEntityException;
import Implementations.JDBC;
import Models.Users.User;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserDialog implements EntityDialog<User> {
    public static Scanner sc = new Scanner(System.in);
    private String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$";
    private String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    @Override
    public User input() {
        User user =new User();
        while (user.getFirstName() == null) {
            System.out.println("Enter First Name:");
            String ans = sc.nextLine();
            if (ans.length() < 2 || ans.length()>15) {
                System.out.println("Error: User's First Name should be in the range 2 and 15 characters long.");
            } else {
                user.setFirstName(ans);
            }
        }
        while (user.getLastName() == null) {
            System.out.println("Enter Last Name:");
            String ans = sc.nextLine();
            if (ans.length() < 2 || ans.length()>15) {
                System.out.println("Error: User's Last Name should be in the range 2 and 15 characters long.");
            } else {
                user.setLastName(ans);
            }
        }
        while (user.getEmail() == null) {
            System.out.println("Enter email:");
            String ans = sc.nextLine();
            Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(ans);
            boolean matchFound = matcher.find();
            if(!matchFound){
                System.out.println("Error: Email is not valid. Try again!");
            } else {
                user.setEmail(ans);
            }
        }
        while (user.getUsername() == null) {
            System.out.println("Username:");
            String ans = sc.nextLine();
            if (ans.length() < 2 || ans.length()>15) {
                System.out.println("Error: User's Username should be in the range 2 and 15 characters long.");
            } else {
                user.setUsername(ans);
            }
        }
        //password validation
        while (user.getPassword() == null) {
            System.out.println("Enter Password:");
            String ans = sc.nextLine();
            Pattern pattern = Pattern.compile(passwordRegex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(ans);
            boolean matchFound = matcher.find();
            if(!matchFound){
                System.out.println("Error: Password must be string 8 to 15 characters long, at least one digit, one capital letter, and one sign different than letter or digit");
            } else {
                user.setPassword(ans);
            }
        }
        return user;
    }

    public User inputRole(IUserService userService) {
        User user = null;
        while (user==null) {
            System.out.println("Enter User's id for update:");
            Long id = sc.nextLong();

            try {
                user = userService.getById(id);
                System.out.printf("You got successfully %s\n",user.getFirstName());
            } catch (NonexistingEntityException e) {
                System.out.println("User with this id does not exist!");
            }
        }
        String sts = sc.nextLine();
        while (user.getRole() == null) {
            System.out.println("Assign User's Role:");
            System.out.printf("Options are: ");
            Arrays.stream(ROLE.values()).forEach(result-> System.out.printf(" %s",result));

            String ans = sc.nextLine();

            //check if it matches any of the roles we have
            try {
                ROLE role = ROLE.valueOf(ans);
                if (!Arrays.stream(ROLE.values()).anyMatch(r->r==role)) {
                    System.out.println("Error: Invalid ROLE!");
                } else {

                    user.setRole(role);
                }
            }catch (IllegalArgumentException ex){
                System.out.println("Invalid ROLE!");
            }
        }
        return user;
    }

    public User inputDelete(IUserService userService) {
        User user = null;
        while (user==null) {
            System.out.println("Enter User's id for delete:");
            Long id = sc.nextLong();

            try {
                user = userService.getById(id);
                System.out.printf("You got successfully %s\n",user.getFirstName());
            } catch (NonexistingEntityException e) {
                System.out.println("User with this id does not exist!");
            }
        }
        return user;
    }

    public User deleteUserInDB(IUserService userService) {
        User user = null;
        while (user==null) {
            System.out.println("Enter User's id for delete:");
            Long id = sc.nextLong();

            try {
                user = JDBC.getUserByIdFromDB(id);
                System.out.printf("You got successfully %s\n",user.getFirstName());
            }catch (SQLException e) {
                System.out.println("User with this id does not exist!");
            }
        }
        return user;
    }

    public  User changeRoleInDB(IUserService userService){
        User user = null;
        while (user==null) {
            System.out.println("Enter User's id for update:");
            Long id = sc.nextLong();

            try {
                user = JDBC.getUserByIdFromDB(id);
                System.out.printf("You got successfully %s\n", user.getFirstName());
            }catch (SQLException e) {
                System.out.println("User with this id does not exist in the DB!");
            }
        }
        String sts = sc.nextLine();
        while (user.getRole() == ROLE.NOT_SET) {
            System.out.println("Assign User's Role:");
            System.out.printf("Options are: ");
            Arrays.stream(ROLE.values()).forEach(result-> System.out.printf(" %s",result));

            String ans = sc.nextLine();

            //check if it matches any of the roles we have
            try {
                ROLE role = ROLE.valueOf(ans);
                if (!Arrays.stream(ROLE.values()).anyMatch(r->r==role)) {
                    System.out.println("Error: Invalid ROLE!");
                } else {
                    user.setRole(role);
                    userService.updateUserInDB(user);
                }
            }catch (IllegalArgumentException ex){
                System.out.println("Invalid ROLE!");
            }
        }
        return user;
    }
}
