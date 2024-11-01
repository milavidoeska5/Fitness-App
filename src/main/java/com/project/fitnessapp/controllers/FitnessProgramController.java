package com.project.fitnessapp.controllers;

import com.project.fitnessapp.models.AppUser;
import com.project.fitnessapp.models.Client;
import com.project.fitnessapp.models.FitnessProgram;
import com.project.fitnessapp.models.Instructor;
import com.project.fitnessapp.services.AppUserService;
import com.project.fitnessapp.services.ClientService;
import com.project.fitnessapp.services.FitnessProgramService;
import com.project.fitnessapp.services.InstructorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/programs")
public class FitnessProgramController {
    private final InstructorService instructorService;
    private final ClientService clientService;
    private final FitnessProgramService fitnessProgramService;
    private final AppUserService appUserService;

    public FitnessProgramController(InstructorService instructorService, ClientService clientService, FitnessProgramService fitnessProgramService, AppUserService appUserService) {
        this.instructorService = instructorService;
        this.clientService = clientService;
        this.fitnessProgramService = fitnessProgramService;
        this.appUserService = appUserService;
    }

    @GetMapping("/{clientId}")
    public String getAllPrograms(@PathVariable Long clientId, Model model) {
        verifyClientAccess(clientId);

        List<FitnessProgram> programs = fitnessProgramService.getAll();
        model.addAttribute("programs", programs);
        model.addAttribute("clientId", clientId);
        List<FitnessProgram> enrolledPrograms = clientService.getEnrolledPrograms(clientId);
        model.addAttribute("enrolledPrograms", enrolledPrograms);
        return "programs";
    }


    @GetMapping("/client-programs/{clientId}")
    public String getProgramsByClientId(@PathVariable Long clientId, Model model) {
        verifyClientAccess(clientId);

        List<FitnessProgram> clientPrograms = clientService.getEnrolledPrograms(clientId);
        model.addAttribute("programs", clientPrograms);
        model.addAttribute("clientId", clientId);
        return "client-programs";
    }

    @GetMapping("/instructor-programs/{instructorId}")
    public String getProgramsByInstructorId(@PathVariable Long instructorId, Model model) {
        verifyInstructorAccess(instructorId);

        List<FitnessProgram> instructorPrograms = instructorService.getFitnessPrograms(instructorId);
        model.addAttribute("programs", instructorPrograms);
        model.addAttribute("instructorId", instructorId);
        return "instructor-programs";
    }

    @GetMapping("/{instructorId}/addProgram")
    public String showAddProgramForm(@PathVariable Long instructorId, Model model) {
        verifyInstructorAccess(instructorId);

        Instructor instructor = instructorService.findById(instructorId);
        model.addAttribute("instructor", instructor);
        model.addAttribute("fitnessProgram", new FitnessProgram());  // Form-bound object
        return "addProgram";
    }

    @PostMapping("/{instructorId}/addProgram")
    public String addProgram(
            @PathVariable Long instructorId,
            @ModelAttribute FitnessProgram fitnessProgram, Model model) {
        verifyInstructorAccess(instructorId);

        fitnessProgramService.addProgram(instructorId, fitnessProgram);
        model.addAttribute("instructorId", instructorId);
        return "redirect:/programs/instructor-programs/" + instructorId;
    }

    @GetMapping("/client-info/{clientId}")
    public String getClientInfo(@PathVariable Long clientId, Model model) {
        isClientOfInstructor(clientId);

        Client client = clientService.getClient(clientId);
        model.addAttribute("client", client);
        return "client-info";
    }

    @PostMapping("/enroll")
    public String enrollInProgram(@RequestParam Long clientId, @RequestParam Long programId) {
        Client client = clientService.getClient(clientId);
        FitnessProgram program = fitnessProgramService.getById(programId);
        if(!clientService.getEnrolledPrograms(clientId).contains(program)){
            clientService.getEnrolledPrograms(clientId).add(program);
            clientService.addClient(client);
        }

        return "redirect:/programs/" + clientId;
    }

    @GetMapping("/fetch-data")
    public String showFetchDataForm() {
        return "fetch-data";
    }


    @PostMapping("/fetch-data")
    public String fetchData(@RequestParam("url") String url, Model model) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            model.addAttribute("data", response.getBody());
        } catch (Exception e) {
            model.addAttribute("error", "Error fetching data: " + e.getMessage());
        }

        return "fetch-data";
    }

    private void verifyInstructorAccess(Long instructorId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser currentUser = appUserService.findByEmail(userDetails.getUsername());

        if (!"INSTRUCTOR".equals(currentUser.getRole().toString()) || !currentUser.getId().equals(instructorId)) {
            throw new AccessDeniedException("Access is denied.");
        }
    }

    private void verifyClientAccess(Long clientId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser currentUser = appUserService.findByEmail(userDetails.getUsername());

        if (!"CLIENT".equals(currentUser.getRole().toString()) || !currentUser.getId().equals(clientId)) {
            throw new AccessDeniedException("Access is denied.");
        }
    }

    private void isClientOfInstructor(Long clientId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser currentUser = appUserService.findByEmail(userDetails.getUsername());
        Long instructorId= currentUser.getId();

        if (!"INSTRUCTOR".equals(currentUser.getRole().toString()) || !instructorService.isClientOfInstructor(instructorId,clientId)) {
            throw new AccessDeniedException("Access is denied.");
        }

    }


}
