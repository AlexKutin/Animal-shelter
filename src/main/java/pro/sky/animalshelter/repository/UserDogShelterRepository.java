package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelter.model.UserDogShelter;
@Repository
public interface UserDogShelterRepository extends JpaRepository<UserDogShelter, Integer> {

}
