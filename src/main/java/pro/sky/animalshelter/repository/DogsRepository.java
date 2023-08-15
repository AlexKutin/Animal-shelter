package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelter.dto.DogDTO;
import pro.sky.animalshelter.model.Dog;

public interface DogsRepository extends JpaRepository<Dog, Integer> {
}
