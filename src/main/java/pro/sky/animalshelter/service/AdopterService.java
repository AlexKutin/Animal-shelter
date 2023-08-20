package pro.sky.animalshelter.service;

import org.springframework.stereotype.Service;
import pro.sky.animalshelter.exception.AdopterNotFoundException;
import pro.sky.animalshelter.exception.ShelterNotFoundException;
import pro.sky.animalshelter.model.CatAdopter;
import pro.sky.animalshelter.model.DogAdopter;
import pro.sky.animalshelter.repository.CatAdopterRepository;
import pro.sky.animalshelter.repository.DogAdopterRepository;

@Service
public class AdopterService {
    private final CatAdopterRepository catAdopterRepository;
    private final DogAdopterRepository dogAdopterRepository;

    public AdopterService(CatAdopterRepository catAdopterRepository, DogAdopterRepository dogAdopterRepository) {
        this.catAdopterRepository = catAdopterRepository;
        this.dogAdopterRepository = dogAdopterRepository;
    }

    public CatAdopter findCatAdopterById(Integer adopterId) {
        return catAdopterRepository.findById(adopterId)
                .orElseThrow(() -> new AdopterNotFoundException(
                        String.format("Adopter with id = %d not found in Cat Shelter database", adopterId)));
    }

    public DogAdopter findDogAdopterById(Integer adopterId) {
        return dogAdopterRepository.findById(adopterId)
                .orElseThrow(() -> new AdopterNotFoundException(
                        String.format("Adopter with id = %d not found in Dog Shelter database", adopterId)));
    }
}
