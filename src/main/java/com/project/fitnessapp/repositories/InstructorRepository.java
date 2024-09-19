package com.project.fitnessapp.repositories;

import com.project.fitnessapp.models.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}
