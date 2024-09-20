package com.project.fitnessapp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("CLIENT")
public class Client extends AppUser{

    @ManyToMany
    private List<FitnessProgram> enrolledPrograms=new ArrayList<>();

    public Client(String name, String email, String password) {
        super(name, email, password, Role.CLIENT);
    }

}
