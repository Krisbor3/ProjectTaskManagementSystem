package Controllers;

import Contracts.ITaskService;
import Contracts.IUserService;
import Exceptions.NonexistingEntityException;
import Implementations.JDBC;
import Models.Task;
import Models.Users.Senior;
import Models.Users.Student;
import Models.Users.User;
import Views.Menu;
import Views.TaskDialog;
import Views.UserDialog;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class TaskController {
    private ITaskService taskService;
    private IUserService userService;
    private Senior senior;

    public TaskController(ITaskService taskService, IUserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }
    public TaskController(ITaskService taskService, IUserService userService, Senior senior) {
        this.taskService = taskService;
        this.userService = userService;
        this.senior=senior;
    }

    public void call() {
        var menu = new Menu("Senior/Admin Menu", List.of(
//                new Menu.Option("Add new Task in Memory", () -> {
//                    Task task = new TaskDialog().input();
//                    Task created = taskService.add(task);
//                    return String.format("Task ID:%s: '%s' added successfully.",
//                            created.getId(), created.getName());
//                }),
                new Menu.Option("Add new Task", () -> {//done
                    Task task = new TaskDialog().input();
                    Task created = JDBC.addTaskToDB(task);
                    return String.format("Task ID:%s: '%s' added successfully.",
                            created.getId(), created.getName());
                }),
                new Menu.Option("Set User's role in DB", ()->{//done
                    User user = new UserDialog().changeRoleInDB(userService);
                    return String.format("User: %s with ID:%s: set Role to '%s' successfully in the DB.",
                            user.getFirstName(),user.getId(),user.getRole());
                }),
//                new Menu.Option("Assign User to a Task in Memory", () -> {
//                    Task task = new TaskDialog().giveTaskToUser(taskService, userService);
//                    Task created = null;
//                    User createdUser = null;
//                    try {
//                        created = taskService.update(task);
//                        createdUser = userService.getById(created.getUserId());
//                    } catch (NonexistingEntityException e) {
//                        e.printStackTrace();
//                    }
//                    return String.format("Task: %s with ID:%s: assigned to '%s' with id '%s' successfully.",
//                            created.getName(), created.getId(), createdUser.getFirstName(), created.getUserId());
//                }),
                new Menu.Option("Assign User to a Task", () -> {//done
                    Task task = new TaskDialog().assignUserToTask();
                    User createdUser=null;
                    try {
                        createdUser = JDBC.getUserByIdFromDB(task.getUserId());
                    } catch (SQLException e) {
                        System.out.println("User not found with this id");
                    }
                    return String.format("Task: %s with ID:%s: assigned to '%s' with id '%s' successfully.",
                            task.getName(), task.getId(), createdUser.getFirstName(), task.getUserId());
                }),
                new Menu.Option("Check Task", () -> {//done
                    Task task = new TaskDialog().checkTask(senior.getId());
                    return String.format("Successfully accessed: %s\nContent: %s", task,task.getTaskContent());
                }),
                new Menu.Option("Mark Task as done", () -> {//done
                    Task task = new TaskDialog().markTask(senior.getId());
                    return String.format("Marked task: %s", task);
                }),
                new Menu.Option("Decline Task, not done well", () -> {//done
                    Task task = new TaskDialog().declineTask(senior.getId());
                    return String.format("Declined task: %s", task);
                }),
//                new Menu.Option("Delete Task", () -> {
//                    Task task = new TaskDialog().inputDelete(taskService);
//                    Task created = null;
//                    try {
//                        created = taskService.deleteById(task.getId());
//                    } catch (NonexistingEntityException e) {
//                        e.printStackTrace();
//                    }
//                    return String.format("Task: %s with ID:%s: successfully deleted.",
//                            created.getName(), created.getId());
//                }),
                new Menu.Option("Delete Task", () -> {//done
                    Task task = new TaskDialog().deleteTask();
                    return String.format("Task: %s with ID:%s: successfully deleted.",
                            task.getName(), task.getId());
                }),
//                new Menu.Option("Print All Tasks", () -> {
//                    System.out.println("Loading Tasks ...\n");
//                    Collection<Task> tasks = taskService.getAll();
//                    tasks.forEach(System.out::println);
//                    return "Total tasks count: " + tasks.size();
//                }),
                new Menu.Option("Print All Tasks", () -> {//done
                    System.out.println("Loading Tasks ...\n");
                    Collection<Task> tasks = null;
                    try {
                        tasks = JDBC.getAllTasks();
                    } catch (SQLException e) {
                        System.out.println("Couldn't load Tasks");
                    }
                    tasks.forEach(System.out::println);
                    return "Total tasks count: " + tasks.size();
                }),
//                new Menu.Option("Save All Tasks to a file (IO)", () -> {
//                    System.out.println("Saving Tasks ...\n");
//                    taskService.saveAllTasks();
//
//                    return "Tasks saved successfully!";
//                })
                new Menu.Option("Save All Completed Tasks to a file (IO)", () -> {//done
                    System.out.println("Saving Tasks ...\n");
                    taskService.saveAllCompletedTasks();
                    return "Tasks saved successfully!";
                })
        ));
        menu.show();
    }
}
