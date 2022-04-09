package Views;

import Contracts.EntityDialog;
import Contracts.IService;
import Contracts.ITaskService;
import Contracts.IUserService;
import Enums.DIFFICULTY;
import Enums.ROLE;
import Exceptions.ConstraintViolationException;
import Exceptions.NonexistingEntityException;
import Implementations.JDBC;
import Models.Result;
import Models.SeniorTask;
import Models.Task;
import Models.TaskContent;
import Models.Users.Senior;
import Models.Users.Student;
import Models.Users.User;
import Services.TaskService;
import Util.ResultValidator;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

public class TaskDialog implements EntityDialog<Task> {
    public static Scanner sc = new Scanner(System.in);
    private ResultValidator resultValidator;

    public TaskDialog() {
    }

    public TaskDialog(ResultValidator resultValidator) {
        this.resultValidator = resultValidator;
    }

    @Override
    public Task input() {
        Task task = new Task();
        while (task.getName() == null) {
            System.out.println("Enter Task Name:");
            String ans = sc.nextLine();
            if (ans.length() < 5 || ans.length() > 30) {
                System.out.println("Error: Task Name should be in the range 5 and 30 characters long.");
            } else {
                task.setName(ans);
            }
        }
        while (task.getDifficulty() == null) {
            System.out.println("Set Difficulty:");
            System.out.printf("Options are: ");
            Arrays.stream(DIFFICULTY.values()).forEach(result -> System.out.printf(" %s", result));

            String ans = sc.nextLine();

            //check if it matches any of the difficulties we have
            try {
                DIFFICULTY difficulty = DIFFICULTY.EASY.valueOf(ans);
                if (!Arrays.stream(DIFFICULTY.values()).anyMatch(r -> r == difficulty)) {
                    System.out.println("Error: Invalid DIFFICULTY!");
                } else {
                    task.setDifficulty(difficulty);
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Invalid DIFFICULTY!");
            }
        }
        return task;
    }

    public Task giveTaskToUser(ITaskService taskService, IUserService userService) {
        Task task = null;
        while (task == null) {
            System.out.println("Enter Task's id for assignment:");
            Long id = sc.nextLong();

            try {
                task = taskService.getById(id);
                System.out.printf("You got successfully %s\n", task.getName());
            } catch (NonexistingEntityException e) {
                System.out.println("Task with this id does not exist!");
            }
        }
        User user = null;
        while (user == null) {
            System.out.println("Enter User's id for assignment:");
            Long id = sc.nextLong();

            try {
                user = userService.getById(id);
                System.out.printf("You got successfully %s\n", user.getFirstName());
            } catch (NonexistingEntityException e) {
                System.out.println("User with this id does not exist!");
            }
        }
        task.setUserId(user.getId());
        return task;
    }

    public Task inputDelete(ITaskService taskService) {
        Task task = null;
        while (task == null) {
            System.out.println("Enter Task's id for delete:");
            Long id = sc.nextLong();

            try {
                task = taskService.getById(id);
                System.out.printf("You got successfully %s\n", task.getName());
            } catch (NonexistingEntityException e) {
                System.out.println("Task with this id does not exist!");
            }
        }
        return task;
    }

    public Task getTaskForWork(ITaskService taskService, Student student) {
        Collection<Task> tasks = taskService.getAll();
        tasks.forEach(System.out::println);
        Task task = null;
        while (task == null) {
            System.out.println("Choose a Task id from the list above:");
            Long id = sc.nextLong();

            try {
                task = taskService.getById(id);
                System.out.printf("You got successfully %s\n", task.getName());
            } catch (NonexistingEntityException e) {
                System.out.println("Task with this id does not exist!");
            }
        }
        task.setUserId(student.getId());
        return task;
    }

    public Task getTaskForWorkFromDB(Student student) {
        Task task = null;
        while (task == null) {
            try {
                Collection<Task> tasks = JDBC.getListOfNotDoneTasks();
                tasks.forEach(System.out::println);
            } catch (SQLException e) {
                System.out.println("No tasks available for work");
            }
            System.out.println("Choose a Task id from the list above:");
            Long id = sc.nextLong();

            try {
                task = JDBC.getNotCompletedAndAssignedTaskById(id);
                System.out.printf("You got successfully %s\n", task.getName());
            } catch (SQLException e) {
                System.out.println("Task with this id does not exist!");
            }
        }
        try {
            JDBC.setTaskToUser(student.getId(), task.getId());
        } catch (SQLException e) {
            System.out.println("Something went wrong with getting the Task");
        }
        return task;
    }

    public Task giveInTaskForCheck(Student student) {
        Task task = null;
        while (task == null) {
            try {
                Collection<Task> tasks = JDBC.getYourTasks(student);
                tasks.forEach(System.out::println);
            } catch (SQLException e) {
                System.out.println("No tasks available for work");
            }
            System.out.println("Choose a Task id from the list above to give in:");
            Long taskId = sc.nextLong();

            try {
                task = JDBC.getTaskByIdFromYourTasks(student.getId(), taskId);
                System.out.printf("You got successfully %s\n", task.getName());
            } catch (SQLException e) {
                System.out.println("Incorrect id for task!");
            }
        }
        Senior senior = null;
        while (senior == null) {
            try {
                Collection<Senior> seniors = JDBC.getAllSeniors();
                seniors.forEach(System.out::println);
            } catch (SQLException e) {
                System.out.println("Seniors not found");
            }
            System.out.println("Choose a Senior id from the list above to check your task:");
            Long seniorId = sc.nextLong();

            try {
                senior = JDBC.getSeniorById(seniorId);
                System.out.printf("You choose successfully %s\n", senior.getFirstName());
            } catch (SQLException e) {
                System.out.println("Incorrect id for senior!");
            }
        }

        try {
            JDBC.giveTaskToSenior(senior.getId(), task.getId());
        } catch (SQLException e) {
            System.out.println("Incorrect id for senior!");
        }

        try {
            JDBC.assignTaskToSenior(task.getId(), senior.getId());
        } catch (SQLException e) {
            System.out.println("Senior not assigned to check the task");
        }

        return task;
    }

    public static Task assignUserToTask() {
        Task task = null;
        while (task == null) {
            try {
                Collection<Task> tasks = JDBC.getListOfNotDoneTasks();
                tasks.forEach(System.out::println);
            } catch (SQLException e) {
                System.out.println("No tasks available for assignment");
            }

            System.out.println("Enter Task's id for assignment:");
            Long id = sc.nextLong();

            try {
                task = JDBC.getTaskByIdWhereNotAssigned(id);//?
                System.out.printf("You got successfully %s\n", task.getName());
            } catch (SQLException e) {
                System.out.println("Task with this id does not exist!");
            }
        }
        Student student = null;
        while (student == null) {
            try {
                Collection<Student> students = JDBC.getAllStudents();
                students.forEach(System.out::println);
            } catch (SQLException e) {
                System.out.println("Students not found");
            }
            System.out.println("Choose a Student id from the list above to assign the task:");
            Long studentId = sc.nextLong();

            try {
                student = JDBC.getStudentById(studentId);
                System.out.printf("You choose successfully %s\n", student.getFirstName());
            } catch (SQLException e) {
                System.out.println("Incorrect id for student!");
            }
        }
        task.setUserId(student.getId());
        try {
            JDBC.assignTaskToStudent(task.getId(), student.getId());
        } catch (SQLException e) {
            System.out.println("Student not assigned to the task");
        }
        return task;
    }

    public static Task checkTask(Long seniorId) {
        Task task = null;
        while (task == null) {
            try {
                Collection<Task> tasks = JDBC.getTasksForCheck(seniorId);
                tasks.forEach(System.out::println);
            } catch (SQLException e) {
                System.out.println("All tasks checked!");
            }

            System.out.println("Enter Task's id for checking content:");
            Long taskId = sc.nextLong();

            try {
                task = JDBC.getTaskFromCheckList(seniorId, taskId);
                System.out.printf("You got successfully %s\n", task.getName());
            } catch (SQLException e) {
                System.out.println("Task with this id does not exist!");
            }
        }
        TaskContent taskContent = null;
        try {
            taskContent = JDBC.getTaskContentById(task.getTaskContentId());
        } catch (SQLException e) {
            System.out.println("Couldn't get task content");
        }
        task.setTaskContent(taskContent);
        return task;
    }

    public Task markTask(Long seniorId) {
        Task task = null;
        while (task == null) {
            System.out.println("Enter Task's id for assigning feedback:");
            Long taskId = sc.nextLong();

            try {
                task = JDBC.getTaskFromCheckList(seniorId, taskId);
                System.out.printf("You got successfully %s\n", task.getName());
            } catch (SQLException e) {
                System.out.println("Task with this id does not exist!");
            }
        }
        sc.nextLine();
        Result result = new Result();
        while (result.getFeedback() == null) {
            System.out.println("Enter Feedback:");
            String ans = sc.nextLine();
            if (ans.length() < 10 || ans.length() > 300) {
                System.out.println("Error: Feedback should be in the range 10 and 300 characters long.");
            } else {
                result.setFeedback(ans);
            }
        }
        while (result.getGrade() == null) {
            System.out.println("Enter Grade:");
            Integer ans = sc.nextInt();
            if (ans < 0 || ans > 100 || ans==null) {
                System.out.println("Error: Grade should be in the range 0 and 100 percent.");
            } else {
                result.setGrade(ans);
            }
        }
        try {
            JDBC.createResult(result.getFeedback(), result.getGrade());
        } catch (SQLException e) {
            System.out.println("Result creation failed");
        }

        try {
            result = JDBC.getResultByFeedback(result.getFeedback());
        } catch (SQLException e) {
            System.out.println("No such result found");
        }

        try {
            JDBC.setResultToTask(task.getId(), result.getId());
        } catch (SQLException e) {
            System.out.println("Result set failed");
        }

        task.setResult(result);
        task.setResultId(result.getId());
        //set to completed
        try {
            JDBC.updateTaskToCompleted(task.getId());
        } catch (SQLException e) {
            System.out.println("Task update failed");
        }
        task.setCompleted(true);

        //set userId to null
        try {
            JDBC.setUserIdToNull(task.getId());
        } catch (SQLException e) {
            System.out.println("Updating userId in task failed");
        }

        return task;
    }

    public Task deleteTask() {
        Task task = null;
        while (task == null) {
            Collection<Task> tasks = null;
            try {
                tasks = JDBC.getAllTasks();
            } catch (SQLException e) {
                System.out.println("Couldn't load Tasks");
            }
            tasks.forEach(System.out::println);
            System.out.println("Enter Task's id for delete:");
            Long id = sc.nextLong();

            try {
                task = JDBC.getTaskById(id);
            } catch (SQLException e) {
                System.out.println("Invalid task Id!");
            }
        }
        try {
            JDBC.deleteTask(task.getId());
        } catch (SQLException e) {
            System.out.println("Deleting task failed");
        }
        return task;
    }

    public Task importContentToTask(Student student) {
        Task task = null;
        while (task == null) {
            try {
                Collection<Task> tasks = JDBC.getYourTasks(student);
                tasks.forEach(System.out::println);
            } catch (SQLException e) {
                System.out.println("No tasks available for work");
            }
            System.out.println("Choose a Task id from the list above to import content to:");
            Long taskId = sc.nextLong();

            try {
                task = JDBC.getTaskByIdFromYourTasks(student.getId(), taskId);
                System.out.printf("You got successfully %s\n", task.getName());
            } catch (SQLException e) {
                System.out.println("Incorrect id for task!");
            }
        }
        TaskContent taskContent = new TaskContent();
        while (taskContent.getContent() == null) {
            System.out.println("Enter Your assignment/content:");
            String ans = sc.nextLine();
            if (ans.length() < 50 || ans.length() > 1024) {
                System.out.println("Error: Content should be in the range 50 and 1024 characters long.");
            } else {
                taskContent.setContent(ans);
            }
        }
        while (true) {
            System.out.println("Enter comment to your task:");
            String ans = sc.nextLine();
            if (ans.length() >= 60) {
                System.out.println("Error: Comment is too long (max 60 characters long).");
                continue;
            }
            taskContent.setComment(ans);
            break;
        }

        //add taskContent to DB
        try {
            JDBC.addTaskContent(taskContent.getContent(),taskContent.getComment());
        } catch (SQLException e) {
            System.out.println("Adding content failed!");
        }
        //get id on taskContentByContent
        try {
            taskContent=JDBC.getTaskContentByContent(taskContent.getContent());
        } catch (SQLException e) {
            System.out.println("Could not get task content's id");
        }
        //assign content to Task
        try {
            JDBC.assignContentToTask(taskContent.getId(),task.getId());
        } catch (SQLException e) {
            System.out.println("Content couldn't manage assignment");
        }
        task.setTaskContent(taskContent);

        return task;
    }

    public static Task declineTask(Long seniorId){
        Task task = null;
        while (task == null) {
            try {
                Collection<Task> tasks = JDBC.getTasksForCheck(seniorId);
                tasks.forEach(System.out::println);
            } catch (SQLException e) {
                System.out.println("All tasks checked!");
            }

            System.out.println("Enter Task's id for readjustment:");
            Long taskId = sc.nextLong();

            try {
                task = JDBC.getTaskFromCheckList(seniorId, taskId);
                System.out.printf("You got successfully %s\n", task.getName());
            } catch (SQLException e) {
                System.out.println("Task with this id does not exist!");
            }
        }
        sc.nextLine();
        //add feedback why
        Result result = new Result();
        while (result.getFeedback() == null) {
            System.out.println("Enter Feedback:");
            String ans = sc.nextLine();
            if (ans.length() < 10 || ans.length() > 300) {
                System.out.println("Error: Feedback should be in the range 10 and 300 characters long.");
            } else {
                result.setFeedback(ans);
            }
        }
        while (result.getGrade() == null) {
            System.out.println("Enter Grade:");
            Integer ans = sc.nextInt();
            if (ans < 0 || ans > 100) {
                System.out.println("Error: Grade should be in the range 0 and 100 percent.");
            } else {
                result.setGrade(ans);
            }
        }
        try {
            JDBC.createResult(result.getFeedback(), result.getGrade());
        } catch (SQLException e) {
            System.out.println("Result creation failed");
        }

        try {
            result = JDBC.getResultByFeedback(result.getFeedback());
        } catch (SQLException e) {
            System.out.println("No such result found");
        }

        try {
            JDBC.setResultToTask(task.getId(), result.getId());
        } catch (SQLException e) {
            System.out.println("Result set failed");
        }
        task.setResult(result);
        task.setResultId(result.getId());
        try {
            JDBC.setTaskToUndone(task.getId());
        } catch (SQLException e) {
            System.out.println("Failed to resubmit task for refactoring");
        }
        return task;
    }
}