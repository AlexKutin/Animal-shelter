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

import java.util.Collection;
import java.util.List;
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

    public List<ReportAnimalDTO> getReportsDogSheltersByAdopterAndStatus(Integer adopterId, Collection<ReportStatus> reportStatuses) {
        List<ReportDogShelter> dogAdopterReports;
        if (adopterId == null && (reportStatuses == null || reportStatuses.isEmpty())) {
            dogAdopterReports = reportDogShelterRepository.findAll();
            logger.info("The list reports of DogAdopter for all users has been successfully loaded, found {} reports",
                    dogAdopterReports.size());
            return toAnimalDTOList(dogAdopterReports);
        }
        if (adopterId != null) {
            DogAdopter dogAdopter = adopterService.findDogAdopterById(adopterId);
            if (reportStatuses != null && !reportStatuses.isEmpty()) {
                dogAdopterReports = reportDogShelterRepository.findByDogAdopterAndReportStatusIn(dogAdopter, reportStatuses);
            } else {
                dogAdopterReports = reportDogShelterRepository.findByDogAdopter(dogAdopter);
            }
            logger.info("The list reports of DogAdopter (id = {}, userName = {}) has been successfully loaded, found {} reports",
                    adopterId, dogAdopter.getNotNullUserName(), dogAdopterReports.size());
            return toAnimalDTOList(dogAdopterReports);
        }
        dogAdopterReports = reportDogShelterRepository.findByReportStatusIn(reportStatuses);
        logger.info("The list reports of DogAdopter for all users with reportStatus = {} has been successfully loaded, found {} reports",
                reportStatuses, dogAdopterReports.size());
        return toAnimalDTOList(dogAdopterReports);
    }

    private <T extends ReportAnimal> List<ReportAnimalDTO> toAnimalDTOList(List<T> dogAdopterReports) {
        return dogAdopterReports.stream()
                .map(ReportAnimal::toDTO)
                .collect(Collectors.toList());
    }

    public List<ReportAnimalDTO> getReportsCatShelterByAdopterAndStatus(Integer adopterId, Collection<ReportStatus> reportStatuses) {
        List<ReportCatShelter> catAdopterReports;
        if (adopterId == null && (reportStatuses == null || reportStatuses.isEmpty())) {
            catAdopterReports = reportCatShelterRepository.findAll();
            logger.info("The list reports of CatAdopter for all users has been successfully loaded, found {} reports",
                    catAdopterReports.size());
            return toAnimalDTOList(catAdopterReports);
        }
        if (adopterId != null) {
            CatAdopter catAdopter = adopterService.findCatAdopterById(adopterId);
            if (reportStatuses != null && !reportStatuses.isEmpty()) {
                catAdopterReports = reportCatShelterRepository.findByCatAdopterAndReportStatusIn(catAdopter, reportStatuses);
            } else {
                catAdopterReports = reportCatShelterRepository.findByCatAdopter(catAdopter);
            }
            logger.info("The list reports of CatAdopter (id = {}, userName = {}) has been successfully loaded, found {} reports",
                    adopterId, catAdopter.getNotNullUserName(), catAdopterReports.size());
            return toAnimalDTOList(catAdopterReports);
        }
        catAdopterReports = reportCatShelterRepository.findByReportStatusIn(reportStatuses);
        logger.info("The list reports of CatAdopter for all users with reportStatus = {} has been successfully loaded, found {} reports",
                reportStatuses, catAdopterReports.size());
        return toAnimalDTOList(catAdopterReports);
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
//            reportDogShelter.setPhotoData(reportAnimalDTO.getPhotoData());
//            reportDogShelter.setPhotoFilename(reportAnimalDTO.getPhotoFilename());

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
//            reportCatShelter.setPhotoData(reportAnimalDTO.getPhotoData());
//            reportCatShelter.setPhotoFilename(reportAnimalDTO.getPhotoFilename());

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


