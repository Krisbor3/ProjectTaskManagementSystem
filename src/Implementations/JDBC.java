package Implementations;

import Enums.DIFFICULTY;
import Enums.ROLE;
import Exceptions.EntityPersistenceException;
import Models.*;
import Models.Users.Senior;
import Models.Users.Student;
import Models.Users.User;

import javax.management.relation.Role;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import static Enums.ROLE.NOT_SET;

public class JDBC {
    private static final String GET_ALL_USERS = "SELECT * FROM users";
    private static final String GET_ALL_PROJECTS="SELECT * FROM ptms.projects";
    private static final String GET_ALL_TASKS = "SELECT * FROM tasks ";
    private static final String ADD_USER = "INSERT INTO `ptms`.`users` (`firstname`, `lastname`, `email`, `username`, `password`, `role`)" +
            " VALUES (?, ?, ?, ?, ?, ?);";
    private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE (id = ? );";
    private static final String UPDATE_USER_ROLE = "UPDATE `ptms`.`users` SET `role` = ? WHERE (`id` = ? );";
    private static final String DELETE_USER = "DELETE FROM `ptms`.`users` WHERE (`id` = ? );";
    private static final String GET_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ? ";
    private static final String GET_USER_BY_PASSWORD = "SELECT * FROM users WHERE username= ? " +
            "and password= ? ";
    private static final String GET_TASK_BY_ID = "SELECT * FROM ptms.tasks WHERE id= ? ";
    private static final String SET_TASK_TO_USER = "UPDATE `ptms`.`tasks` SET `userId` = ? WHERE (`id` = ?);";
    private static final String GET_YOUR_ASSIGNED_TASKS = "SELECT * FROM ptms.tasks WHERE userId = ? ";
    private static final String GET_SPECIFIC_TASK_FROM_TODO = "SELECT * FROM ptms.tasks WHERE userId = ? " +
            "and id= ? ";
    private static final String GET_TASK_BY_ID_NOT_ASSIGNED = "SELECT * FROM ptms.tasks WHERE userId is null " +
            "and id= ? ";
    private static final String GET_ALL_UNASSIGNED_TASKS = "SELECT * FROM ptms.tasks WHERE userId is null " +
            "and isCompleted=0";
    private static final String GET_ALL_SENIORS = "SELECT * FROM ptms.users WHERE role= \"SENIOR\";";
    private static final String GIVE_TASKS_TO_SENIOR = "INSERT INTO `ptms`.`senior_tasks` (`senior_id`, `task_id`) VALUES ( ? , ? );";
    private static final String GET_SENIOR_BY_ID = "SELECT * FROM ptms.users WHERE role=\"SENIOR\" " +
            "and id= ? ;";
    private static final String GET_STUDENT_BY_ID = "SELECT * FROM ptms.users WHERE role=\"STUDENT\" " +
            "and id= ? ;";
    private static final String ASSIGN_TASK_TO_SENIOR = "UPDATE `ptms`.`tasks` SET `userId` = ? WHERE (`id` = ? );";
    private static final String ASSIGN_TASK_TO_STUDENT = "UPDATE `ptms`.`tasks` SET `userId` = ? WHERE (`id` = ? );";
    private static final String ADD_TASK = "INSERT INTO `ptms`.`tasks` (`name`, `difficulty`) VALUES ( ? , ? );";
    private static final String GET_ALL_STUDENTS = "SELECT * FROM ptms.users WHERE role= \"STUDENT\";";
    private static final String GET_TASKS_FOR_CHECK = "SELECT * FROM ptms.tasks WHERE userId= ? ";
    private static final String GET_TASK_FROM_CHECK_LIST = "SELECT * FROM ptms.tasks WHERE userId = ? and id= ? ";
    private static final String CREATE_RESULT = "INSERT INTO `ptms`.`result` (`feedback`, `grade`) VALUES ( ? , ? );";
    private static final String SET_RESULT_TO_TASK = "UPDATE `ptms`.`tasks` SET `resultId` = ? WHERE (`id` = ? );";
    private static final String GET_RESULT_BY_FEEDBACK = "SELECT * FROM ptms.result WHERE feedback= ? ;";
    private static final String UPDATE_TASK_TO_COMPLETED = "UPDATE `ptms`.`tasks` SET `isCompleted` = '1' WHERE (`id` = ? );";
    private static final String SET_TASK_ID_TO_NULL = "UPDATE `ptms`.`tasks` SET `userId` = NULL WHERE (`id` = ? );";
    private static final String DELETE_TASK = "DELETE FROM `ptms`.`tasks` WHERE (`id` = ? );";
    private static final String GET_ALL_COMPLETED_TASKS = "SELECT * FROM ptms.tasks where isCompleted=1;";
    private static final String GET_RESULT_BY_ID = "SELECT * FROM ptms.result WHERE id= ? ;";
    private static final String ADD_TASK_CONTENT = "INSERT INTO `ptms`.`tasks_contents` (`content`, `comment`) VALUES ( ? , ? );";
    private static final String GET_TASK_CONTENT_BY_CONTENT = "SELECT * FROM ptms.tasks_contents where content= ? ;";
    private static final String ASSIGN_CONTENT_TO_TASK = "UPDATE `ptms`.`tasks` SET `contentId` = ? WHERE (`id` = ? );";
    private static final String GET_NOT_COMPLETED_AND_ASSIGNED_TASK = "SELECT * FROM ptms.tasks  where isCompleted=0 " +
            "and userId is null and id = ? ;";
    private static final String SET_TASK_TO_UNDONE = "UPDATE `ptms`.`tasks` SET `userId` = NULL WHERE (`id` = ? );";
    private static final String GET_TASK_CONTENT_BY_ID = "SELECT * FROM ptms.tasks_contents where id= ? ;";
    private static final String CREATE_PROJECT = "INSERT INTO `ptms`.`projects` (`title`) VALUES ( ? );";
    private static final String GET_PROJECT_BY_ID="SELECT * FROM ptms.projects where id= ? ;";
    private static final String DELETE_PROJECT="DELETE FROM `ptms`.`projects` WHERE (`id` = ? );";

    private static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(JDBCConstants.Url, JDBCConstants.User, JDBCConstants.Password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAllUsers() throws SQLException {

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(GET_ALL_USERS);

        while (resultSet.next()) {
            System.out.printf("FirstName: '%s' LastName: '%s' Email: '%s' Username: '%s' Password: '%s' Role: '%s'\n",
                    resultSet.getString("firstname"),
                    resultSet.getString("lastname"),
                    resultSet.getString("email"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("role"));
        }
    }

    public static Collection<Task> getAllTasks() throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(GET_ALL_TASKS);

        Collection<Task> tasks = new ArrayList<>();
        while (resultSet.next()) {
            Task task = new Task();
            task.setId(resultSet.getLong("id"));
            task.setName(resultSet.getString("name"));
            task.setDifficulty(DIFFICULTY.valueOf(resultSet.getString("difficulty")));
            task.setCompleted(resultSet.getBoolean("isCompleted"));
            task.setUserId(resultSet.getLong("userId"));
            task.setResultId(resultSet.getLong("resultId"));
            tasks.add(task);
        }
        return tasks;
    }

    public static User addUserToDB(User user) {
        try (PreparedStatement stmt = connection.prepareStatement(ADD_USER, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getUsername());
            stmt.setString(5, user.getPassword());
            stmt.setString(6, user.getRole().toString());

            connection.setAutoCommit(false);
            int affectedRows = stmt.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);

            if (affectedRows == 0) {
                throw new EntityPersistenceException("Creating user failed, no rows affected.");
            }
        } catch (SQLException e) {
            System.out.println("Failed user registration");
        }
        return user;
    }

    public static User getUserByIdFromDB(Long id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(GET_USER_BY_ID, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, id);

        ResultSet resultSet = stmt.executeQuery();


        User user = new User();
        user.setId(id);
        while (resultSet.next()) {
            user.setFirstName(resultSet.getString("firstname"));
            user.setLastName(resultSet.getString("lastname"));
            user.setEmail(resultSet.getString("email"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
        }
        return user;
    }

    public static Task getTaskById(Long id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(GET_TASK_BY_ID, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, id);

        ResultSet resultSet = stmt.executeQuery();

        Task task = new Task();

        while (resultSet.next()) {
            task.setId(resultSet.getLong("id"));
            task.setName(resultSet.getString("name"));
            task.setDifficulty(DIFFICULTY.valueOf(resultSet.getString("difficulty")));
            task.setUserId(resultSet.getLong("userId"));
            task.setCompleted(resultSet.getBoolean("isCompleted"));
        }
        if (task.getName() == null) {
            throw new SQLException();
        }
        return task;
    }

    public static void setTaskToUser(Long userId, Long taskId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(SET_TASK_TO_USER, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, userId);
        stmt.setLong(2, taskId);


        int affectedRows = stmt.executeUpdate();


        if (affectedRows == 0) {
            throw new EntityPersistenceException("Creating user failed, no rows affected.");
        }

        System.out.println("Successfully obtained Task");
    }

    public static User updateUserToDB(User user) {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_USER_ROLE, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getRole().toString());
            stmt.setLong(2, user.getId());
            connection.setAutoCommit(false);
            int affectedRows = stmt.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);

            if (affectedRows == 0) {
                throw new EntityPersistenceException("Updating user failed, no rows affected.");
            }

        } catch (SQLException e) {
            System.out.println("Updating user failed, no rows affected.");
            e.printStackTrace();
        }
        return user;
    }

    public static User deleteUserFromDB(User user) {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_USER, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, user.getId());
            connection.setAutoCommit(false);
            int affectedRows = stmt.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);

            if (affectedRows == 0) {
                throw new EntityPersistenceException("Deleting user failed, no rows affected.");
            }

        } catch (SQLException e) {
            System.out.println("Deleting user failed, no rows affected.");
            e.printStackTrace();
        }
        return user;
    }

    public static String getUserByUsername(String username) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(GET_USER_BY_USERNAME, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, username);

        ResultSet resultSet = stmt.executeQuery();

        User user = new User();

        while (resultSet.next()) {
            user.setFirstName(resultSet.getString("firstname"));
            user.setLastName(resultSet.getString("lastname"));
            user.setEmail(resultSet.getString("email"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
        }
        return user.getUsername();
    }

    public static User getPassword(String username, String password) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(GET_USER_BY_PASSWORD, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, username);
        stmt.setString(2, password);

        ResultSet resultSet = stmt.executeQuery();

        User user = new User();

        while (resultSet.next()) {
            user.setId(resultSet.getLong("id"));
            user.setFirstName(resultSet.getString("firstname"));
            user.setLastName(resultSet.getString("lastname"));
            user.setEmail(resultSet.getString("email"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setRole(ROLE.valueOf(resultSet.getString("role")));
        }
        user.setUsername(username);
        return user;
    }

    public static Collection<Task> getYourTasks(Student student) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(GET_YOUR_ASSIGNED_TASKS, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, student.getId());

        ResultSet resultSet = stmt.executeQuery();

        Collection<Task> tasks = new ArrayList<>();

        while (resultSet.next()) {
            Task task = new Task();
            task.setId(resultSet.getLong("id"));
            task.setName(resultSet.getString("name"));
            task.setDifficulty(DIFFICULTY.valueOf(resultSet.getString("difficulty")));
            task.setUserId(resultSet.getLong("userId"));
            task.setCompleted(resultSet.getBoolean("isCompleted"));
            tasks.add(task);
        }
        return tasks;
    }

    public static Collection<Task> getListOfNotDoneTasks() throws SQLException {

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(GET_ALL_UNASSIGNED_TASKS);


        Collection<Task> tasks = new ArrayList<>();

        while (resultSet.next()) {
            Task task = new Task();
            task.setId(resultSet.getLong("id"));
            task.setName(resultSet.getString("name"));
            task.setDifficulty(DIFFICULTY.valueOf(resultSet.getString("difficulty")));
            task.setUserId(resultSet.getLong("userId"));
            task.setCompleted(resultSet.getBoolean("isCompleted"));
            tasks.add(task);
        }
        return tasks;
    }

    public static Collection<Senior> getAllSeniors() throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(GET_ALL_SENIORS);

        Collection<Senior> seniors = new ArrayList<>();
        while (resultSet.next()) {
            Senior senior = new Senior(
                    resultSet.getString("firstname"),
                    resultSet.getString("lastname"),
                    resultSet.getString("email"),
                    resultSet.getString("username"),
                    resultSet.getString("password")
            );
            senior.setId(resultSet.getLong("id"));
            seniors.add(senior);
        }
        return seniors;
    }

    public static Collection<Student> getAllStudents() throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(GET_ALL_STUDENTS);

        Collection<Student> students = new ArrayList<>();
        while (resultSet.next()) {
            Student student = new Student(
                    resultSet.getString("firstname"),
                    resultSet.getString("lastname"),
                    resultSet.getString("email"),
                    resultSet.getString("username"),
                    resultSet.getString("password")
            );
            student.setId(resultSet.getLong("id"));
            students.add(student);
        }
        return students;

    }

    public static void giveTaskToSenior(Long seniorId, Long taskId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(GIVE_TASKS_TO_SENIOR, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, seniorId);
        stmt.setLong(2, taskId);

        connection.setAutoCommit(false);
        int affectedRows = stmt.executeUpdate();
        connection.commit();
        connection.setAutoCommit(true);

        if (affectedRows == 0) {
            throw new SQLException("Giving senior the task failed, no rows affected.");
        }
    }

    public static Task getTaskByIdFromYourTasks(Long studentId, Long taskId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(GET_SPECIFIC_TASK_FROM_TODO, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, studentId);
        stmt.setLong(2, taskId);

        ResultSet resultSet = stmt.executeQuery();

        Task task = new Task();

        while (resultSet.next()) {
            task.setId(resultSet.getLong("id"));
            task.setName(resultSet.getString("name"));
            task.setDifficulty(DIFFICULTY.valueOf(resultSet.getString("difficulty")));
            task.setUserId(resultSet.getLong("userId"));
            task.setCompleted(resultSet.getBoolean("isCompleted"));
        }
        if (task.getName() == null) {
            throw new SQLException();
        }
        return task;
    }

    public static Senior getSeniorById(Long seniorId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(GET_SENIOR_BY_ID, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, seniorId);

        ResultSet resultSet = stmt.executeQuery();

        Senior senior = new Senior();
        while (resultSet.next()) {
            senior.setId(resultSet.getLong("id"));
            senior.setFirstName(resultSet.getString("firstname"));
            senior.setLastName(resultSet.getString("lastname"));
            senior.setEmail(resultSet.getString("email"));
            senior.setUsername(resultSet.getString("username"));
            senior.setPassword(resultSet.getString("password"));
        }
        if (senior.getFirstName() == null) {
            throw new SQLException();
        }
        return senior;
    }

    public static Student getStudentById(Long studentId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(GET_STUDENT_BY_ID, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, studentId);

        ResultSet resultSet = stmt.executeQuery();

        Student student = new Student();
        while (resultSet.next()) {
            student.setId(resultSet.getLong("id"));
            student.setFirstName(resultSet.getString("firstname"));
            student.setLastName(resultSet.getString("lastname"));
            student.setEmail(resultSet.getString("email"));
            student.setUsername(resultSet.getString("username"));
            student.setPassword(resultSet.getString("password"));
        }
        if (student.getFirstName() == null) {
            throw new SQLException();
        }
        return student;
    }

    public static void assignTaskToSenior(Long taskId, Long seniorId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(ASSIGN_TASK_TO_SENIOR, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, seniorId);
        stmt.setLong(2, taskId);

        connection.setAutoCommit(false);
        int affectedRows = stmt.executeUpdate();
        connection.commit();
        connection.setAutoCommit(true);

        if (affectedRows == 0) {
            throw new SQLException("Giving senior the task failed, no rows affected.");
        } else {
            System.out.println("Senior assigned to check the task");
        }
    }

    public static void assignTaskToStudent(Long taskId, Long studentId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(ASSIGN_TASK_TO_STUDENT, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, studentId);
        stmt.setLong(2, taskId);

        connection.setAutoCommit(false);
        int affectedRows = stmt.executeUpdate();
        connection.commit();
        connection.setAutoCommit(true);

        if (affectedRows == 0) {
            throw new SQLException("Giving student the task failed, no rows affected.");
        } else {
            System.out.println("Student assigned to task");
        }
    }

    public static Task addTaskToDB(Task task) {
        try (PreparedStatement stmt = connection.prepareStatement(ADD_TASK, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, task.getName());
            stmt.setString(2, task.getDifficulty().toString());

            connection.setAutoCommit(false);
            int affectedRows = stmt.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);

            if (affectedRows == 0) {
                throw new EntityPersistenceException("Creating task failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    task.setId(generatedKeys.getLong(1));
                    return task;
                } else {
                    throw new EntityPersistenceException("Creating task failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new EntityPersistenceException("Error rolling back SQL query: " + ADD_TASK, ex);
            }
            throw new EntityPersistenceException("Error executing SQL query: " + ADD_TASK, ex);
        }
    }

    public static Task getTaskByIdWhereNotAssigned(Long taskId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(GET_TASK_BY_ID_NOT_ASSIGNED, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, taskId);

        ResultSet resultSet = stmt.executeQuery();

        Task task = new Task();

        while (resultSet.next()) {
            task.setId(resultSet.getLong("id"));
            task.setName(resultSet.getString("name"));
            task.setDifficulty(DIFFICULTY.valueOf(resultSet.getString("difficulty")));
            task.setUserId(resultSet.getLong("userId"));
            task.setCompleted(resultSet.getBoolean("isCompleted"));
        }
        if (task.getName() == null) {
            throw new SQLException();
        }
        return task;
    }

    public static Collection<Task> getTasksForCheck(Long seniorId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(GET_TASKS_FOR_CHECK, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, seniorId);

        ResultSet resultSet = stmt.executeQuery();

        Collection<Task> tasks = new ArrayList<>();

        while (resultSet.next()) {
            Task task = new Task();
            task.setId(resultSet.getLong("id"));
            task.setName(resultSet.getString("name"));
            task.setDifficulty(DIFFICULTY.valueOf(resultSet.getString("difficulty")));
            task.setUserId(resultSet.getLong("userId"));
            task.setCompleted(resultSet.getBoolean("isCompleted"));
            task.setTaskContentId(resultSet.getLong("contentId"));
            tasks.add(task);
        }
        return tasks;
    }

    public static Task getTaskFromCheckList(Long seniorId, Long taskId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(GET_TASK_FROM_CHECK_LIST, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, seniorId);
        stmt.setLong(2, taskId);

        ResultSet resultSet = stmt.executeQuery();

        Task task = new Task();

        while (resultSet.next()) {
            task.setId(resultSet.getLong("id"));
            task.setName(resultSet.getString("name"));
            task.setDifficulty(DIFFICULTY.valueOf(resultSet.getString("difficulty")));
            task.setUserId(resultSet.getLong("userId"));
            task.setCompleted(resultSet.getBoolean("isCompleted"));
            task.setTaskContentId(resultSet.getLong("contentId"));
        }
        if (task.getName() == null) {
            throw new SQLException();
        }
        return task;
    }

    public static void createResult(String feedback, Integer grade) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(CREATE_RESULT, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, feedback);
        stmt.setInt(2, grade);

        connection.setAutoCommit(false);
        int affectedRows = stmt.executeUpdate();
        connection.commit();
        connection.setAutoCommit(true);

        if (affectedRows == 0) {
            throw new SQLException("Creating result failed, no rows affected.");
        }
        System.out.println("Result added successfully");
    }

    public static void setResultToTask(Long taskId, Long resultId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(SET_RESULT_TO_TASK, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, resultId);
        stmt.setLong(2, taskId);

        connection.setAutoCommit(false);
        int affectedRows = stmt.executeUpdate();
        connection.commit();
        connection.setAutoCommit(true);

        if (affectedRows == 0) {
            throw new SQLException("Assigning result failed, no rows affected.");
        }
        System.out.println("Result assigned successfully");
    }

    public static Result getResultByFeedback(String feedback) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(GET_RESULT_BY_FEEDBACK, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, feedback);

        ResultSet resultSet = stmt.executeQuery();


        Result result = new Result();
        while (resultSet.next()) {
            result.setId(resultSet.getLong("id"));
            result.setFeedback(resultSet.getString("feedback"));
            result.setGrade(resultSet.getInt("grade"));
        }
        return result;
    }

    public static void updateTaskToCompleted(Long taskId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(UPDATE_TASK_TO_COMPLETED, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, taskId);

        connection.setAutoCommit(false);
        int affectedRows = stmt.executeUpdate();
        connection.commit();
        connection.setAutoCommit(true);

        if (affectedRows == 0) {
            throw new SQLException("Updating task failed, no rows affected.");
        }
        System.out.println("Task is now completed");
    }

    public static void setUserIdToNull(Long taskId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(SET_TASK_ID_TO_NULL, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, taskId);

        connection.setAutoCommit(false);
        int affectedRows = stmt.executeUpdate();
        connection.commit();
        connection.setAutoCommit(true);

        if (affectedRows == 0) {
            throw new SQLException("Updating task userId failed, no rows affected.");
        }
        System.out.println("Student unassigned from task");
    }

    public static void deleteTask(Long taskId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(DELETE_TASK, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, taskId);

        connection.setAutoCommit(false);
        int affectedRows = stmt.executeUpdate();
        connection.commit();
        connection.setAutoCommit(true);

        if (affectedRows == 0) {
            throw new SQLException("Deleting task failed, no rows affected.");
        }
        System.out.println("Task successfully deleted");
    }

    public static Collection<Task> getAllCompletedTasks() throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(GET_ALL_COMPLETED_TASKS);

        Collection<Task> tasks = new ArrayList<>();
        while (resultSet.next()) {
            Task task = new Task();
            task.setId(resultSet.getLong("id"));
            task.setName(resultSet.getString("name"));
            task.setDifficulty(DIFFICULTY.valueOf(resultSet.getString("difficulty")));
            task.setUserId(resultSet.getLong("userId"));
            task.setCompleted(resultSet.getBoolean("isCompleted"));
            task.setResultId(resultSet.getLong("resultId"));
            tasks.add(task);
        }
        return tasks;
    }

    public static Result getResultById(Long resultId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(GET_RESULT_BY_ID, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, resultId);

        ResultSet resultSet = stmt.executeQuery();

        Result result = new Result();
        while (resultSet.next()) {
            result.setId(resultSet.getLong("id"));
            result.setFeedback(resultSet.getString("feedback"));
            result.setGrade(resultSet.getInt("grade"));
        }
        return result;
    }

    public static void addTaskContent(String content, String comment) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(ADD_TASK_CONTENT, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, content);
        stmt.setString(2, comment);

        connection.setAutoCommit(false);
        int affectedRows = stmt.executeUpdate();
        connection.commit();
        connection.setAutoCommit(true);

        if (affectedRows == 0) {
            throw new SQLException("Content adding failed, no rows affected.");
        }
        System.out.println("Content successfully added");
    }

    public static TaskContent getTaskContentByContent(String content) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(GET_TASK_CONTENT_BY_CONTENT, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, content);

        ResultSet resultSet = stmt.executeQuery();

        TaskContent taskContent = new TaskContent();
        while (resultSet.next()) {
            taskContent.setId(resultSet.getLong("id"));
            taskContent.setContent(resultSet.getString("content"));
            taskContent.setComment(resultSet.getString("comment"));
        }
        return taskContent;
    }

    public static void assignContentToTask(Long contentId, Long taskId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(ASSIGN_CONTENT_TO_TASK, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, contentId);
        stmt.setLong(2, taskId);

        connection.setAutoCommit(false);
        int affectedRows = stmt.executeUpdate();
        connection.commit();
        connection.setAutoCommit(true);

        if (affectedRows == 0) {
            throw new SQLException("Content assignment failed, no rows affected.");
        }
        System.out.println("Content successfully assigned to task");
    }

    public static Task getNotCompletedAndAssignedTaskById(Long taskId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(GET_NOT_COMPLETED_AND_ASSIGNED_TASK, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, taskId);

        ResultSet resultSet = stmt.executeQuery();

        Task task = new Task();
        while (resultSet.next()) {
            task.setId(resultSet.getLong("id"));
            task.setName(resultSet.getString("name"));
            task.setDifficulty(DIFFICULTY.valueOf(resultSet.getString("difficulty")));
            task.setUserId(resultSet.getLong("userId"));
            task.setCompleted(resultSet.getBoolean("isCompleted"));
            task.setResultId(resultSet.getLong("resultId"));
        }
        if (task.getId() == null) {
            throw new SQLException();
        }
        return task;
    }

    public static void setTaskToUndone(Long taskId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(SET_TASK_TO_UNDONE, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, taskId);

        connection.setAutoCommit(false);
        int affectedRows = stmt.executeUpdate();
        connection.commit();
        connection.setAutoCommit(true);

        if (affectedRows == 0) {
            throw new SQLException("Set task to undone failed, no rows affected.");
        }
        System.out.println("Task successfully resubmitted for refactoring");
    }

    public static TaskContent getTaskContentById(Long taskContentId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(GET_TASK_CONTENT_BY_ID, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, taskContentId);

        ResultSet resultSet = stmt.executeQuery();

        TaskContent taskContent = new TaskContent();
        while (resultSet.next()) {
            taskContent.setId(resultSet.getLong("id"));
            taskContent.setContent(resultSet.getString("content"));
            taskContent.setComment(resultSet.getString("comment"));
        }
        if (taskContent.getId() == null) {
            throw new SQLException();
        }
        return taskContent;
    }

    public static void createProject(String title) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(CREATE_PROJECT, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, title);

        connection.setAutoCommit(false);
        int affectedRows = stmt.executeUpdate();
        connection.commit();
        connection.setAutoCommit(true);

        if (affectedRows == 0) {
            throw new SQLException("Project creation failed, no rows affected.");
        }
    }
    public static Project getProjectById(Long projectId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(GET_PROJECT_BY_ID, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, projectId);

        ResultSet resultSet = stmt.executeQuery();

        Project project = new Project();
        while (resultSet.next()) {
            project.setId(resultSet.getLong("id"));
            project.setTitle(resultSet.getString("title"));
            project.setComplete(resultSet.getBoolean("isCompleted"));
        }
        if (project.getId() == null) {
            throw new SQLException();
        }
        return project;
    }
    public static void deleteProject(Long projectId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(DELETE_PROJECT, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, projectId);

        connection.setAutoCommit(false);
        int affectedRows = stmt.executeUpdate();
        connection.commit();
        connection.setAutoCommit(true);

        if (affectedRows == 0) {
            throw new SQLException("Project creation failed, no rows affected.");
        }
    }
    public static Collection<Project> getAllProjects() throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(GET_ALL_PROJECTS);

        Collection<Project> projects = new ArrayList<>();
        while (resultSet.next()) {
            Project project = new Project();
            project.setId(resultSet.getLong("id"));
            project.setTitle(resultSet.getString("title"));
            project.setComplete(resultSet.getBoolean("isCompleted"));

            projects.add(project);
        }
        return projects;
    }
}