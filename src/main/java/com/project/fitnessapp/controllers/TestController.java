package com.project.fitnessapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/cause-error")
    public String causeError() {
        // This will throw an ArithmeticException (division by zero)
        int result = 10 / 0;
        return "error-page";
    }
}
