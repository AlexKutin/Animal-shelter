package pro.sky.animalshelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
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
    private final Logger logger = LoggerFactory.getLogger(ReportController.class);
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
            @RequestParam(required = false) @Parameter(description = "Id усыновителя") Integer adopterId,
            @RequestParam(required = false) @Parameter(description = "Статус отчета") ReportStatus reportStatus) {
        try {
            List<ReportAnimalDTO> reportAnimalDTOList = reportService.getReportsByAdopterAndStatus(ShelterType.DOG_SHELTER, adopterId, reportStatus);
            return ResponseEntity.ok(reportAnimalDTOList);
        } catch (ReportNotFoundException | ShelterNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Обработка отчета усыновителя приюта для кошек",
            description = "Обработка отчета усыновителя волонтером приюта для кошек",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отчет с установленным статусом после обработки волонтером",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReportAnimalDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Информация об отчете с указанным Id отсутствует в БД",
                            content = @Content
                    )
            }
    )
    @PutMapping("process_report")
    public ResponseEntity<ReportAnimalDTO> processReportCatShelter(
            @RequestParam @Parameter(description = "Id Отчета") Integer reportId,
            @RequestParam @Parameter(description = "Статус отчета после обработки волонтером") ReportStatus reportStatus) {
        try {
            ReportAnimalDTO reportAnimalDTO = reportService.editStatusReport(ShelterType.CAT_SHELTER, reportId, reportStatus);
            return ResponseEntity.ok(reportAnimalDTO);
        } catch (AdopterNotFoundException | ShelterNotFoundException | ReportNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
