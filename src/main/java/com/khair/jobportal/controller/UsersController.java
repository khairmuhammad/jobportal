package com.khair.jobportal.controller;

import com.khair.jobportal.entity.Users;
import com.khair.jobportal.entity.UsersType;
import com.khair.jobportal.services.UsersService;
import com.khair.jobportal.services.UsersTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class UsersController {

    private final UsersTypeService usersTypeService;
    private final UsersService usersService;

    public UsersController(UsersTypeService usersTypeService, UsersService usersService) {
        this.usersTypeService = usersTypeService;
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        List<UsersType> usersTypes = usersTypeService.getAllUsersTypes();
        model.addAttribute("getAllTypes", usersTypes);
        model.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping("/register/new")
    public String userRegistration(@Valid Users users, Model model) {

        Optional<Users> optionalUsers = usersService.getUserByEmail(users.getEmail());
        if (optionalUsers.isPresent()) {
            model.addAttribute("error", "Email already registered, try to login or register with other email.");
            List<UsersType> usersTypes = usersTypeService.getAllUsersTypes();
            model.addAttribute("getAllTypes", usersTypes);
            model.addAttribute("user", new Users());
            return "register";
        }
        usersService.addNewUser(users);
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.createEmptyContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/";
    }

}
