package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Report;
import pro.sky.animalshelter.model.UserShelter;

import java.time.LocalDateTime;
import java.util.Collection;

public interface ReportRepository extends JpaRepository<Report, Long> {
    static Report getReportById(Integer idReport) {
        return null;
    }

    Collection<Report> findVolunteersByShelter(UserShelter userShelter);


    boolean getReportByDataTimeReportAfter (LocalDateTime localDateTime);

    static Report getReportByUser(UserShelter userShelter) {
        return null;
    }

    Report updateReportByIdReport (Integer idReport);

}
