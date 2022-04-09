package Controllers;

import Contracts.IProjectService;
import Exceptions.NonexistingEntityException;
import Implementations.JDBC;
import Models.Project;
import Models.Users.User;
import Services.ProjectService;
import Views.Menu;
import Views.ProjectDialog;
import Views.UserDialog;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class ProjectController {
    private IProjectService projectService;

    public ProjectController(IProjectService projectService) {
        this.projectService = projectService;
    }

    public  void call(){
        var menu = new Menu("Manage Projects Menu", List.of(
                new Menu.Option("Add new Project", ()->{//done
                    Project project = new ProjectDialog().createProject();
                    return String.format("Project ID:%s: '%s' added successfully.",
                            project.getId(), project.getTitle());
                }),
                new Menu.Option("Delete Project", ()->{//done
                    Project project = new ProjectDialog().deleteProject();
                    return String.format("Project: %s with ID:%s: successfully deleted.",
                            project.getTitle(),project.getId());
                }),
                new Menu.Option("Print All Projects",() ->{
                    System.out.println("Loading Projects ...\n");
                    Collection<Project> projects = null;
                    try {
                        projects = JDBC.getAllProjects();
                    } catch (SQLException e) {
                        System.out.println("Couldn't load Projects");
                    }
                    projects.forEach(System.out::println);
                    return "Total projects count: " + projects.size();
                })
        ));
        menu.show();
    }
}
