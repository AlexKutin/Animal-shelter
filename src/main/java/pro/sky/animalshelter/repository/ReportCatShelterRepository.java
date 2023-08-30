package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.*;

import java.util.Collection;
import java.util.List;

public interface ReportCatShelterRepository extends JpaRepository<ReportCatShelter, Integer> {

    List<ReportCatShelter> findByCatAdopter(CatAdopter catAdopter);

    List<ReportCatShelter> findByCatAdopterAndReportStatusIn(CatAdopter dogAdopter, Collection<ReportStatus> reportStatuses);

    List<ReportCatShelter> findByReportStatusIn(Collection<ReportStatus> reportStatuses);
}
