package Services;

import Contracts.ITaskService;
import Exceptions.ConstraintViolationException;
import Exceptions.InvalidEntityDataException;
import Exceptions.NonexistingEntityException;
import Implementations.JDBC;
import Infrastructure.TaskRepository;
import Models.Task;
import Models.Users.Student;
import Util.TaskValidator;
import Views.TaskDialog;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class TaskService implements ITaskService {
    private final TaskRepository taskRepo;
    private final TaskValidator validator;

    public TaskService(TaskRepository taskRepo, TaskValidator validator) {
        this.taskRepo = taskRepo;
        this.validator=validator;
    }

    @Override
    public Collection<Task> getAll() {
        return taskRepo.findAll();
    }

    @Override
    public Task getById(Long id) throws NonexistingEntityException {
        var task = taskRepo.findById(id);
        if (task==null){
            throw new NonexistingEntityException("Task with ID='" + id + "' does not exist.");
        }
        return task;
    }

    @Override
    public Task add(Task task) throws InvalidEntityDataException {
        try {
            validator.validate(task);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(
                    String.format("Error creating task '%s'", task.getName()),
                    ex
            );
        }
        return taskRepo.create(task);
    }

    @Override
    public Task update(Task task) throws NonexistingEntityException, InvalidEntityDataException {
        try {
            validator.validate(task);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(
                    String.format("Error creating task '%s'", task.getName()),
                    ex
            );
        }
        return taskRepo.update(task);
    }

    @Override
    public Task deleteById(Long id) throws NonexistingEntityException {
        return taskRepo.deleteById(id);
    }

    @Override
    public long count() {
        return taskRepo.count();
    }

    public void saveAllTasks() {
        Collection<Task> tasks =getAll();

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("tasks.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("Tasks:\n");
            tasks.forEach(task -> {
                printWriter.printf("Task name is '%s' with difficulty - '%s' \n",task.getName(),task.getDifficulty());
            });
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveAllCompletedTasks(){
        Collection<Task> tasks = new ArrayList<>();
        try {
            tasks = JDBC.getAllCompletedTasks();
        } catch (SQLException e) {
            System.out.println("No completed tasks found");
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("completedTasks.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("Tasks:\n");
            tasks.forEach(task -> {
                printWriter.printf("Completed Task name is '%s' with difficulty - '%s'\n",task.getName(),task.getDifficulty());
            });
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Task getTaskForWork(Student student){
        Task task = new TaskDialog().getTaskForWorkFromDB(student);
        student.getTasks().add(task);
        return task;
    }

    public Collection<Task> seeAllYourToDoTasks(Student student){
        Collection<Task> tasks = null;
        try {
            tasks = JDBC.getYourTasks(student);
        } catch (SQLException e) {
            System.out.println("No tasks found");
        }
        tasks.forEach(System.out::println);
        return tasks;
    }
}
