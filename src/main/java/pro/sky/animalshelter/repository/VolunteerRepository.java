package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.model.Volunteer;

import java.util.Collection;

public interface VolunteerRepository extends JpaRepository<Volunteer, Integer> {

    Collection<Volunteer> findVolunteersByShelter(Shelter shelter);
    Collection<Volunteer> findVolunteersByShelter_ShelterType(ShelterType shelterType);
}

//    @Query(value = "UPDATE volunteers SET volunteer_active = false WHERE volunteer_id = :volunteerId", nativeQuery = true)
//    void blockVolunteerById(Integer volunteerId);
//
//    @Query(value = "UPDATE volunteers SET volunteer_active = true WHERE volunteer_id = :volunteerId", nativeQuery = true)
//    void unlockVolunteerById(Integer volunteerId);


