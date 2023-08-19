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

    /**
     * Сохраняет иформацию о питомце приюта для собак в таблицу dogs_list.
     * @return DogDTO
     */
    public DogDTO saveDogToDb(DogDTO dogDTO) {
        Dog dog =  Dog.fromDTO(dogDTO);
        List<Shelter> list = shelterRepository.findSheltersByShelterType(ShelterType.DOG_SHELTER);
        dog.setShelterId(list.get(0));
        dog = dogsRepository.save(dog);
        return dog.toDTO();
    }
    /**
     * Сохраняет иформацию о питомце приюта для кошек в таблицу cats_list.
     * @return CatDTO
     */
    public CatDTO saveCatToDb(CatDTO catDTO) {
        Cat cat = Cat.fromDTO(catDTO);
        List<Shelter> list = shelterRepository.findSheltersByShelterType(ShelterType.CAT_SHELTER);
        cat.setShelterId(list.get(0));
        cat = catsRepository.save(cat);
        return cat.toDTO();
    }

    /**
     * Сохраняет иформацию о питомце приюта для собак в таблицу dogs_list.
     * @return DogAdopterListDTO
     * @throws java.util.NoSuchElementException если переданы некорректные dogId или adopterId
     */
    public DogAdopterListDTO saveDogAdopter(DogAdopterListDTO dogAdopterListDTO, Integer dogId, Integer adopterId) {
        DogAdopterList dogAdopterList = DogAdopterList.fromDTO(dogAdopterListDTO);
        dogAdopterList.setAdoptionDate(LocalDateTime.now());
        dogAdopterList.setDogId(dogsRepository.findById(dogId).orElseThrow());
        dogAdopterList.setAdopterId(userDogShelterRepository.findById(adopterId).orElseThrow());
        dogAdopterList = dogAdopterListRepository.save(dogAdopterList);
        return dogAdopterList.toDTO();
    }
    /**
     * Сохраняет иформацию о питомце приюта для кошек в таблицу cats_list.
     * @return CatAdopterListDTO
     * @throws java.util.NoSuchElementException если переданы некорректные catId или adopterId
     */
    public CatAdopterListDTO saveCatAdopter(CatAdopterListDTO catAdopterListDTO, Integer catId, Integer adopterId) {
        CatAdopterList catAdopterList = CatAdopterList.fromDTO(catAdopterListDTO);
        catAdopterList.setAdoptionDate(LocalDateTime.now());
        catAdopterList.setCatId(catsRepository.findById(catId).orElseThrow());
        catAdopterList.setAdopterId(userCatShelterRepository.findById(adopterId).orElseThrow());
        catAdopterList = catAdopterListRepository.save(catAdopterList);
        return catAdopterList.toDTO();
    }
}
