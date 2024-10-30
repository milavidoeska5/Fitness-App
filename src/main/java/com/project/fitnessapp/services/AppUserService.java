package com.project.fitnessapp.services;

import com.project.fitnessapp.models.AppUser;
import com.project.fitnessapp.models.Client;
import com.project.fitnessapp.models.Instructor;
import com.project.fitnessapp.models.Role;
import com.project.fitnessapp.repositories.AppUserRepository;
import com.project.fitnessapp.repositories.AppUserRepositoryVulnerable;
import com.project.fitnessapp.repositories.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppUserRepositoryVulnerable appUserRepositoryVulnerable;
    @Autowired
    private InstructorRepository instructorRepository;

    public AppUser findByEmailAndPasswordUnsafe(String email,String password) {
        return appUserRepositoryVulnerable.findByEmailAndPasswordUnsafe(email, password);
    }

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

    public AppUser findByEmailUnsafe(String email) {
        return appUserRepositoryVulnerable.findByEmailUnsafe(email);
    }

    public AppUser findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }
}
