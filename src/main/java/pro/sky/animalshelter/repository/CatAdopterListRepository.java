package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.CatAdopterList;

public interface CatAdopterListRepository extends JpaRepository<CatAdopterList, Integer> {
}
