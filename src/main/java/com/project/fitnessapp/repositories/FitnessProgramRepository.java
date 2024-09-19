package com.project.fitnessapp.repositories;

import com.project.fitnessapp.models.FitnessProgram;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FitnessProgramRepository extends JpaRepository<FitnessProgram, Long> {
}
