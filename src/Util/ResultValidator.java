package Util;

import Contracts.IValidation;
import Exceptions.ConstraintViolation;
import Exceptions.ConstraintViolationException;
import Models.Result;

import java.util.ArrayList;
import java.util.List;

public class ResultValidator implements IValidation<Result> {
    @Override
    public void validate(Result entity) throws ConstraintViolationException {
        List<ConstraintViolation> violations = new ArrayList<>();

        int feedbackLength = entity.getFeedback().trim().length();
        Integer gradeCapacity = entity.getGrade();

        if (feedbackLength<10 || feedbackLength>300){
            violations.add(new ConstraintViolation(entity.getClass().getName(),"feedback",entity.getFeedback(),
                    "Feedback length should be between 10 and 300 characters."));
        }
        if (gradeCapacity<0 || gradeCapacity>100){
            violations.add(new ConstraintViolation(entity.getClass().getName(),"grade",entity.getGrade(),
                    "Grade should be between 0 and 100 percent."));
        }
        if(violations.size() > 0) {
            throw new ConstraintViolationException("Invalid Result", violations);
        }
    }
}
