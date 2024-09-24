package com.project.fitnessapp.repositories;

import com.project.fitnessapp.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByEmail(String email);

    @Query(value = "SELECT * FROM app_user WHERE email = :email AND password = :password", nativeQuery = true)
    AppUser findByEmailAndPasswordUnsafe(@Param("email") String email, @Param("password") String password);
}
