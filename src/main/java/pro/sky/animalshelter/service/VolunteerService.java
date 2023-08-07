package pro.sky.animalshelter.service;

import pro.sky.animalshelter.dto.VolunteerDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.exception.VolunteerNotFoundException;
import pro.sky.animalshelter.model.Volunteer;
import pro.sky.animalshelter.repository.VolunteerRepository;

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
                .orElseThrow(() -> new VolunteerNotFoundException(volunteerId + " not found in database"));
        volunteer.setActive(isActive);
        return VolunteerDTO.fromVolunteer(volunteerRepository.save(volunteer));
    }
}
