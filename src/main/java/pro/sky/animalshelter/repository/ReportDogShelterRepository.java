package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.*;

import java.util.Collection;
import java.util.List;

public interface ReportDogShelterRepository extends JpaRepository<ReportDogShelter, Integer> {

    List<ReportDogShelter> findByDogAdopterAndReportStatusIn(DogAdopter dogAdopter, Collection<ReportStatus> reportStatuses);

    List<ReportDogShelter> findByDogAdopter(DogAdopter dogAdopter);

    List<ReportDogShelter> findByReportStatusIn(Collection<ReportStatus> reportStatuses);

}
