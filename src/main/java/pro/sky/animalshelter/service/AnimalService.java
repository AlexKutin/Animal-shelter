package pro.sky.animalshelter.service;

import org.springframework.stereotype.Service;
import pro.sky.animalshelter.dto.*;
import pro.sky.animalshelter.exception.AnimalNotFoundException;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalService {
    private final ShelterService shelterService;
    private final UserShelterService userShelterService;

    private final DogsRepository dogsRepository;
    private final CatsRepository catsRepository;
    private final ShelterRepository shelterRepository;
    private final DogAdopterRepository dogAdopterRepository;
    private final CatAdopterListRepository catAdopterListRepository;
    private final UserDogShelterRepository userDogShelterRepository;
    private final UserCatShelterRepository userCatShelterRepository;

    public AnimalService(ShelterService shelterService, UserShelterService userShelterService, DogsRepository dogsRepository, CatsRepository catsRepository, ShelterRepository shelterRepository, DogAdopterRepository dogAdopterRepository, CatAdopterListRepository catAdopterListRepository, UserDogShelterRepository userDogShelterRepository, UserCatShelterRepository userCatShelterRepository) {
        this.shelterService = shelterService;
        this.userShelterService = userShelterService;
        this.dogsRepository = dogsRepository;
        this.catsRepository = catsRepository;
        this.shelterRepository = shelterRepository;
        this.dogAdopterRepository = dogAdopterRepository;
        this.catAdopterListRepository = catAdopterListRepository;
        this.userDogShelterRepository = userDogShelterRepository;
        this.userCatShelterRepository = userCatShelterRepository;
    }

    public DogDTO saveDogToDb(DogDTO dogDTO) {
        Dog dog =  Dog.fromDTO(dogDTO);
        Shelter dogShelter = shelterService.findShelterByShelterType(ShelterType.DOG_SHELTER);
        dog.setShelter(dogShelter);

//        List<Shelter> list = shelterRepository.findSheltersByShelterType(ShelterType.DOG_SHELTER);
//        dog.setShelter(list.get(0));
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

//    public DogAdopterDTO saveDogAdopter(DogAdopterDTO dogAdopterDTO, Integer dogId, Integer adopterId) {
//        DogAdopter dogAdopter = DogAdopter.fromDTO(dogAdopterDTO);
//        dogAdopter.setAdoptionDate(LocalDateTime.now());
//        dogAdopter.setDog(dogsRepository.findById(dogId).get());
//        dogAdopter.setAdopter(userDogShelterRepository.findById(adopterId).get());
//        dogAdopter = dogAdopterListRepository.save(dogAdopter);
//        return dogAdopter.toDTO();
//    }

    public AnimalAdopterDTO saveDogAdopter(AnimalAdopterDTO animalAdopterDTO) {
        DogAdopter dogAdopter = new DogAdopter();
        dogAdopter.setDog(findDogById(animalAdopterDTO.getAnimalId()));
        dogAdopter.setUser(userShelterService.findUserDogShelterById(animalAdopterDTO.getUserId()));
        dogAdopter.setAdoptionDate(LocalDateTime.now());
        dogAdopter.setAdopterStatus(animalAdopterDTO.getAdopterStatus());
        dogAdopter.setEndProbationDate(LocalDateTime.now().plusMonths(1));
        dogAdopter = dogAdopterRepository.save(dogAdopter);
        return dogAdopter.toDTO();
    }

    public CatAdopterListDTO saveCatAdopter(CatAdopterListDTO catAdopterListDTO, Integer catId, Integer adopterId) {
        CatAdopterList catAdopterList = CatAdopterList.fromDTO(catAdopterListDTO);
        catAdopterList.setAdoptionDate(LocalDateTime.now());
        catAdopterList.setCatId(catsRepository.findById(catId).get());
        catAdopterList.setAdopterId(userCatShelterRepository.findById(adopterId).get());
        catAdopterList = catAdopterListRepository.save(catAdopterList);
        return catAdopterList.toDTO();
    }

    public List<AnimalAdopterDTO> getDogAdopters(AdopterStatus adopterStatus) {
        List<DogAdopter> dogAdopters = dogAdopterRepository.findAllByAdopterStatusOrderByUser(adopterStatus);
        return dogAdopters
                .stream()
                .map(DogAdopter::toDTO)
                .collect(Collectors.toList());
    }

    private Cat findCatById(Integer animalId) {
        return catsRepository.findById(animalId)
                .orElseThrow(() -> new AnimalNotFoundException(animalId + " not found in database"));
    }

    private Dog findDogById(Integer animalId) {
        return dogsRepository.findById(animalId)
                .orElseThrow(() -> new AnimalNotFoundException(animalId + " not found in database"));
    }


}
