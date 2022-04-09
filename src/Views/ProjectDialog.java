package Views;

import Contracts.EntityDialog;
import Contracts.IProjectService;
import Contracts.IUserService;
import Exceptions.ConstraintViolationException;
import Exceptions.NonexistingEntityException;
import Implementations.JDBC;
import Models.Project;
import Models.Users.User;
import Util.ProjectValidator;

import java.sql.SQLException;
import java.util.Scanner;

public class ProjectDialog implements EntityDialog<Project> {
    private ProjectValidator projectValidator = new ProjectValidator();


    public static Scanner sc = new Scanner(System.in);

    @Override
    public Project input() {
        Project project = new Project();
        while (project.getTitle() == null) {
            System.out.println("Enter Project Name:");
            String ans = sc.nextLine();
            if (ans.length() < 5 || ans.length() > 100) {
                System.out.println("Error: Project Name should be in the range 5 and 100 characters long.");
            } else {
                project.setTitle(ans);
            }
        }
        return project;
    }

    public Project inputDelete(IProjectService projectService) {
        Project project = null;
        while (project == null) {
            System.out.println("Enter Project's id for delete:");
            Long id = sc.nextLong();

            try {
                project = projectService.getById(id);
                System.out.printf("You got successfully %s\n", project.getTitle());
            } catch (NonexistingEntityException e) {
                System.out.println("Project with this id does not exist!");
            }
        }
        return project;
    }
    public Project deleteProject() {
        Project project = null;
        while (project == null) {
            System.out.println("Enter Project's id for delete:");
            Long id = sc.nextLong();

            try {
                project = JDBC.getProjectById(id);
                System.out.printf("You got successfully %s\n", project.getTitle());
            } catch (SQLException e) {
                System.out.println("Project with this id does not exist!");
            }
        }
        try {
            JDBC.deleteProject(project.getId());
        } catch (SQLException e) {
            System.out.println("Deleting project failed");
        }
        return project;
    }

    public Project createProject() {
        Project project = new Project();
        while (project.getTitle() == null) {
            System.out.println("Enter Project Title:");
            String ans = sc.nextLine();
            try {
                project.setTitle(ans);
                projectValidator.validate(project);
            } catch (ConstraintViolationException e) {
                System.out.println("Invalid title");
                continue;
            }
            project.setTitle(ans);
        }
        try {
            JDBC.createProject(project.getTitle());
            System.out.printf("You created successfully %s project\n", project.getTitle());
        } catch (SQLException e) {
            System.out.println("Project was not created!");
        }
        return project;
    }
}
