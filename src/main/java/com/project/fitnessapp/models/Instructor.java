package com.project.fitnessapp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("INSTRUCTOR")
public class Instructor extends User{

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FitnessProgram> fitnessPrograms = new ArrayList<>();

    public Instructor(String name, String email, String password) {
        super(name, email, password);
    }

}
