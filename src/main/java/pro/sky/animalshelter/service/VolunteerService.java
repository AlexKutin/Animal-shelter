package pro.sky.animalshelter.service;

import pro.sky.animalshelter.dto.VolunteerDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.exception.VolunteerNotFoundException;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.model.Volunteer;
import pro.sky.animalshelter.repository.VolunteerRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static pro.sky.animalshelter.Constants.TextConstants.VOLUNTEER_BY_ID_NOT_FOUND_MESSAGE;

@Service
public class VolunteerService {
    private final VolunteerRepository volunteerRepository;

    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    public VolunteerDTO blockVolunteerById(Integer volunteerId) {
        return manageVolunteerEnabled(volunteerId, false);
    }

    public VolunteerDTO unlockVolunteerById(Integer volunteerId) {
        return manageVolunteerEnabled(volunteerId, true);
    }

    @NotNull
    private VolunteerDTO manageVolunteerEnabled(Integer volunteerId, Boolean isActive) {
        Volunteer volunteer = volunteerRepository
                .findById(volunteerId)
                .orElseThrow(() -> new VolunteerNotFoundException(String.format(VOLUNTEER_BY_ID_NOT_FOUND_MESSAGE, volunteerId)));
        volunteer.setActive(isActive);
        return VolunteerDTO.fromVolunteer(volunteerRepository.save(volunteer));
    }
    public Collection<Volunteer> findVolunteersByShelterType(ShelterType shelterType) {
        return volunteerRepository.findVolunteersByShelter_ShelterType(shelterType);
    }

    public String findAvailableVolunteerTelegram(ShelterType shelterType) {
        Collection<Volunteer> volunteers = volunteerRepository.findVolunteersByShelter_ShelterType(shelterType);

        for (Volunteer volunteer : volunteers) {
            if (volunteer.isActive()) {
                return volunteer.getTelegramAddress();
            }
        }

        return null; // Если нет доступных волонтеров
    }

    public List<Long> findAvailableVolunteerChatId(ShelterType shelterType) {
        return volunteerRepository.findAllByIsActiveTrueAndShelter_ShelterType(shelterType)
                .stream()
                .map(Volunteer::getChatId)
                .collect(Collectors.toList());
    }
}

