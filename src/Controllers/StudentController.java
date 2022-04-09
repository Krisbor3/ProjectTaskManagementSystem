package Controllers;

import Contracts.ITaskService;
import Implementations.JDBC;
import Models.Task;
import Models.Users.Student;
import Views.Menu;
import Views.TaskDialog;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class StudentController {
    private ITaskService taskService;
    private Student student;

    public StudentController(ITaskService taskService) {
        this.taskService = taskService;
    }

    public StudentController(ITaskService taskService, Student student) {
        this.taskService = taskService;
        this.student = student;
    }

    public StudentController(Student student) {
        this.student = student;
    }

    public void call() {
        var menu = new Menu("Student Menu", List.of(
//                new Menu.Option("Get a Task from Memory", () -> {
//                    Task task = new TaskDialog().getTaskForWork(taskService, student);
//                    student.getTasks().add(task);
//                    return String.format("You are now assigned to '%s' task", task.getName());
//                }),
                new Menu.Option("Get a Task for work", () -> {
                    Task task = taskService.getTaskForWork(student);
                    return String.format("You are now assigned to '%s' task", task.getName());
                }),
//                new Menu.Option("See all your Tasks", () -> {
//                    System.out.println("Loading Tasks ...\n");
//                    Collection<Task> tasks = student.getTasks();
//                    tasks.forEach(System.out::println);
//                    return "Total tasks count: " + tasks.size();
//                }),
                new Menu.Option("See all your to do Tasks", () -> {
                    System.out.println("Loading Tasks ...\n");
                    Collection<Task> tasks = taskService.seeAllYourToDoTasks(student);
                    return "Total tasks count: " + tasks.size();
                }),
                new Menu.Option("Import content to a Task", () -> {
                    Task task = new TaskDialog().importContentToTask(student);
                    return String.format("You successfully assigned content! %s with %s", task, task.getTaskContent());
                }),
                new Menu.Option("Give in task to Senior", () -> {
                    Task task = new TaskDialog().giveInTaskForCheck(student);
                    return String.format("Task '%s' successfully given to the senior!", task.getName());
                })
        ));
        menu.show();
    }
}
