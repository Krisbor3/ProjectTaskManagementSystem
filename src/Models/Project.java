package Models;

import Contracts.Identifiable;

import java.util.ArrayList;
import java.util.List;

public class Project implements Identifiable<Long> {
    private static long nextId = 0;
    private Long id;
    private String title;
    private boolean isComplete=false;//default

    public Project() {
        id = ++nextId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
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
        final StringBuilder sb = new StringBuilder("Project{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", isComplete='").append(isComplete);
        sb.append('}');
        return sb.toString();
    }
}
