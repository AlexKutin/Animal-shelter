package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.ShelterType;

import java.util.List;

public interface ShelterRepository extends JpaRepository<Shelter, Integer> {

    List<Shelter> findSheltersByShelterType(ShelterType shelterType);

}
