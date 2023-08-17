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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalshelter.dto.*;
import pro.sky.animalshelter.exception.ShelterNotFoundException;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.DogsRepository;
import pro.sky.animalshelter.service.AnimalService;
import pro.sky.animalshelter.service.UserShelterService;

import java.util.List;

@Tag(name = "Раздел для волонтеров", description = "Управление функционалом для волонтеров")
@RestController()
@RequestMapping("/volunteers")
public class VolunteerController {
    private final Logger logger = LoggerFactory.getLogger(VolunteerController.class);

    private final UserShelterService userShelterService;

    private final AnimalService animalService;


    public VolunteerController(UserShelterService userShelterService, DogsRepository dogsRepository, AnimalService animalService) {
        this.userShelterService = userShelterService;
        this.animalService = animalService;
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
            summary = "Сохранение новой собаки в базу данных"
    )
    @PostMapping("/save_dog")
    public ResponseEntity<DogDTO> saveDogToDb(@RequestBody DogDTO dogDTO) {
         DogDTO savedDog = animalService.saveDogToDb(dogDTO);
        return ResponseEntity.ok(savedDog);
    }

    @Operation(
            summary = "Сохранение новой кошки в базу данных"
    )
    @PostMapping("/save_cat")
    public ResponseEntity<CatDTO> saveCatToDb(@RequestBody CatDTO catDTO) {
        CatDTO savedCat = animalService.saveCatToDb(catDTO);
        return ResponseEntity.ok(savedCat);
    }

    @Operation(
            summary = "Добавление в базу данных записи о закреплении собаки за усыновителем"
    )
    @PostMapping("/save_dog_adopter")
    public ResponseEntity<DogAdopterListDTO> saveDogAdopter(@RequestBody DogAdopterListDTO dogAdopterListDTO, @RequestParam Integer dogId, @RequestParam Integer adopterId) {
        DogAdopterListDTO savedDogAdopter = animalService.saveDogAdopter(dogAdopterListDTO, dogId, adopterId);
        return ResponseEntity.ok(savedDogAdopter);
    }

    @Operation(
            summary = "Добавление в базу данных записи о закреплении кошки за усыновителем"
    )
    @PostMapping("/save_cat_adopter")
    public ResponseEntity<CatAdopterListDTO> saveCatAdopter(@RequestBody CatAdopterListDTO catAdopterListDTO, @RequestParam Integer catId, @RequestParam Integer adopterId) {
        CatAdopterListDTO savedCatAdopter = animalService.saveCatAdopter(catAdopterListDTO, catId, adopterId);
        return ResponseEntity.ok(savedCatAdopter);
    }
}
