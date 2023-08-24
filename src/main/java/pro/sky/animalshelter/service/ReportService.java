package pro.sky.animalshelter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.dto.ReportAnimalDTO;
import pro.sky.animalshelter.exception.AdopterNotFoundException;
import pro.sky.animalshelter.exception.ReportNotFoundException;
import pro.sky.animalshelter.exception.ShelterNotFoundException;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.ReportCatShelterRepository;
import pro.sky.animalshelter.repository.ReportDogShelterRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReportService {
    Logger logger = LoggerFactory.getLogger(ReportService.class);
    private final ReportDogShelterRepository reportDogShelterRepository;
    private final ReportCatShelterRepository reportCatShelterRepository;
    private final AdopterService adopterService;

    public ReportService(ReportDogShelterRepository reportDogShelterRepository, ReportCatShelterRepository reportCatShelterRepository,
                         AdopterService adopterService) {
        this.reportDogShelterRepository = reportDogShelterRepository;
        this.reportCatShelterRepository = reportCatShelterRepository;
        this.adopterService = adopterService;
    }

    public List<ReportAnimalDTO> getReportsByAdopterAndStatus(ShelterType shelterType, Integer adopterId, ReportStatus reportStatus) {
        List<ReportAnimalDTO> reportAnimalDTOList;
        if (shelterType == ShelterType.DOG_SHELTER) {
            DogAdopter dogAdopter = adopterService.findDogAdopterById(adopterId);
            List<ReportDogShelter> dogAdopterReports;
            if (Objects.nonNull(reportStatus)) {
                dogAdopterReports = reportDogShelterRepository.findByDogAdopterAndReportStatus(dogAdopter, reportStatus);
            } else {
                dogAdopterReports = reportDogShelterRepository.findByDogAdopter(dogAdopter);
            }
            logger.info("The list reports of DogAdopter (id = {}, userName = {}) has been successfully loaded, found {} reports",
                    adopterId, dogAdopter.getNotNullUserName(), dogAdopterReports.size());
            reportAnimalDTOList = dogAdopterReports.stream()
                    .map(ReportDogShelter::toDTO)
                    .collect(Collectors.toList());
            return reportAnimalDTOList;
        } else if (shelterType == ShelterType.CAT_SHELTER) {
            CatAdopter catAdopter = adopterService.findCatAdopterById(adopterId);
            List<ReportCatShelter> catAdopterReports = reportCatShelterRepository.findByCatAdopterAndReportStatus(catAdopter, reportStatus);

            logger.info("The list reports of CatAdopter (id = {}, userName = {}) has been successfully loaded, found {} reports",
                    adopterId, catAdopter.getNotNullUserName(), catAdopterReports.size());
            reportAnimalDTOList = catAdopterReports.stream()
                    .map(ReportCatShelter::toDTO)
                    .collect(Collectors.toList());
            return reportAnimalDTOList;
        }
        logger.error("Shelter type {} not supported. The list users can not be created", shelterType);
        throw new ShelterNotFoundException(String.format("Shelter type: %s not supported yet", shelterType));
    }

    public ReportAnimalDTO saveReport(ReportAnimalDTO reportAnimalDTO) {
        ShelterType shelterType = reportAnimalDTO.getShelterType();
        Long chatId = reportAnimalDTO.getChatId();
        Integer adopterId = reportAnimalDTO.getAdopterId();
        ReportAnimalDTO resultAnimalDTO;
        if (chatId == null && adopterId == null) {
            throw new AdopterNotFoundException("Не задано ни одного идентификатора для поиска усыновителя (chatId && adopterId = null");
        }

        if (shelterType == ShelterType.DOG_SHELTER) {
            ReportDogShelter reportDogShelter = ReportDogShelter.fromDTO(reportAnimalDTO);
            DogAdopter dogAdopter;
            if (chatId != null) {
                dogAdopter = adopterService.findDogAdopterByChatIdAndStatus(chatId, AdopterStatus.PROBATION_ACTIVE);
            } else {
                dogAdopter = adopterService.findDogAdopterById(adopterId);
            }
            reportDogShelter.setDogAdopter(dogAdopter);

            // Передача бинарных данных изображения и имя файла
            reportDogShelter.setPhotoData(reportAnimalDTO.getPhotoData());
            reportDogShelter.setPhotoFilename(reportAnimalDTO.getPhotoFilename());

            reportDogShelter = reportDogShelterRepository.save(reportDogShelter);
            resultAnimalDTO = reportDogShelter.toDTO();
            logger.info("Report saved successfully: {}", resultAnimalDTO);
            return resultAnimalDTO;
        } else if (shelterType == ShelterType.CAT_SHELTER) {
            ReportCatShelter reportCatShelter = ReportCatShelter.fromDTO(reportAnimalDTO);
            CatAdopter catAdopter;
            if (chatId != null) {
                catAdopter = adopterService.findCatAdopterByChatIdAndStatuses(chatId,
                        List.of(AdopterStatus.PROBATION_ACTIVE, AdopterStatus.WAITING_REPORT));
            } else {
                catAdopter = adopterService.findCatAdopterById(adopterId);
            }
            reportCatShelter.setCatAdopter(catAdopter);

            // Передача бинарных данных изображения и имя файла
            reportCatShelter.setPhotoData(reportAnimalDTO.getPhotoData());
            reportCatShelter.setPhotoFilename(reportAnimalDTO.getPhotoFilename());

            reportCatShelter = reportCatShelterRepository.save(reportCatShelter);
            resultAnimalDTO = reportCatShelter.toDTO();

            logger.info("Report saved successfully: {}", resultAnimalDTO);
            return resultAnimalDTO;
        }
        logger.error("Shelter type {} not supported. Report cannot be saved", shelterType);
        throw new ShelterNotFoundException(String.format("Shelter type: %s not supported yet", shelterType));
    }

    public ReportAnimalDTO editStatusReport(ShelterType shelterType, Integer reportId, ReportStatus reportStatus) {
        ReportAnimalDTO reportAnimalDTO;
        if (shelterType == ShelterType.DOG_SHELTER) {
            ReportDogShelter reportDogShelter = getReportDogShelterById(reportId);
            reportDogShelter.setReportStatus(reportStatus);
            reportDogShelter = reportDogShelterRepository.save(reportDogShelter);
            reportAnimalDTO = reportDogShelter.toDTO();
            logger.info("Report status {} saved successful for report: {}", reportStatus, reportAnimalDTO);
            return reportDogShelter.toDTO();
        } else if (shelterType == ShelterType.CAT_SHELTER) {
            ReportCatShelter reportCatShelter = getReportCatShelterById(reportId);
            reportCatShelter.setReportStatus(reportStatus);

            reportCatShelter = reportCatShelterRepository.save(reportCatShelter);
            reportAnimalDTO = reportCatShelter.toDTO();
            logger.info("Report status {} saved successful for report: {}", reportStatus, reportAnimalDTO);
            return reportAnimalDTO;
        }
        logger.error("Shelter type {} not supported. Report can not be saved", shelterType);
        throw new ShelterNotFoundException(String.format("Shelter type: %s not supported yet", shelterType));
    }


    public ReportCatShelter getReportCatShelterById(Integer reportId) {
        return reportCatShelterRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException(
                        String.format("Отчет пользователя c id = %d not found in Cat Shelter database", reportId)));
    }

    public ReportDogShelter getReportDogShelterById(Integer reportId) {
        return reportDogShelterRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException(
                        String.format("Отчет пользователя c id = %d not found in Dog Shelter database", reportId)));
    }
}


