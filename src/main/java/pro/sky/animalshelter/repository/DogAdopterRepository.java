package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.Query;
import pro.sky.animalshelter.model.DogAdopter;

import java.util.List;

public interface DogAdopterRepository extends AdopterRepository<DogAdopter> {

    @Query(value = "SELECT EXISTS (SELECT adopter_id FROM dog_adopters WHERE user_id = :userId and dog_id = :dogId)", nativeQuery = true)
    boolean isPresentDogAdopterByUserAndDog(Integer userId, Integer dogId);

    @Query(value = "select adopter_id from "+
            "(select rds.adopter_id, max(date_report) as last_date_report from report_dog_shelter rds " +
            "join dog_adopters da " +
            "on rds.adopter_id = da.adopter_id where da.adopter_status in (?1, ?2) group by rds.adopter_id) t " +
            "where last_date_report < (CURRENT_DATE - ?3) ", nativeQuery = true)
    List<Integer> getAdoptersChatIdWhoNotSendReport(String adopterStatuses1, String adopterStatuses2, int skippedDays);
}
