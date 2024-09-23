package com.project.fitnessapp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("INSTRUCTOR")
public class Instructor extends AppUser{

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FitnessProgram> fitnessPrograms = new ArrayList<>();

    public Instructor(String name, String email, String password) {
        super(name, email, password,Role.INSTRUCTOR);
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", fitnessProgramsCount=" + fitnessPrograms.size() +
                '}';
    }
}
