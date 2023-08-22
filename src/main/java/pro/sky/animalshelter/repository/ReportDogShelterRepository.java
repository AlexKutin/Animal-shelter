package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.*;

import java.util.List;

public interface ReportDogShelterRepository extends JpaRepository<ReportDogShelter, Integer> {

    List<ReportDogShelter> findByDogAdopterAndReportStatus(DogAdopter dogAdopter, ReportStatus reportStatus);
    List<ReportDogShelter> findByDogAdopter(DogAdopter dogAdopter);
}
