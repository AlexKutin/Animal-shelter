package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Dog;

public interface DogsRepository extends JpaRepository<Dog, Integer> {
}
