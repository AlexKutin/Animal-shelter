package pro.sky.animalshelter.controller;

import dto.ShelterDTO;
import dto.VolunteerDTO;
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
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalshelter.exception.ShelterNotFoundException;
import pro.sky.animalshelter.exception.VolunteerNotFoundException;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.service.ShelterService;
import pro.sky.animalshelter.service.VolunteerService;

import java.util.List;

@Tag(name = "Приюты", description = "Управление приютами")
@RestController
@RequestMapping("/shelters")
public class ShelterController {
    private final Logger logger = LoggerFactory.getLogger(ShelterController.class);
    private final ShelterService shelterService;
    private final VolunteerService volunteerService;

    public ShelterController(ShelterService shelterService, VolunteerService volunteerService) {
        this.shelterService = shelterService;
        this.volunteerService = volunteerService;
    }

    @Operation(
            summary = "Получение информации о приюте",
            description = "Получение информации о приюте выбранного типа",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Информация о выбранном приюте",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ShelterDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Информация о выбранном типе приюта отсутствует в БД",
                            content = @Content
                    )
            }
    )
    @GetMapping()
    public ResponseEntity<ShelterDTO> getShelterInfoByShelterType(
            @RequestParam @Parameter(description = "Тип приюта") ShelterType shelterType) {
        try {
            ShelterDTO shelterDTO = shelterService.getShelterByShelterType(shelterType);
            return ResponseEntity.ok(shelterDTO);
        } catch (ShelterNotFoundException e) {
            logger.error("Указанный тип приюта {} не найден в БД ", shelterType);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Получение списка волонтеров приюта",
            description = "Получение списка волонтеров приюта выбранного типа",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список волонтеров выбранного типа приюта",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = VolunteerDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Информация о выбранном типе приюта отсутствует в БД",
                            content = @Content
                    )
            }
    )
    @GetMapping("/volunteers")
    public ResponseEntity<List<VolunteerDTO>> getVolunteersByShelterType(
            @RequestParam @Parameter(description = "Тип приюта") ShelterType shelterType) {
        try {
            List<VolunteerDTO> volunteerDTOList = shelterService.getAllVolunteersByShelterType(shelterType);
            return ResponseEntity.ok(volunteerDTOList);
        } catch (ShelterNotFoundException e) {
            logger.error("Указанный тип приюта {} не найден в БД ", shelterType);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Добавление волонтера",
            description = "Добавление волонтера для приюта выбранного типа",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Информация о добавленном волонтере",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = VolunteerDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Информация о выбранном типе приюта отсутствует в БД",
                            content = @Content
                    ),
                    @ApiResponse(responseCode = "400", description = "Неверно указан тип приюта",
                            content = @Content
                    )
            }
    )
    @PostMapping("/volunteer")
    public ResponseEntity<VolunteerDTO> addVolunteerToShelter(
            @RequestBody @Parameter(description = "Информация о волонтере") VolunteerDTO volunteerDTO) {
        try {
            VolunteerDTO addedVolunteer = shelterService.addVolunteerToShelter(volunteerDTO);
            logger.info("Добавлен новый волонтер: {}", addedVolunteer);
            return ResponseEntity.ok(addedVolunteer);
        } catch (ShelterNotFoundException e) {
            logger.error("Указанный тип приюта {} не найден в БД ", volunteerDTO.getShelterType());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Заблокировать волонтера",
            description = "Заблокировать (отключить) волонтера  по его идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Информация о заблокированном волонтере",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = VolunteerDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Волонтер с указанным Id не найден в БД",
                            content = @Content
                    )
            }
    )
    @PutMapping("/volunteer/block/{volunteerId}")
    public ResponseEntity<VolunteerDTO> blockVolunteerById(
            @PathVariable @Parameter(description = "Id волонтера")Integer volunteerId) {
        try {
            VolunteerDTO blockedVolunteer = volunteerService.blockVolunteerById(volunteerId);
            logger.info("Волонтер заблокирован: {}", blockedVolunteer);
            return ResponseEntity.ok(blockedVolunteer);
        } catch (VolunteerNotFoundException e) {
            logger.error("Волонтер с указаным id = {} не найден в БД ", volunteerId);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Разблокировать волонтера",
            description = "Разблокировать (активировать) волонтера по его идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Информация о разблокированном волонтере",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = VolunteerDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Волонтер с указанным Id не найден в БД",
                            content = @Content
                    )
            }
    )
    @PutMapping("/volunteer/unlock/{volunteerId}")
    public ResponseEntity<VolunteerDTO> unlockVolunteerById(
            @PathVariable @Parameter(description = "Id волонтера")Integer volunteerId) {
        try {
            VolunteerDTO blockedVolunteer = volunteerService.unlockVolunteerById(volunteerId);
            logger.info("Волонтер разблокирован: {}", blockedVolunteer);
            return ResponseEntity.ok(blockedVolunteer);
        } catch (VolunteerNotFoundException e) {
            logger.error("Волонтер с указаным id = {} не найден в БД ", volunteerId);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Редактирование информации о приюте",
            description = "Редактирование информации о приюте выбранного типа",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Результат редактирования информации о выбранном приюте",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ShelterDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Информация о выбранном типе приюта отсутствует в БД",
                            content = @Content
                    )
            }
    )
    @PutMapping
    public ResponseEntity<ShelterDTO> editShelterByShelterType(
            @RequestParam @Parameter(description = "Тип приюта") ShelterType shelterType,
            @RequestBody @Parameter(description = "Обновленная информация от приюте") ShelterDTO shelterDTO
    ) {
        try {
            ShelterDTO editedShelterDTO = shelterService.editShelter(shelterType, shelterDTO);
            logger.info("Обновлена информация о приюте: {}", editedShelterDTO);
            return ResponseEntity.ok(editedShelterDTO);
        } catch (ShelterNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)  // 400
    public ResponseEntity<?> handleJsonParseError(HttpMessageNotReadableException e) {
        logger.error(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
