package Controllers;

import Contracts.IProjectService;
import Contracts.ITaskService;
import Contracts.IUserService;
import Views.Menu;

import java.util.List;

public class AdminController {
    private IUserService userService;
    private ITaskService taskService;
    private IProjectService projectService;

    public AdminController(IUserService userService, ITaskService taskService, IProjectService projectService) {
        this.userService = userService;
        this.taskService = taskService;
        this.projectService = projectService;
    }

    public void call() {
        var menu = new Menu("Admin Menu", List.of(
                new Menu.Option("Manage Users", () -> {
                    UserController userController = new UserController(userService);
                    userController.call();
                   return "Exited from Manage Users Menu";
                }),
                new Menu.Option("Manage Tasks", () -> {
                    TaskController taskController = new TaskController(taskService,userService);
                    taskController.call();
                    return "Exited from Manage Tasks Menu";
                }),
                new Menu.Option("Manage Projects", () -> {
                    ProjectController projectController = new ProjectController(projectService);
                    projectController.call();
                    return "Exited from Manage Projects Menu";
                })
        ));
        menu.show();
    }
}
