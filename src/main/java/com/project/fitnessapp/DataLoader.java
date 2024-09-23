package com.project.fitnessapp;

import com.project.fitnessapp.models.Client;
import com.project.fitnessapp.models.FitnessProgram;
import com.project.fitnessapp.models.Instructor;
import com.project.fitnessapp.models.Role;
import com.project.fitnessapp.repositories.ClientRepository;
import com.project.fitnessapp.repositories.FitnessProgramRepository;
import com.project.fitnessapp.repositories.InstructorRepository;
import com.project.fitnessapp.services.ClientService;
import com.project.fitnessapp.services.FitnessProgramService;
import com.project.fitnessapp.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private FitnessProgramRepository fitnessProgramRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create instructors
        Instructor instructor1 = new Instructor("Mila Vidoeska","mv@gmail.com", "admin123");
        Instructor instructor2 = new Instructor("Mihaela Trajkovska", "mt@gmail.com", "admin123");

        // Save instructors
        instructorRepository.save(instructor1);
        instructorRepository.save(instructor2);

        // Create fitness programs for each instructor
        FitnessProgram program1 = new FitnessProgram("Yoga for Beginners", "A beginner-level yoga class", instructor1);
        FitnessProgram program2 = new FitnessProgram("Advanced HIIT", "A high-intensity interval training program", instructor2);

        // Save fitness programs
        fitnessProgramRepository.save(program1);
        fitnessProgramRepository.save(program2);

        // Create clients and enroll them in programs
        Client client1 = new Client("Klient 1", "klient1@gmail.com", "user123");
        Client client2 = new Client("Klient 2", "klient2@gmail.com", "user123");

        client1.getEnrolledPrograms().add(program1);
        client2.getEnrolledPrograms().add(program2);

        clientRepository.save(client1);
        clientRepository.save(client2);
    }
}
