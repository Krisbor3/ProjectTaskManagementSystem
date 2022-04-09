import Contracts.IProjectService;
import Contracts.ITaskService;
import Contracts.IUserService;
import Controllers.*;
import Enums.ROLE;
import Exceptions.InvalidEntityDataException;
import Exceptions.NonexistingEntityException;
import Implementations.ProjectRepositoryImp;
import Implementations.TaskRepositoryImp;
import Implementations.UserRepositoryImp;
import Infrastructure.ProjectRepository;
import Infrastructure.TaskRepository;
import Infrastructure.UserRepository;
import Models.Users.Administrator;
import Models.Users.Senior;
import Models.Users.Student;
import Models.Users.User;
import Services.ProjectService;
import Services.TaskService;
import Services.UserService;
import Util.ProjectValidator;
import Util.TaskValidator;
import Util.UserValidator;

import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepo = new UserRepositoryImp();
        UserValidator userValidator = new UserValidator();
        IUserService userService = new UserService(userRepo, userValidator);

        TaskRepository taskRepo = new TaskRepositoryImp();
        TaskValidator taskValidator = new TaskValidator();
        ITaskService taskService = new TaskService(taskRepo,taskValidator);

        ProjectRepository projectRepo = new ProjectRepositoryImp();
        ProjectValidator projectValidator = new ProjectValidator();
        IProjectService projectService = new ProjectService(projectRepo,projectValidator);

        //Login && Register
        LoginController loginController = new LoginController();
        loginController.call();
        User u = loginController.getUserS();

        //Not assigned User
        if (u.getRole()==ROLE.NOT_SET){
            System.out.println("Apparently you do not have any rights for now sorry!");
            System.out.println("Reach out to the Admin or Senior to be assigned a role!");
            return;
        }
        //Student
        else if(u.getRole()== ROLE.STUDENT){
            Student student = new Student(u.getFirstName(),u.getLastName(),u.getEmail(),u.getUsername(),u.getPassword());
            student.setId(u.getId());
            StudentController studentController = new StudentController(taskService,student);
            studentController.call();
        }
        //Senior
        else if(u.getRole()==ROLE.SENIOR){
            Senior senior = new Senior(u.getFirstName(),u.getLastName(),u.getEmail(),u.getUsername(),u.getPassword());
            senior.setId(u.getId());
            TaskController taskController = new TaskController(taskService,userService,senior);
            taskController.call();
        }
        //Manage Users -> Admin
        else if(u.getRole()==ROLE.ADMINISTRATOR){
            System.out.println("Welcome ADMIN!");
            AdminController adminController = new AdminController(userService,taskService,projectService);
            adminController.call();
        }
    }
}
