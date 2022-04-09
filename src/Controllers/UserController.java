package Controllers;

import Contracts.IUserService;
import Exceptions.NonexistingEntityException;
import Implementations.JDBC;
import Models.Users.User;
import Views.Menu;
import Views.UserDialog;

import java.util.List;

public class UserController {
    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    public  void call(){
        var menu = new Menu("Manage Users Menu", List.of(
//                new Menu.Option("Add new User in Memory", ()->{
//                User user = new UserDialog().input();
//                User created = userService.add(user);
//                return String.format("User ID:%s: '%s %s' added successfully.",
//                    created.getId(), created.getFirstName(),created.getLastName());
//                }),
                new Menu.Option("Add new User to DB", ()->{
                    User user = new UserDialog().input();
                    User created = JDBC.addUserToDB(user);
                    return String.format("User ID:%s: '%s %s' added successfully to DB.",
                            created.getId(), created.getFirstName(),created.getLastName());
                }),
//                new Menu.Option("Set User's role in Memory", ()->{
//                    User user = new UserDialog().inputRole(userService);
//                    User created = null;
//                    try {
//                        created = userService.update(user);
//                    } catch (NonexistingEntityException e) {
//                        e.printStackTrace();
//                    }
//                    return String.format("User: %s with ID:%s: set Role to '%s' successfully.",
//                            created.getFirstName(),created.getId(),created.getRole());
//                }),
                new Menu.Option("Set User's role in DB", ()->{
                    User user = new UserDialog().changeRoleInDB(userService);
                    return String.format("User: %s with ID:%s: set Role to '%s' successfully in the DB.",
                            user.getFirstName(),user.getId(),user.getRole());
                }),
//                new Menu.Option("Delete User in Memory", ()->{
//                    User user = new UserDialog().inputDelete(userService);
//                    User created = null;
//                    try {
//                        created = userService.deleteById(user.getId());
//                    } catch (NonexistingEntityException e) {
//                        e.printStackTrace();
//                    }
//                    return String.format("User: %s with ID:%s: successfully deleted.",
//                            created.getFirstName(),created.getId());
//                }),
                new Menu.Option("Delete User in DB", ()->{
                    User user = new UserDialog().deleteUserInDB(userService);
                    User created  = userService.deleteByIdInDB(user);
                    return String.format("User: %s with ID:%s: successfully deleted from Data Base.",
                            created.getFirstName(),created.getId());
                }),
//                new Menu.Option("Print All Users in Memory",() ->{
//                    System.out.println("Loading Users ...\n");
//                    var users = userService.getAll();
//                    users.forEach(System.out::println);
//                    return "Total users count: " + users.size();
//                }),
                new Menu.Option("Print All Users from DB",() ->{
                    System.out.println("Loading Users from DB ...\n");
                    userService.getAllFromDB();
                    return "Successfully printed all users from DB";
                })
        ));
        menu.show();
    }
}
