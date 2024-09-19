package com.project.fitnessapp.controllers;

import com.project.fitnessapp.models.Client;
import com.project.fitnessapp.services.ClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{clientId}")
    public Client getClient(@PathVariable Long clientId) {
        return clientService.getClient(clientId);
    }
}
