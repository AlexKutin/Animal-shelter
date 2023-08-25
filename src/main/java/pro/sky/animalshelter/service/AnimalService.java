package pro.sky.animalshelter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.dto.*;
import pro.sky.animalshelter.exception.AnimalNotFoundException;
import pro.sky.animalshelter.exception.DuplicateAdopterException;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AnimalService {
    private  final Logger logger = LoggerFactory.getLogger(AnimalService.class);
    private final ShelterService shelterService;
    private final UserShelterService userShelterService;

    private final DogsRepository dogsRepository;
    private final CatsRepository catsRepository;
    private final ShelterRepository shelterRepository;
    private final DogAdopterRepository dogAdopterRepository;
    private final CatAdopterRepository catAdopterRepository;

    public AnimalService(ShelterService shelterService, UserShelterService userShelterService, DogsRepository dogsRepository, CatsRepository catsRepository, ShelterRepository shelterRepository, DogAdopterRepository dogAdopterRepository, CatAdopterRepository catAdopterRepository) {
        this.shelterService = shelterService;
        this.userShelterService = userShelterService;
        this.dogsRepository = dogsRepository;
        this.catsRepository = catsRepository;
        this.shelterRepository = shelterRepository;
        this.dogAdopterRepository = dogAdopterRepository;
        this.catAdopterRepository = catAdopterRepository;
    }

    public DogDTO saveDogToDb(DogDTO dogDTO) {
        Dog dog =  Dog.fromDTO(dogDTO);
        Shelter dogShelter = shelterService.findShelterByShelterType(ShelterType.DOG_SHELTER);
        dog.setShelter(dogShelter);
        dog = dogsRepository.save(dog);
        return dog.toDTO();
    }

    public List<DogDTO> getAllDogs() {
        return dogsRepository.findAll()
                .stream()
                .map(Dog::toDTO)
                .collect(Collectors.toList());
    }

    public List<CatDTO> getAllCats() {
        return catsRepository.findAll()
                .stream()
                .map(Cat::toDTO)
                .collect(Collectors.toList());
    }

    public CatDTO saveCatToDb(CatDTO catDTO) {
        Cat cat = Cat.fromDTO(catDTO);
        List<Shelter> list = shelterRepository.findSheltersByShelterType(ShelterType.CAT_SHELTER);
        cat.setShelterId(list.get(0));
        cat = catsRepository.save(cat);
        return cat.toDTO();
    }

    public AnimalAdopterDTO saveDogAdopter(AnimalAdopterDTO animalAdopterDTO) {
        Integer userId = animalAdopterDTO.getUserId();
        Integer dogId = animalAdopterDTO.getAnimalId();
        if (dogAdopterRepository.isPresentDogAdopterByUserAndDog(userId, dogId)) {
            logger.warn("Dog Shelter: Adopter duplication error: userId = {} and catId = {} already present", userId, dogId);
            throw new DuplicateAdopterException(
                    String.format("Dog Shelter: Adopter duplication error: userId = %d and dogId = %d already present", userId, dogId));
        }
        DogAdopter dogAdopter = new DogAdopter();
        dogAdopter.setDog(findDogById(dogId));
        dogAdopter.setUser(userShelterService.findUserDogShelterById(userId));
        dogAdopter.setAdoptionDate(LocalDateTime.now());
        dogAdopter.setAdopterStatus(animalAdopterDTO.getAdopterStatus());
        dogAdopter.setEndProbationDate(LocalDateTime.now().plusMonths(1));
        dogAdopter = dogAdopterRepository.save(dogAdopter);
        return dogAdopter.toDTO();
    }

    public AnimalAdopterDTO saveCatAdopter(AnimalAdopterDTO animalAdopterDTO) {
        Integer userId = animalAdopterDTO.getUserId();
        Integer catId = animalAdopterDTO.getAnimalId();
        if (catAdopterRepository.isPresentCatAdopterByUserAndCat(userId, catId)) {
            logger.warn("Cat Shelter: Adopter duplication error: userId = {} and catId = {} already present", userId, catId);
            throw new DuplicateAdopterException(
                    String.format("Cat Shelter: Adopter duplication error: userId = %d and catId = %d already present", userId, catId));
        }
        CatAdopter catAdopter = new CatAdopter();
        catAdopter.setCat(findCatById(catId));
        catAdopter.setUser(userShelterService.findUserCatShelterById(userId));
        catAdopter.setAdoptionDate(LocalDateTime.now());
        catAdopter.setAdopterStatus(animalAdopterDTO.getAdopterStatus());
        catAdopter.setEndProbationDate(LocalDateTime.now().plusMonths(1));
        catAdopter = catAdopterRepository.save(catAdopter);

        return catAdopter.toDTO();
    }

    public List<AnimalAdopterDTO> getDogAdopters(AdopterStatus adopterStatus) {
        List<DogAdopter> dogAdopters;
        if (Objects.nonNull(adopterStatus)) {
            dogAdopters = dogAdopterRepository.findAllByAdopterStatusOrderByUser(adopterStatus);
        } else  {
            dogAdopters = dogAdopterRepository.findAll();
        }
        return dogAdopters
                .stream()
                .map(DogAdopter::toDTO)
                .collect(Collectors.toList());
    }

    public List<AnimalAdopterDTO> getCatAdopters(AdopterStatus adopterStatus) {
        List<CatAdopter> catAdopters;
        if (Objects.nonNull(adopterStatus)) {
            catAdopters = catAdopterRepository.findAllByAdopterStatusOrderByUser(adopterStatus);
        } else  {
            catAdopters = catAdopterRepository.findAll();
        }
        return catAdopters
                .stream()
                .map(CatAdopter::toDTO)
                .collect(Collectors.toList());
    }

    private Cat findCatById(Integer animalId) {
        return catsRepository.findById(animalId)
                .orElseThrow(() -> new AnimalNotFoundException("Cat with id = " + animalId + " not found in database"));
    }

    private Dog findDogById(Integer animalId) {
        return dogsRepository.findById(animalId)
                .orElseThrow(() -> new AnimalNotFoundException("Dog with id = " + animalId + " not found in database"));
    }
}
