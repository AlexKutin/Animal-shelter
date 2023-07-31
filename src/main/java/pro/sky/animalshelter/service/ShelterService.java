package pro.sky.animalshelter.service;

import org.springframework.stereotype.Service;
import pro.sky.animalshelter.exception.ShelterNotFoundException;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.repository.ShelterRepository;

@Service
public class ShelterService {
    private final ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    /**
     * Поиск в списке приютов собак по типу DOG_SHELTER.
     * Т.к. по условию задачи такой приют один, то выбирается первый (и фактически единственный)
     * элемент из коллекции, полученной из репозитария.
     *
     * @return Возвращает экземпляр класса Shelter путем выборки из БД по типу приюта CAT_SHELTER
     */
    public Shelter getDogShelter() {
        return shelterRepository.findSheltersByShelterType(ShelterType.DOG_SHELTER)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ShelterNotFoundException("Dog shelter not found in database"));
    }

    /**
     * Поиск в списке приютов кошек по типу CAT_SHELTER.
     * Т.к. по условию задачи такой приют один, то выбирается первый (и фактически единственный)
     * элемент из коллекции, полученной из репозитария.
     *
     * @return Возвращает экземпляр класса Shelter путем выборки из БД по типу приюта CAT_SHELTER
     */
    public Shelter getCatShelter() {
        return shelterRepository.findSheltersByShelterType(ShelterType.CAT_SHELTER)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ShelterNotFoundException("Cat shelter not found in database"));
    }
}
