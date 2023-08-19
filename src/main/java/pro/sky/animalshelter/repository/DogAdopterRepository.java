package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.AdopterStatus;
import pro.sky.animalshelter.model.DogAdopter;

import java.time.LocalDateTime;
import java.util.List;

public interface DogAdopterRepository extends JpaRepository<DogAdopter, Integer> {
    List<DogAdopter> findAllByAdopterStatusOrderByUser(AdopterStatus adopterStatus);

}
