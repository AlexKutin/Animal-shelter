package pro.sky.animalshelter.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalshelter.dto.ReportAnimalDTO;
import pro.sky.animalshelter.exception.AdopterNotFoundException;
import pro.sky.animalshelter.exception.ReportNotFoundException;
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

    @GetMapping("/cat_shelter_reports")
    public ResponseEntity<List<ReportAnimalDTO>> getReportsByCatShelterAdopter(
            @RequestParam @Parameter(description = "Id усыновителя") Integer adopterId,
            @RequestParam @Parameter(description = "Статус отчета") ReportStatus reportStatus) {
        try {
            List<ReportAnimalDTO> reportAnimalDTOList = reportService.getReportsByAdopterAndStatus(ShelterType.CAT_SHELTER, adopterId, reportStatus);
            return ResponseEntity.ok(reportAnimalDTOList);
        } catch (AdopterNotFoundException | ShelterNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("dog_shelter_reports")
    public ResponseEntity<List<ReportAnimalDTO>> getReportsByDogShelterAdopter(
            @RequestParam @Parameter(description = "Id усыновителя") Integer adopterId,
            @RequestParam @Parameter(description = "Статус отчета") ReportStatus reportStatus) {
        try {
            List<ReportAnimalDTO> reportAnimalDTOList = reportService.getReportsByAdopterAndStatus(ShelterType.DOG_SHELTER, adopterId, reportStatus);
            return ResponseEntity.ok(reportAnimalDTOList);
        } catch (ReportNotFoundException | ShelterNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("process_report")
    public ResponseEntity<ReportAnimalDTO> processReportCatShelter(
            @RequestParam @Parameter(description = "Id Отчета") Integer reportId,
            @RequestParam @Parameter(description = "Статус отчета после обработки волонтером") ReportStatus reportStatus) {
        try {
            ReportAnimalDTO reportAnimalDTO = reportService.editStatusReport(ShelterType.CAT_SHELTER, reportId, reportStatus);
            return ResponseEntity.ok(reportAnimalDTO);
        } catch (AdopterNotFoundException | ShelterNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
