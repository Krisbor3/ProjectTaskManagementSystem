package Util;

import Contracts.IValidation;
import Exceptions.ConstraintViolation;
import Exceptions.ConstraintViolationException;
import Exceptions.InvalidEntityDataException;
import Models.Users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator implements IValidation<User> {
    private String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$";

    @Override
    public void validate(User entity) throws ConstraintViolationException {
        List<ConstraintViolation> violations = new ArrayList<>();

        var firstNameLength = entity.getFirstName().trim().length();
        var lastNameLength = entity.getLastName().trim().length();
        var usernameLength = entity.getUsername().trim().length();
        if (firstNameLength<2 || firstNameLength>15){
            violations.add(new ConstraintViolation(entity.getClass().getName(),"firstName",entity.getFirstName(),
                    "First Name length should be between 2 and 15 characters."));
        }
        if (lastNameLength<2 || lastNameLength>15){
            violations.add(new ConstraintViolation(entity.getClass().getName(),"lastName",entity.getLastName(),
                    "Last Name length should be between 2 and 15 characters."));
        }
        if (usernameLength<2 || usernameLength>15){
            violations.add(new ConstraintViolation(entity.getClass().getName(),"username",entity.getUsername(),
                    "Username length should be between 2 and 15 characters."));
        }
        //Validation for the password
        Pattern pattern = Pattern.compile(passwordRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(entity.getPassword());
        boolean matchFound = matcher.find();
        if(!matchFound) {
            violations.add(new ConstraintViolation(entity.getClass().getName(),"password",entity.getPassword(),
                    "Password must be string 8 to 15 characters long, at least one digit, one capital letter, and one sign different than letter or digit"));
        }
        if(violations.size() > 0) {
            throw new ConstraintViolationException("Invalid User", violations);
        }
    }
}
