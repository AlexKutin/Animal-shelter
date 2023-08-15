package pro.sky.animalshelter.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.dto.CatAdopterListDTO;
import pro.sky.animalshelter.dto.CatDTO;
import pro.sky.animalshelter.dto.DogAdopterListDTO;
import pro.sky.animalshelter.dto.DogDTO;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnimalService {
    @Autowired
    private final DogsRepository dogsRepository;
    @Autowired
    private final CatsRepository catsRepository;
    @Autowired
    private final ShelterRepository shelterRepository;
    @Autowired
    private final DogAdopterListRepository dogAdopterListRepository;
    @Autowired
    private final CatAdopterListRepository catAdopterListRepository;
    @Autowired
    private final UserDogShelterRepository userDogShelterRepository;
    @Autowired
    private final UserCatShelterRepository userCatShelterRepository;
    public AnimalService(DogsRepository dogsRepository, CatsRepository catsRepository, ShelterRepository shelterRepository, DogAdopterListRepository dogAdopterListRepository, CatAdopterListRepository catAdopterListRepository, UserDogShelterRepository userDogShelterRepository, UserCatShelterRepository userCatShelterRepository) {
        this.dogsRepository = dogsRepository;
        this.catsRepository = catsRepository;
        this.shelterRepository = shelterRepository;
        this.dogAdopterListRepository = dogAdopterListRepository;
        this.catAdopterListRepository = catAdopterListRepository;
        this.userDogShelterRepository = userDogShelterRepository;
        this.userCatShelterRepository = userCatShelterRepository;
    }

    public DogDTO saveDogToDb(DogDTO dogDTO) {
        Dog dog =  Dog.fromDTO(dogDTO);
        List<Shelter> list = shelterRepository.findSheltersByShelterType(ShelterType.DOG_SHELTER);
        dog.setShelterId(list.get(0));
        dog = dogsRepository.save(dog);
        return dog.toDTO();
    }

    public CatDTO saveCatToDb(CatDTO catDTO) {
        Cat cat = Cat.fromDTO(catDTO);
        List<Shelter> list = shelterRepository.findSheltersByShelterType(ShelterType.CAT_SHELTER);
        cat.setShelterId(list.get(0));
        cat = catsRepository.save(cat);
        return cat.toDTO();
    }

    public DogAdopterListDTO saveDogAdopter(DogAdopterListDTO dogAdopterListDTO, Integer dogId, Integer adopterId) {
        DogAdopterList dogAdopterList = DogAdopterList.fromDTO(dogAdopterListDTO);
        dogAdopterList.setAdoptionDate(LocalDateTime.now());
        dogAdopterList.setDogId(dogsRepository.findById(dogId).get());
        dogAdopterList.setAdopterId(userDogShelterRepository.findById(adopterId).get());
        dogAdopterList = dogAdopterListRepository.save(dogAdopterList);
        return dogAdopterList.toDTO();
    }

    public CatAdopterListDTO saveCatAdopter(CatAdopterListDTO catAdopterListDTO, Integer catId, Integer adopterId) {
        CatAdopterList catAdopterList = CatAdopterList.fromDTO(catAdopterListDTO);
        catAdopterList.setAdoptionDate(LocalDateTime.now());
        catAdopterList.setCatId(catsRepository.findById(catId).get());
        catAdopterList.setAdopterId(userCatShelterRepository.findById(adopterId).get());
        catAdopterList = catAdopterListRepository.save(catAdopterList);
        return catAdopterList.toDTO();
    }
}
