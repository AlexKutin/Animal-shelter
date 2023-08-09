package pro.sky.animalshelter.service;

import dto.ShelterDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.exception.ShelterNotFoundException;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.model.Volunteer;
import pro.sky.animalshelter.repository.ShelterRepository;
import pro.sky.animalshelter.repository.VolunteerRepository;

import java.util.List;
import java.util.stream.Collectors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ShelterService {
    private final ShelterRepository shelterRepository;
    private final VolunteerRepository volunteerRepository;

    public ShelterService(ShelterRepository shelterRepository, VolunteerRepository volunteerRepository) {
        this.shelterRepository = shelterRepository;
        this.volunteerRepository = volunteerRepository;
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
        // Заполняем только доступные для редактирования поля (исключая id, shelterType)
        shelter.setShelterName(shelterDTO.getShelterName());
        shelter.setShelterDescription(shelterDTO.getShelterDescription());
        shelter.setShelterAddress(shelterDTO.getShelterAddress());
        shelter.setDrivingDirection(shelterDTO.getDrivingDirection());
        shelter.setShelterContacts(shelterDTO.getShelterContacts());
        shelter.setSecurityContacts(shelterDTO.getSecurityContacts());

        Shelter editedShelter = shelterRepository.save(shelter);
        return ShelterDTO.fromShelter(editedShelter);
    }

    /**
     * Возвращает список волонтеров для выбранного типа приюта
     * @param shelterType Тип приюта
     * @return Список волонтеров выбранного типа приюта
     */
    public List<VolunteerDTO> getAllVolunteersByShelterType(ShelterType shelterType) {
        Shelter shelter = findShelterByShelterType(shelterType);
        List<Volunteer> volunteers = shelter.getVolunteers();
        return volunteers.stream().map(VolunteerDTO::fromVolunteer).collect(Collectors.toList());
    }

    /**
     * Добавляет волонтера и прикрепряет его к приюту
     * @param volunteerDTO Информация о новом волонтере
     * @return Возвращает экземпляр класса <b>VolunteerDTO</b> после сохранения его в БД
     */
    public VolunteerDTO addVolunteerToShelter(VolunteerDTO volunteerDTO) {
        Shelter shelter = findShelterByShelterType(volunteerDTO.getShelterType());
        Volunteer volunteer = volunteerDTO.toVolunteer(shelter);
        volunteer = volunteerRepository.save(volunteer);
        return VolunteerDTO.fromVolunteer(volunteer);
    }

    private Shelter findShelterByShelterType(ShelterType shelterType) {
        return shelterRepository.findSheltersByShelterType(shelterType)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ShelterNotFoundException(shelterType + " not found in database"));
    }
}
