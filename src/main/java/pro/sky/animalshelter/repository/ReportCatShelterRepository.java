package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Report;
import pro.sky.animalshelter.model.ReportCatShelter;
import pro.sky.animalshelter.model.UserShelter;

import java.time.LocalDateTime;
import java.util.Collection;

public interface ReportCatShelterRepository extends JpaRepository<ReportCatShelter, Long> {


}
