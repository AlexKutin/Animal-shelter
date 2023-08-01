package pro.sky.animalshelter.controller;

import dto.ShelterDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalshelter.exception.ShelterNotFoundException;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.service.ShelterService;

@Tag(name = "Приюты", description = "Управление приютами")
@RestController
@RequestMapping("/shelters")
public class ShelterController {
    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
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
            return ResponseEntity.ok(editedShelterDTO);
        } catch (ShelterNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
