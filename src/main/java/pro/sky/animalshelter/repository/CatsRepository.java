package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Cat;

public interface CatsRepository extends JpaRepository<Cat, Integer> {
}
