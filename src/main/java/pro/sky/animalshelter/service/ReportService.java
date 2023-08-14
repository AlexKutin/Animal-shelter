package pro.sky.animalshelter.service;

import org.jetbrains.annotations.NotNull;
import org.jvnet.hk2.annotations.Service;
import pro.sky.animalshelter.dto.ReportDTO;
import pro.sky.animalshelter.dto.VolunteerDTO;
import pro.sky.animalshelter.exception.VolunteerNotFoundException;
import pro.sky.animalshelter.model.Report;
import pro.sky.animalshelter.model.UserShelter;
import pro.sky.animalshelter.repository.ReportRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    public ReportService (ReportRepository reportRepository){
        this.reportRepository = reportRepository;
    }
    public boolean yesterdayReportIsExist (){
        return reportRepository.getReportByDataTimeReportAfter(LocalDateTime.now().minusDays(1));
    }

    ReportDTO getReportByUser (UserShelter userShelter) {
        Report report = ReportRepository.getReportByUser(userShelter);
        return ReportDTO.fromReport(reportRepository.save(report));    }

    ReportDTO getReportById (Integer idReport) {
        Report report = ReportRepository.getReportById(idReport);
        return ReportDTO.fromReport(reportRepository.save(report));    }

    ReportDTO updateReportByIdReport (Integer idReport, Report report) {
        Report report1 = ReportRepository.getReportById(idReport).put(report);

        return ReportDTO.fromReport(reportRepository.save(report));
    }
}

