package pro.sky.animalshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.repository.ShelterRepository;

import java.io.File;
import java.util.Optional;

@Service
public class FileService {

    @Value("${path.to.data.shelter.file}")
    private String dataFilePath;
    @Value("${name.of.data.shelter.file}")
    private String dataFileName;

    @Autowired
    private ShelterRepository shelterRepository;

    public File getDataFile(String shelterName) {
        return new File(dataFilePath + "/" + shelterName + dataFileName);
    }

    /**
     * Сохраняет путь до файла в таблицу shelters.
     * <p>
     * Путь сохраняется в строку соответствующую переданному id.
     */
    public void saveDirectoryToRepository(ShelterType shelterType, String shelterName) {
        Shelter shelter = shelterRepository.findSheltersByShelterType(shelterType).get(0);
        shelter.setDrivingDirection(dataFilePath + "/" + shelterName + dataFileName);
        shelterRepository.save(shelter);
    }
}
