package com.project.fitnessapp.services;

import com.project.fitnessapp.models.FitnessProgram;
import com.project.fitnessapp.models.Instructor;
import com.project.fitnessapp.repositories.FitnessProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FitnessProgramService {
    @Autowired
    FitnessProgramRepository fitnessProgramRepository;
    @Autowired
    InstructorService instructorService;

    public List<FitnessProgram> getAll() {
        return fitnessProgramRepository.findAll();
    }

    public FitnessProgram addProgram(Long instructorId, FitnessProgram fitnessProgram) {
        Instructor instructor = instructorService.findById(instructorId);
        fitnessProgram.setInstructor(instructor);
        return fitnessProgramRepository.save(fitnessProgram);
    }

}
