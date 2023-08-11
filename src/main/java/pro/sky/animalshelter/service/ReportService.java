package pro.sky.animalshelter.service;

import org.springframework.stereotype.Service;
import pro.sky.animalshelter.repository.ClientRepository;
import pro.sky.animalshelter.repository.ReportRepository;

import java.time.LocalDateTime;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    public ReportService (ReportRepository reportRepository){
        this.reportRepository = reportRepository;
    }
    public boolean yesterdayReportIsExist (){
        return reportRepository.getReportByDataTimeReportAfter(LocalDateTime.now().minusDays(1));
    }
}