package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.model.Volunteer;

import java.util.Collection;

public interface VolunteerRepository extends JpaRepository<Volunteer, Integer> {

    Collection<Volunteer> findVolunteersByShelter(Shelter shelter);

    Collection<Volunteer> findVolunteersByShelter_ShelterType(ShelterType shelterType);

    Collection<Volunteer> findAllByIsActiveTrueAndShelter_ShelterType(ShelterType shelterType);
}


