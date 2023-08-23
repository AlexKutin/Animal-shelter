package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.animalshelter.model.AdopterStatus;
import pro.sky.animalshelter.model.DogAdopter;

import java.util.List;

public interface DogAdopterRepository extends JpaRepository<DogAdopter, Integer> {
    List<DogAdopter> findAllByAdopterStatusOrderByUser(AdopterStatus adopterStatus);

    @Query(value = "SELECT EXISTS (SELECT adopter_id FROM dog_adopters WHERE user_id = :userId and dog_id = :dogId)", nativeQuery = true)
    boolean isPresentDogAdopterByUserAndDog(Integer userId, Integer dogId);

    DogAdopter findByUserUserId(Integer userId);
}
