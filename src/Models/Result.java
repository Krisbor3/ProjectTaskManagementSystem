package Models;

import Contracts.Identifiable;

public class Result implements Identifiable<Long> {
    private static long nextId = 0;
    private Long id;
    private String feedback;
    private Integer grade;

    public Result() {
        id = ++nextId;
    }

    public Result(String feedback, Integer grade) {
        this.feedback = feedback;
        this.grade = grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Result:{");
        sb.append("feedback='").append(feedback).append('\'');
        sb.append(", grade=").append(grade);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id=id;
    }
}
