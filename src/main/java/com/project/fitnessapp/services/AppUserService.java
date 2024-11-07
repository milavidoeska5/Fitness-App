package com.project.fitnessapp.services;

import com.project.fitnessapp.models.AppUser;
import com.project.fitnessapp.models.Client;
import com.project.fitnessapp.models.Instructor;
import com.project.fitnessapp.models.Role;
import com.project.fitnessapp.repositories.AppUserRepository;
import com.project.fitnessapp.repositories.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {
    @Autowired
    private AppUserRepository appUserRepository;

    public AppUser register(String name, String email, String password, Role role) {
        AppUser newUser;
        if(role == Role.INSTRUCTOR){
            newUser = new Instructor(name, email, password);
        }else if (role == Role.CLIENT){
            newUser = new Client(name, email, password);
        }else{
            return null;
        }
        return appUserRepository.save(newUser);
    }

    public AppUser findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public boolean isValidPassword(String password) {
        int minLength = 8;

        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{" + minLength + ",}$";

        return password != null && password.matches(passwordPattern);
    }

}
