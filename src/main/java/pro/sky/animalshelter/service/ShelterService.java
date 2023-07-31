package pro.sky.animalshelter.service;

import dto.ShelterDTO;
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
     * @return Возвращает экземпляр класса ShelterDTO путем выборки из БД по типу приюта CAT_SHELTER
     */
    public ShelterDTO getDogShelter() {
        Shelter shelter = shelterRepository.findSheltersByShelterType(ShelterType.DOG_SHELTER)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ShelterNotFoundException("Dog shelter not found in database"));
        return ShelterDTO.fromShelter(shelter);
    }

    /**
     * Поиск в списке приютов кошек по типу CAT_SHELTER.
     * Т.к. по условию задачи такой приют один, то выбирается первый (и фактически единственный)
     * элемент из коллекции, полученной из репозитария.
     *
     * @return Возвращает экземпляр класса ShelterDTO путем выборки из БД по типу приюта CAT_SHELTER
     */
    public ShelterDTO getCatShelter() {
        Shelter shelter = shelterRepository.findSheltersByShelterType(ShelterType.CAT_SHELTER)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ShelterNotFoundException("Cat shelter not found in database"));
        return ShelterDTO.fromShelter(shelter);
    }

    /**
     * Поиск в списке приютов указанного типа.
     * Т.к. по условию задачи приюты разного типа присутствуют в единственном экземпляре,
     * то выбирается первый (и фактически единственный) элемент из коллекции, полученной из репозитария.
     * @param shelterType Тип приюта
     * @return Возвращает экземпляр класса <b>ShelterDTO</b> путем выборки из БД по типу приюта <b>shelterType</b>
     */
    public ShelterDTO getShelterByShelterType(ShelterType shelterType) {
        Shelter shelter = findShelterByShelterType(shelterType);
        return ShelterDTO.fromShelter(shelter);
    }

    /**
     * Редактирует информацию о выбранном приюте. Поля id и тип приюта (shelterType) изменять нельзя.
     * @param shelterType Тип приюта
     * @param shelterDTO Обновленная информация о приюте
     * @return Возвращает экземпляр класса <b>ShelterDTO</b> после сохранения результата редактрирования
     */
    public ShelterDTO editShelter(ShelterType shelterType, ShelterDTO shelterDTO) {
        Shelter shelter = findShelterByShelterType(shelterType);
        // Заполняем только доступные для редактирования поля (исключая id, shelterType и схема проезда (она не реализована))
        shelter.setShelterName(shelterDTO.getShelterName());
        shelter.setShelterDescription(shelterDTO.getShelterDescription());
        shelter.setShelterAddress(shelterDTO.getShelterAddress());
        shelter.setShelterContacts(shelterDTO.getShelterContacts());
        shelter.setSecurityContacts(shelterDTO.getSecurityContacts());

        Shelter editedShelter = shelterRepository.save(shelter);
        return ShelterDTO.fromShelter(editedShelter);
    }

    private Shelter findShelterByShelterType(ShelterType shelterType) {
        return shelterRepository.findSheltersByShelterType(shelterType)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ShelterNotFoundException(shelterType + " not found in database"));
    }
}
