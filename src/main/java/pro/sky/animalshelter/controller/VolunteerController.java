package pro.sky.animalshelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalshelter.dto.*;
import pro.sky.animalshelter.exception.*;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.service.AdopterService;
import pro.sky.animalshelter.service.AnimalService;
import pro.sky.animalshelter.service.UserShelterService;

import java.util.List;

//@Tag(name = "Раздел для волонтеров", description = "Управление функционалом для волонтеров")
@RestController()
@RequestMapping("/volunteers")
public class VolunteerController {
    private final Logger logger = LoggerFactory.getLogger(VolunteerController.class);

    private final UserShelterService userShelterService;
    private final AnimalService animalService;
    private final AdopterService adopterService;

    public VolunteerController(UserShelterService userShelterService, AnimalService animalService, AdopterService adopterService) {
        this.userShelterService = userShelterService;
        this.animalService = animalService;
        this.adopterService = adopterService;
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

    @Operation(
            summary = "Получение списка всех собак приюта",
            description = "Получение списка всех собак, содержащихся в приюте",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список всех собак приюта",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = DogDTO.class)
                                    )
                            )
                    )
            },
            tags = "Животные"
    )
    @GetMapping("/view_all_dogs")
    public ResponseEntity<List<DogDTO>> getAllDogs() {
        List<DogDTO> dogDTOList = animalService.getAllDogs();
        return ResponseEntity.ok(dogDTOList);
    }

    @Operation(
            summary = "Получение списка всех кошек приюта",
            description = "Получение списка всех кошек, содержащихся в приюте",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список всех кошек приюта",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = CatDTO.class)
                                    )
                            )
                    )
            },
            tags = "Животные"
    )
    @GetMapping("/view_all_cats")
    public ResponseEntity<List<CatDTO>> getAllCats() {
        List<CatDTO> catDTOList = animalService.getAllCats();
        return ResponseEntity.ok(catDTOList);
    }

    @Operation(
            summary = "Сохранение новой собаки в базу данных",
            tags = "Животные"
    )
    @PostMapping("/save_dog")
    public ResponseEntity<DogDTO> saveDogToDb(@RequestBody DogDTO dogDTO) {
        DogDTO savedDog = animalService.saveDogToDb(dogDTO);
        return ResponseEntity.ok(savedDog);
    }

    @Operation(
            summary = "Сохранение новой кошки в базу данных",
            tags = "Животные"
    )
    @PostMapping("/save_cat")
    public ResponseEntity<CatDTO> saveCatToDb(@RequestBody CatDTO catDTO) {
        CatDTO savedCat = animalService.saveCatToDb(catDTO);
        return ResponseEntity.ok(savedCat);
    }

    @Operation(
            summary = "Добавление в базу данных записи о закреплении собаки за усыновителем",
            tags = {"Приют собак - Усыновители"}
    )
    @PostMapping("/save_dog_adopter")
    public ResponseEntity<AnimalAdopterDTO> saveDogAdopter(
            @RequestBody @Parameter(description = "Информация об усыновителе") AnimalAdopterDTO animalAdopterDTO) {
        animalAdopterDTO.setShelterType(ShelterType.DOG_SHELTER);
        AnimalAdopterDTO savedDogAdopter = animalService.saveDogAdopter(animalAdopterDTO);
        return ResponseEntity.ok(savedDogAdopter);
    }

    @Operation(
            summary = "Получение списка всех усыновителей приюта для собак",
            description = "Получение списка всех усыновителей приюта для собак в соответствии со статусом",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список всех усыновителей",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = AnimalAdopterDTO.class)
                                    )
                            )
                    )
            },
            tags = {"Приют собак - Усыновители"}
    )
    @GetMapping("/get_dog_adopters")
    public ResponseEntity<List<AnimalAdopterDTO>> getDogAdopters(
            @RequestParam(required = false)
            @Parameter(description = "Статус усыновителя") AdopterStatus adopterStatus) {
        List<AnimalAdopterDTO> dogAdopters = animalService.getDogAdopters(adopterStatus);
        return ResponseEntity.ok(dogAdopters);
    }

    @Operation(
            summary = "Изменение статуса испытательного срока для усыновителя собаки",
            description = "Изменение волонтером статуса испытательного срока для усыновителя собаки",
            tags = {"Приют собак - Усыновители"}
    )
    @PostMapping("/save_probation_status_dog_adopter")
    public ResponseEntity<AnimalAdopterDTO> saveProbationStatusDogAdopter(
            @RequestParam @Parameter(description = "id усыновителя") Integer adopterId,
            @RequestParam @Parameter(
                    description = "Статус испытательного срока усыновителя",
                    schema = @Schema(implementation = PROBATION_STATUS.class)) PROBATION_STATUS probationStatus) {
        AnimalAdopterDTO dogAdopterDTO = adopterService.processProbationStatusForDogAdopter(adopterId, probationStatus);
        return ResponseEntity.ok(dogAdopterDTO);
    }

    @Operation(
            summary = "Добавление в базу данных записи о закреплении кошки за усыновителем",
            tags = {"Приют кошек - Усыновители"}
    )
    @PostMapping("/save_cat_adopter")
    public ResponseEntity<AnimalAdopterDTO> saveCatAdopter(@RequestBody AnimalAdopterDTO animalAdopterDTO/*, @RequestParam Integer catId, @RequestParam Integer adopterId*/) {
        animalAdopterDTO.setShelterType(ShelterType.CAT_SHELTER);
        AnimalAdopterDTO savedCatAdopter = animalService.saveCatAdopter(animalAdopterDTO/*, catId, adopterId*/);
        return ResponseEntity.ok(savedCatAdopter);
    }

    @Operation(
            summary = "Получение списка всех усыновителей приюта для кошек",
            description = "Получение списка всех усыновителей приюта для кошек в соответствии со статусом",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список всех усыновителей",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = AnimalAdopterDTO.class)
                                    )
                            )
                    )
            },
            tags = {"Приют кошек - Усыновители"}
    )
    @GetMapping("/get_cat_adopters")
    public ResponseEntity<List<AnimalAdopterDTO>> getCatAdopters(
            @RequestParam(required = false) @Parameter(description = "Статус усыновителя") AdopterStatus adopterStatus) {
        List<AnimalAdopterDTO> catAdopters = animalService.getCatAdopters(adopterStatus);
        return ResponseEntity.ok(catAdopters);
    }

    @Operation(
            summary = "Изменение статуса испытательного срока для усыновителя кошки",
            description = "Изменение волонтером статуса испытательного срока для усыновителя кошки",
            tags = {"Приют кошек - Усыновители"}
    )
    @PostMapping("/save_probation_status_cat_adopter")
    public ResponseEntity<AnimalAdopterDTO> saveProbationStatusCatAdopter(
            @RequestParam @Parameter(description = "id усыновителя") Integer adopterId,
            @RequestParam @Parameter(
                    description = "Статус испытательного срока усыновителя",
                    schema = @Schema(implementation = PROBATION_STATUS.class)) PROBATION_STATUS probationStatus) {
        AnimalAdopterDTO dogAdopterDTO = adopterService.processProbationStatusForCatAdopter(adopterId, probationStatus);
        return ResponseEntity.ok(dogAdopterDTO);
    }

    @ExceptionHandler(DuplicateAdopterException.class)  // 400
    public ResponseEntity<ErrorDTO> handleDuplicateAdopter(DuplicateAdopterException e) {
        return ResponseEntity.badRequest().body(e.toErrorDTO());
    }

    @ExceptionHandler(UserNotFoundException.class)  // 404
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException e) {
        logger.error(e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AnimalNotFoundException.class)  // 404
    public ResponseEntity<?> handleAnimalNotFound(AnimalNotFoundException e) {
        logger.error(e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AdopterNotFoundException.class)  // 404
    public ResponseEntity<?> handleAdopterNotFound(AdopterNotFoundException e) {
        logger.error(e.getMessage());
        return ResponseEntity.notFound().build();
    }
}
