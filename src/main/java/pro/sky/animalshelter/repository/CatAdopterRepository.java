package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.animalshelter.model.AdopterStatus;
import pro.sky.animalshelter.model.CatAdopter;

import java.util.List;

public interface CatAdopterRepository extends JpaRepository<CatAdopter, Integer> {
    List<CatAdopter> findAllByAdopterStatusOrderByUser(AdopterStatus adopterStatus);

    @Query(value = "SELECT EXISTS (SELECT adopter_id FROM cat_adopters WHERE user_id = :userId and cat_id = :catId)", nativeQuery = true)
    boolean isPresentCatAdopterByUserAndCat(Integer userId, Integer catId);

    CatAdopter findByUserUserId(Integer userId);
}
