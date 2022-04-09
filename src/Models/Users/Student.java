package Models.Users;

import Enums.ROLE;
import Models.Task;

import java.util.ArrayList;
import java.util.List;

public class Student extends User{
    private List<Task> tasks = new ArrayList<>();
    private Senior senior;

    public Student() {
    }

    public Senior getSenior() {
        return senior;
    }

    public void setSenior(Senior senior) {
        this.senior = senior;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Student(String firstName, String lastName, String email, String username, String password) {
        super(firstName, lastName, email, username, password, ROLE.STUDENT);
    }
}
