package com.project.fitnessapp.repositories;

import com.project.fitnessapp.models.AppUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class AppUserRepositoryVulnerable {

    @PersistenceContext
    private EntityManager entityManager;

    public AppUser findByEmailAndPasswordUnsafe(String email, String password) {
        String query = "SELECT * FROM app_user WHERE email = '" + email + "' AND password = '" + password + "'";
        return (AppUser) entityManager.createNativeQuery(query, AppUser.class).getSingleResult();
    }
}
