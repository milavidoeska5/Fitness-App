package com.project.fitnessapp.controllers;

import com.project.fitnessapp.models.AppUser;
import com.project.fitnessapp.models.Role;
import com.project.fitnessapp.services.AppUserService;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @Autowired
    private AppUserService appUserService;

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
    public String register(@RequestParam String name, @RequestParam String username,
                           @RequestParam String password, @RequestParam Role role, Model model) {

        if (!appUserService.isValidPassword(password)) {
            model.addAttribute("error",
                    "Password must be at least 8 characters long and include an uppercase letter," +
                            " a lowercase letter, a number, and a special character.");
            return "register";
        }

        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        String encodedPassword= passwordEncoder.encode(password);
        AppUser newUser = appUserService.register(name, username, encodedPassword, role);
        model.addAttribute("user", newUser);

        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }


}
