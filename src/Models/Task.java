package Models;

import Contracts.Identifiable;
import Enums.DIFFICULTY;

public class Task implements Identifiable<Long> {
    private static long nextId = 0;
    private Long id;
    private String name;
    private DIFFICULTY difficulty;
    private Long userId;
    private boolean isCompleted=false; //default is not completed

    private Result result;
    private Long resultId;

    private TaskContent taskContent;
    private Long taskContentId;

    public Long getTaskContentId() {
        return taskContentId;
    }

    public void setTaskContentId(Long taskContentId) {
        this.taskContentId = taskContentId;
    }

    public TaskContent getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(TaskContent taskContent) {
        this.taskContent = taskContent;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Task() {

    }

    public Task(String name, DIFFICULTY difficulty, Long userId, boolean isCompleted) {
        id = ++nextId;
        this.name = name;
        this.difficulty = difficulty;
        this.userId = userId;
        this.isCompleted = isCompleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DIFFICULTY getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DIFFICULTY difficulty) {
        this.difficulty = difficulty;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
            this.id=id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Task:{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", difficulty=").append(difficulty);
        sb.append(", isCompleted=").append(isCompleted);
        sb.append(", resultId=").append(resultId);
        sb.append('}');
        return sb.toString();
    }
}
