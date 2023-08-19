package pro.sky.animalshelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.service.FileService;

import java.io.*;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Operation(
            summary = "Загрузка схемы проезда",
            description = "Схема проезда сохраняется в виде изображения формата png, в базу данных сохраняется путь до изображения"
    )
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadDataFileDogShelter(@RequestParam MultipartFile file, @RequestParam ShelterType shelterType, @RequestParam String shelterName) {
        File dataFile = fileService.getDataFile(shelterName);
        try (FileOutputStream fos = new FileOutputStream(dataFile)) {
            IOUtils.copy(file.getInputStream(), fos);
            fileService.saveDirectoryToRepository(shelterType, shelterName);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(
            summary = "Скачивание схемы проезда",
            description = "Схема проезда скачивается в виде изображения формата png"
    )
    @GetMapping(value = "/export", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<InputStreamResource> downloadFileById(String shelterName) throws FileNotFoundException {
        File file = fileService.getDataFile(shelterName);
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename =\"Shelter.pnj\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
