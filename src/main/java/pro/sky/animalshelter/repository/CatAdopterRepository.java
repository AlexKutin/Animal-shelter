package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.AdopterStatus;
import pro.sky.animalshelter.model.CatAdopter;

import java.util.List;

public interface CatAdopterRepository extends JpaRepository<CatAdopter, Integer> {
    List<CatAdopter> findAllByAdopterStatusOrderByUser(AdopterStatus adopterStatus);
}
