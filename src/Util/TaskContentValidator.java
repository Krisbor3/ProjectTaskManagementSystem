package Util;

import Contracts.IValidation;
import Exceptions.ConstraintViolation;
import Exceptions.ConstraintViolationException;
import Models.TaskContent;

import java.util.ArrayList;
import java.util.List;

public class TaskContentValidator implements IValidation<TaskContent> {
    @Override
    public void validate(TaskContent entity) throws ConstraintViolationException {
        List<ConstraintViolation> violations = new ArrayList<>();

        int contentLength = entity.getContent().trim().length();
        int commentLength = entity.getComment().trim().length();

        if (contentLength<50 || contentLength>1024){
            violations.add(new ConstraintViolation(entity.getClass().getName(),"content",entity.getContent(),
                    "Content length should be between 50 and 1024 characters."));
        }
        if (commentLength>60){
            violations.add(new ConstraintViolation(entity.getClass().getName(),"content",entity.getContent(),
                    "Comment length should not be over 60 characters."));
        }
        if(violations.size() > 0) {
            throw new ConstraintViolationException("Invalid Task", violations);
        }
    }
}
