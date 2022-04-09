package Contracts;

import Models.Task;
import Models.Users.Student;

import java.io.IOException;
import java.util.Collection;

public interface ITaskService extends IService<Task>{
    public void saveAllTasks();
    public void saveAllCompletedTasks();
    public Task getTaskForWork(Student student);
    public Collection<Task> seeAllYourToDoTasks(Student student);
}
