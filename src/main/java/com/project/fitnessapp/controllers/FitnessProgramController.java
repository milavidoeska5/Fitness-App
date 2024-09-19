package com.project.fitnessapp.controllers;

import com.project.fitnessapp.models.FitnessProgram;
import com.project.fitnessapp.services.ClientService;
import com.project.fitnessapp.services.FitnessProgramService;
import com.project.fitnessapp.services.InstructorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/programs")
public class FitnessProgramController {
    private final InstructorService instructorService;
    private final ClientService clientService;
    private final FitnessProgramService fitnessProgramService;

    public FitnessProgramController(InstructorService instructorService, ClientService clientService, FitnessProgramService fitnessProgramService) {
        this.instructorService = instructorService;
        this.clientService = clientService;
        this.fitnessProgramService = fitnessProgramService;
    }

    @GetMapping
    public List<FitnessProgram> getAllPrograms() {
        return fitnessProgramService.getAll();
    }

    @GetMapping("/{clientId}")
    public List<FitnessProgram> getProgramsByClientId(@PathVariable Long clientId) {
        return clientService.getEnrolledPrograms(clientId);
    }

    @GetMapping("/{instructorId}")
    public List<FitnessProgram> getProgramsByInstructorId(@PathVariable Long instructorId) {
        return instructorService.getFitnessPrograms(instructorId);
    }

    @PostMapping("/{instructorId}/addProgram")
    public ResponseEntity<FitnessProgram> addProgram(
            @PathVariable Long instructorId,
            @RequestBody FitnessProgram fitnessProgram) {

        FitnessProgram createdProgram = instructorService.addFitnessProgram(instructorId, fitnessProgram);
        return ResponseEntity.ok(createdProgram);
    }
}
