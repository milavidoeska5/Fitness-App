package com.project.fitnessapp.controllers;

import com.project.fitnessapp.models.AppUser;
import com.project.fitnessapp.models.Role;
import com.project.fitnessapp.services.AppUserService;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @Autowired
    private AppUserService appUserService;
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String home() {
        return "homepage";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email,
                           @RequestParam String password, @RequestParam Role role, Model model) {

        AppUser newUser = appUserService.register(name, email, password, role);

        if (newUser.getRole() == Role.INSTRUCTOR) {
            Long instructorId = newUser.getId();
            return "redirect:/programs/instructor-programs/" + instructorId;
        } else if (newUser.getRole() == Role.CLIENT) {
            Long clientId = newUser.getId();
            return "redirect:/programs?clientId=" + clientId;
        }

        return "homepage";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }


}
