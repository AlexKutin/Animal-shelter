package pro.sky.animalshelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

import java.util.Collection;
import java.util.List;

@Tag(name = "Отчеты усыновителей", description = "Просмотр и обработка волонтерами отчетов усыновителей ")
@RestController
@RequestMapping("/reports")
public class ReportController {
    private final Logger logger = LoggerFactory.getLogger(ReportController.class);
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(
            summary = "Просмотр отчетов усыновителей приюта для кошек",
            description = "Просмотр отчетов усыновителей приюта для кошек с возможностью фильтрации по id и статусу усыновителя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список отчетов усыновителя",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReportAnimalDTO.class))
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Информация об отчете усыновителя с указанным id отсутствует в БД",
                            content = @Content
                    )
            }
    )
    @GetMapping("/cat_shelter_reports")
    public ResponseEntity<List<ReportAnimalDTO>> getReportsByCatShelterAdopter(
            @RequestParam(required = false) @Parameter(description = "Id усыновителя") Integer adopterId,
            @RequestParam(required = false) @Parameter(description = "Статус отчета",
                    array = @ArraySchema(schema = @Schema(implementation = ReportStatus.class))) Collection<ReportStatus> reportStatuses) {
        try {
            List<ReportAnimalDTO> reportAnimalDTOList = reportService.getReportsCatShelterByAdopterAndStatus(adopterId, reportStatuses);
            return ResponseEntity.ok(reportAnimalDTOList);
        } catch (AdopterNotFoundException e) {
            logger.warn(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Просмотр отчетов усыновителей приюта для собак",
            description = "Просмотр отчетов усыновителей приюта для собак с возможностью фильтрации по id и статусу усыновителя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список отчетов усыновителя",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReportAnimalDTO.class))
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Информация об отчете усыновителя с указанным id отсутствует в БД",
                            content = @Content
                    )
            }
    )
    @GetMapping("dog_shelter_reports")
    public ResponseEntity<List<ReportAnimalDTO>> getReportsByDogShelterAdopter(
            @RequestParam(required = false) @Parameter(description = "Id усыновителя") Integer adopterId,
            @RequestParam(required = false) @Parameter(description = "Статус отчета",
                    array = @ArraySchema(schema = @Schema(implementation = ReportStatus.class))) Collection<ReportStatus> reportStatuses) {
        try {
            List<ReportAnimalDTO> reportAnimalDTOList = reportService.getReportsDogSheltersByAdopterAndStatus(adopterId, reportStatuses);
            return ResponseEntity.ok(reportAnimalDTOList);
        } catch (ReportNotFoundException | AdopterNotFoundException e) {
            logger.warn(e.getMessage());
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
    @PutMapping("process_cat_shelter_report")
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

    @Operation(
            summary = "Обработка отчета усыновителя приюта для собак",
            description = "Обработка отчета усыновителя волонтером приюта для собак",
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
    @PutMapping("process_dog_shelter_report")
    public ResponseEntity<ReportAnimalDTO> processReportDogShelter(
            @RequestParam @Parameter(description = "Id Отчета") Integer reportId,
            @RequestParam @Parameter(description = "Статус отчета после обработки волонтером") ReportStatus reportStatus) {
        try {
            ReportAnimalDTO reportAnimalDTO = reportService.editStatusReport(ShelterType.DOG_SHELTER, reportId, reportStatus);
            return ResponseEntity.ok(reportAnimalDTO);
        } catch (AdopterNotFoundException | ShelterNotFoundException | ReportNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
