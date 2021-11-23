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

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "NotEmpty.userForm.userName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userFirstName", "NotEmpty.userForm.userFirstName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userLastName", "NotEmpty.userForm.userLastName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.userForm.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.userForm.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "NotEmpty.userForm.confirmPassword");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", "NotEmpty.userForm.gender");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "countryCode", "NotEmpty.userForm.countryCode");

        if (!emailValidator.isValid(userForm.getEmail())) {
            errors.rejectValue("email", "Pattern.userForm.email");
        } else if (userForm.getUserId() == null) {
            User dbUser = userDAO.findUserByEmail(userForm.getEmail());
            if (dbUser != null) {
                errors.rejectValue("email", "Duplicate.userForm.email");
            }
        }

        if (!errors.hasFieldErrors("userName")) {
            User dbUser = userDAO.findUserByUserName(userForm.getUserName());
            if (dbUser != null) {
                errors.rejectValue("userName", "Duplicate.userForm.userName");
            }
        }

        if (!errors.hasErrors()) {
            if (!userForm.getConfirmPassword().equals(userForm.getPassword())) {
                errors.rejectValue("confirmPassword", "Match.userForm.confirmPassword");
            }
        }
    }
}
