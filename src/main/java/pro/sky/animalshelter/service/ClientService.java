package pro.sky.animalshelter.service;

import org.springframework.stereotype.Service;
import pro.sky.animalshelter.enity.Client;
import pro.sky.animalshelter.repository.ClientRepository;

import java.util.List;

public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    public Client getById (Long id){
        return clientRepository.findById(id).orElse(null);
    }

    public List<Client> getClientsByShelterId (Long shelterId){
        return clientRepository.findAllByShelter_ShelterId(shelterId);
    }
}
