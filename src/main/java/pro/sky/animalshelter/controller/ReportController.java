package pro.sky.animalshelter.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.animalshelter.dto.ReportAnimalDTO;
import pro.sky.animalshelter.exception.AdopterNotFoundException;
import pro.sky.animalshelter.exception.ShelterNotFoundException;
import pro.sky.animalshelter.model.ReportStatus;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.service.ReportService;

import java.util.List;

@Tag(name = "Отчеты усыновителей", description = "Обработка отчетов усыновителей")
@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<List<ReportAnimalDTO>> getReportsByAdopter(
            @RequestParam ShelterType shelterType,
            @RequestParam Integer adopterId,
            @RequestParam ReportStatus reportStatus) {
        try {
            List<ReportAnimalDTO> reportAnimalDTOList = reportService.getReportsByAdopterAndStatus(shelterType, adopterId, reportStatus);
            return ResponseEntity.ok(reportAnimalDTOList);
        } catch (AdopterNotFoundException | ShelterNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
