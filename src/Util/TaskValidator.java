package Util;

import Contracts.IValidation;
import Exceptions.ConstraintViolation;
import Exceptions.ConstraintViolationException;
import Models.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskValidator implements IValidation<Task> {
    @Override
    public void validate(Task entity) throws ConstraintViolationException {
        List<ConstraintViolation> violations = new ArrayList<>();

        var nameLength = entity.getName().trim().length();

        if (nameLength<5 || nameLength>30){
            violations.add(new ConstraintViolation(entity.getClass().getName(),"name",entity.getName(),
                    "Name length should be between 5 and 30 characters."));
        }
        if(violations.size() > 0) {
            throw new ConstraintViolationException("Invalid Task", violations);
        }
    }
}
