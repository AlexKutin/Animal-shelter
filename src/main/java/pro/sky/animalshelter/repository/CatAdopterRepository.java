package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.Query;
import pro.sky.animalshelter.model.CatAdopter;

import java.util.List;

public interface CatAdopterRepository extends AdopterRepository<CatAdopter> {

    @Query(value = "SELECT EXISTS (SELECT adopter_id FROM cat_adopters WHERE user_id = :userId and cat_id = :catId)", nativeQuery = true)
    boolean isPresentCatAdopterByUserAndCat(Integer userId, Integer catId);


    @Query(value = "select adopter_id from "+
            "(select rcs.adopter_id, max(date_report) as last_date_report from report_cat_shelter rcs " +
            "join cat_adopters ca " +
            "on rcs.adopter_id = ca.adopter_id where ca.adopter_status in (?1, ?2) group by rcs.adopter_id) t " +
            "where last_date_report < (CURRENT_DATE - ?3) ", nativeQuery = true)
    List<Integer> getAdoptersChatIdWhoNotSendReport(String adopterStatuses1, String adopterStatuses2, int skippedDays);
}
