package sbregapp.validator;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import sbregapp.dao.UserDAO;
import sbregapp.form.UserForm;
import sbregapp.model.User;


@Component
public class UserValidator implements Validator {

    private EmailValidator emailValidator = EmailValidator.getInstance();

    @Autowired
    private UserDAO userDAO;


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == UserForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserForm userForm = (UserForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "notEmpty.userForm.userName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userFirstName", "notEmpty.userForm.userFirstName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userLastName", "notEmpty.userForm.userLastName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "notEmpty.userForm.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "notEmpty.userForm.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "notEmpty.userForm.confirmPassword");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", "notEmpty.userForm.gender");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "countryCode", "notEmpty.userForm.countryCode");

        if (!emailValidator.isValid(userForm.getEmail())) {
            errors.rejectValue("email", "pattern.userForm.email");
        } else if (userForm.getUserId() == null) {
            User dbUser = userDAO.findUserByEmail(userForm.getEmail());
            if (dbUser != null) {
                errors.rejectValue("email", "duplicate.userForm.email");
            }
        }

        if (!errors.hasFieldErrors("userName")) {
            User dbUser = userDAO.findUserByUserName(userForm.getUserName());
            if (dbUser != null) {
                errors.rejectValue("userName", "duplicate.userForm.userName");
            }
        }

        if (!errors.hasErrors()) {
            if (!userForm.getConfirmPassword().equals(userForm.getPassword())) {
                errors.rejectValue("confirmPassword", "match.userForm.confirmPassword");
            }
        }
    }
}
