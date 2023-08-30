package pro.sky.animalshelter.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.animalshelter.dto.ShelterDTO;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.repository.ShelterRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    @Value("${path.to.data.shelter.file}")
    private String dataFilePath;
    @Value("${name.of.data.shelter.file}")
    private String dataFileName;

    private final ShelterRepository shelterRepository;
    private final ShelterService shelterService;

    public FileService(ShelterRepository shelterRepository, ShelterService shelterService) {
        this.shelterRepository = shelterRepository;
        this.shelterService = shelterService;
    }

    public File getDataFile(ShelterType shelterType) {
        Shelter shelter = shelterService.findShelterByShelterType(shelterType);
        return new File(shelter.getDrivingDirection());
    }

   /* public void saveDirectoryToRepository(ShelterType shelterType) {
        Shelter shelter = shelterService.findShelterByShelterType(shelterType);
        shelter.setDrivingDirection(dataFilePath + "/" + shelter.getShelterName() + "_" + dataFileName);
        shelterRepository.save(shelter);
    }*/

    /**
     * Сохраняет путь до файла в таблицу shelters.
     * <p>
     * Путь сохраняется в строку соответствующую переданному id.
     */
    public ShelterDTO saveDirectoryToRepository(ShelterType shelterType, MultipartFile file) throws IOException {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        Path filePath = Path.of(dataFilePath, shelterType + "." + fileExtension);
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream()) {
            Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        String pathToFile = filePath.toString();
        Shelter shelter = shelterService.findShelterByShelterType(shelterType);
        shelter.setDrivingDirection(pathToFile);
        shelter = shelterRepository.save(shelter);

        return ShelterDTO.fromShelter(shelter);
    }
}
