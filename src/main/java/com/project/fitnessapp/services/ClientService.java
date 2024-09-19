package com.project.fitnessapp.services;

import com.project.fitnessapp.models.Client;
import com.project.fitnessapp.models.FitnessProgram;
import com.project.fitnessapp.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    public Client getClient(Long id) {
        return clientRepository.findById(id).orElseThrow();
    }

    public List<FitnessProgram> getEnrolledPrograms(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow();
        return client.getEnrolledPrograms();
    }
}
