package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelter.model.UserCatShelter;

@Repository
public interface UserCatShelterRepository extends JpaRepository<UserCatShelter, Integer> {

}
