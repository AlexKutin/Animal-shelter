package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.CatAdopter;
import pro.sky.animalshelter.model.ReportCatShelter;
import pro.sky.animalshelter.model.ReportStatus;

import java.util.List;

public interface ReportCatShelterRepository extends JpaRepository<ReportCatShelter, Integer> {
    List<ReportCatShelter> findByCatAdopterAndReportStatus(CatAdopter catAdopter, ReportStatus reportStatus);
}
