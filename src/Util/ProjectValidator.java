package Util;

import Contracts.IValidation;
import Exceptions.ConstraintViolation;
import Exceptions.ConstraintViolationException;
import Models.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectValidator implements IValidation<Project> {
    @Override
    public void validate(Project entity) throws ConstraintViolationException {
        List<ConstraintViolation> violations = new ArrayList<>();
        int nameLength = entity.getTitle().trim().length();
        if (nameLength<5 || nameLength>100){
            violations.add(new ConstraintViolation(entity.getClass().getName(),"title",entity.getTitle(),
                    "Project Name length should be between 5 and 100 characters."));
        }
        if(violations.size() > 0) {
            throw new ConstraintViolationException("Invalid User", violations);
        }
    }
}
