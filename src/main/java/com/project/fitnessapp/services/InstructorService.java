package com.project.fitnessapp.services;

import com.project.fitnessapp.models.Instructor;
import com.project.fitnessapp.repositories.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructorService {
    @Autowired
    private InstructorRepository instructorRepository;

    public List<Instructor> findAll() {
        return instructorRepository.findAll();
    }
}
