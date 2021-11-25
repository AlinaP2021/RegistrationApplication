package sbregapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sbregapp.dao.CountryDAO;
import sbregapp.dao.UserDAO;
import sbregapp.form.UserForm;
import sbregapp.model.Country;
import sbregapp.model.User;
import sbregapp.validator.UserValidator;

import javax.validation.Valid;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private CountryDAO countryDAO;

    @Autowired
    private UserValidator userValidator;

    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);
        if (target.getClass() == UserForm.class) {
            dataBinder.setValidator(userValidator);
        }
    }

    @GetMapping("/users")
    public String viewUsers(Model model) {
        List<User> list = userDAO.getUsers();
        model.addAttribute("users", list);
        return "usersPage";
    }

    @GetMapping("/registerSuccessful")
    public String viewRegisterSuccessful(Model model) {
        return "registerSuccessfulPage";
    }

    @GetMapping("/register")
    public String viewRegister(Model model) {
        UserForm form = new UserForm();
        List<Country> countries = countryDAO.getCountries();
        model.addAttribute("userForm", form);
        model.addAttribute("countries", countries);
        return "registerPage";
    }

    @PostMapping("/register")
    public String saveRegister(Model model,
                               @ModelAttribute("userForm") @Valid UserForm userForm,
                               BindingResult result,
                               final RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            List<Country> countries = countryDAO.getCountries();
            model.addAttribute("countries", countries);
            return "registerPage";
        }
        User newUser;
        try {
            newUser = userDAO.createUser(userForm);
        }
        catch (Exception e) {
            List<Country> countries = countryDAO.getCountries();
            model.addAttribute("countries", countries);
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "registerPage";
        }

        redirectAttributes.addFlashAttribute("flashUser", newUser);

        return "redirect:/registerSuccessful";
    }
}
