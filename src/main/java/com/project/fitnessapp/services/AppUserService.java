package com.project.fitnessapp.services;

import com.project.fitnessapp.models.AppUser;
import com.project.fitnessapp.models.Client;
import com.project.fitnessapp.models.Instructor;
import com.project.fitnessapp.models.Role;
import com.project.fitnessapp.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {
    @Autowired
    private AppUserRepository appUserRepository;

    public AppUser findByEmailAndPasswordUnsafe(String email,String password) {
        return appUserRepository.findByEmailAndPasswordUnsafe(email, password);
    }

    public AppUser register(String name, String email, String password, Role role) {
        if(role == Role.INSTRUCTOR){
            Instructor instructor = new Instructor(name, email, password);
            return appUserRepository.save(instructor);
        }else if (role == Role.CLIENT){
            Client client = new Client(name, email, password);
            return appUserRepository.save(client);
        }
        return null;
    }
}
