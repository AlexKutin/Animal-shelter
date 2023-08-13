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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.animalshelter.dto.UserShelterDTO;
import pro.sky.animalshelter.exception.ShelterNotFoundException;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.service.UserShelterService;

import java.util.List;

@Tag(name = "Раздел для волонтеров", description = "Управление функционалом для волонтеров")
@RestController()
@RequestMapping("/volunteers")
public class VolunteerController {
    private final Logger logger = LoggerFactory.getLogger(VolunteerController.class);
    private final UserShelterService userShelterService;

    public VolunteerController(UserShelterService userShelterService) {
        this.userShelterService = userShelterService;
    }

    @Operation(
            summary = "Получение списка пользователей приюта",
            description = "Получение списка зарегистрированных пользователей приюта выбранного типа",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список зарегистрированных пользователей выбранного типа приюта",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = UserShelterDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Информация о выбранном типе приюта отсутствует в БД",
                            content = @Content
                    )
            }
    )
    @GetMapping("/view_register_users")
    public ResponseEntity<List<UserShelterDTO>> getAllRegisteredUsersByShelter(
            @RequestParam @Parameter(description = "Тип приюта") ShelterType shelterType) {
        try {
            List<UserShelterDTO> userShelterDTOList = userShelterService.getAllUsersByShelterType(shelterType);
            return ResponseEntity.ok(userShelterDTOList);
        } catch (ShelterNotFoundException e) {
            logger.error("Указанный тип приюта {} не найден в БД ", shelterType);
            return ResponseEntity.notFound().build();
        }

    }
}
