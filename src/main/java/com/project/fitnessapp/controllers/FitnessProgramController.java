package com.project.fitnessapp.controllers;

import com.project.fitnessapp.models.FitnessProgram;
import com.project.fitnessapp.models.Instructor;
import com.project.fitnessapp.services.ClientService;
import com.project.fitnessapp.services.FitnessProgramService;
import com.project.fitnessapp.services.InstructorService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
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
    public String getAllPrograms(Model model) {
        List<FitnessProgram> programs = fitnessProgramService.getAll();
        model.addAttribute("programs", programs);
        return "programs";
    }

    @GetMapping("/{clientId}")
    public String getProgramsByClientId(@PathVariable Long clientId, Model model) {
        List<FitnessProgram> clientPrograms = clientService.getEnrolledPrograms(clientId);
        model.addAttribute("programs", clientPrograms);
        model.addAttribute("clientId", clientId);
        return "client-programs";
    }

    @GetMapping("/{instructorId}")
    public String getProgramsByInstructorId(@PathVariable Long instructorId, Model model) {
        List<FitnessProgram> instructorPrograms = instructorService.getFitnessPrograms(instructorId);
        model.addAttribute("programs", instructorPrograms);
        model.addAttribute("instructorId", instructorId);
        return "instructor-programs";
    }

    @GetMapping("/{instructorId}/addProgram")
    public String showAddProgramForm(@PathVariable Long instructorId, Model model) {
        Instructor instructor = instructorService.findById(instructorId);
        model.addAttribute("instructor", instructor);
        model.addAttribute("fitnessProgram", new FitnessProgram());
        return "addProgram";
    }

    @PostMapping("/{instructorId}/addProgram")
    public ResponseEntity<FitnessProgram> addProgram(
            @PathVariable Long instructorId,
            @RequestBody FitnessProgram fitnessProgram) {

        FitnessProgram createdProgram = fitnessProgramService.addProgram(instructorId, fitnessProgram);
        return ResponseEntity.ok(createdProgram);
    }
}