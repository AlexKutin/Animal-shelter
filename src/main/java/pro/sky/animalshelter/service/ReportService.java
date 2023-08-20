package pro.sky.animalshelter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.dto.ReportAnimalDTO;
import pro.sky.animalshelter.exception.ShelterNotFoundException;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.ReportCatShelterRepository;
import pro.sky.animalshelter.repository.ReportDogShelterRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {
    Logger logger = LoggerFactory.getLogger(ReportService.class);
    private final ReportDogShelterRepository reportDogShelterRepository;
    private final ReportCatShelterRepository reportCatShelterRepository;
    private final AdopterService adopterService;


    public ReportService(ReportDogShelterRepository reportDogShelterRepository, ReportCatShelterRepository reportCatShelterRepository, AdopterService adopterService) {
        this.reportDogShelterRepository = reportDogShelterRepository;
        this.reportCatShelterRepository = reportCatShelterRepository;
        this.adopterService = adopterService;
    }

    public List<ReportAnimalDTO> getReportsByAdopterAndStatus(ShelterType shelterType, Integer adopterId, ReportStatus reportStatus) {
        List<ReportAnimalDTO> reportAnimalDTOList;
        if (shelterType == ShelterType.DOG_SHELTER) {
            DogAdopter dogAdopter = adopterService.findDogAdopterById(adopterId);
            List<ReportDogShelter> dogAdopterReports = reportDogShelterRepository.findByDogAdopterAndReportStatus(dogAdopter, reportStatus);

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

    //    public void saveReport(ReportDTO reportDTO, Integer userShelter, String description, String photo, Timestamp dateTimeReport) throws IllegalArgumentException {
    public ReportAnimalDTO saveReport(ReportAnimalDTO reportAnimalDTO) {
        ShelterType shelterType = reportAnimalDTO.getShelterType();
        if (shelterType == ShelterType.DOG_SHELTER) {
            ReportDogShelter reportDogShelter = ReportDogShelter.fromDTO(reportAnimalDTO);
            DogAdopter dogAdopter = adopterService.findDogAdopterById(reportAnimalDTO.getAdopterId());
            reportDogShelter.setDogAdopter(dogAdopter);

            reportDogShelter = reportDogShelterRepository.save(reportDogShelter);
            return reportDogShelter.toDTO();
        } else if (shelterType == ShelterType.CAT_SHELTER) {
            ReportCatShelter reportCatShelter = ReportCatShelter.fromDTO(reportAnimalDTO);
            reportCatShelter.setCatAdopter(adopterService.findCatAdopterById(reportAnimalDTO.getAdopterId()));

            reportCatShelter = reportCatShelterRepository.save(reportCatShelter);
            return reportCatShelter.toDTO();
        }
        logger.error("Shelter type {} not supported. The list users can not be created", shelterType);
        throw new ShelterNotFoundException(String.format("Shelter type: %s not supported yet", shelterType));
    }
}

/*
    public Report findReportByid(Integer userId) {
        return ReportDTO.fromReport();
    }
    public Report findReportByid(Integer userId) {
        return ReportDTO.fromReport();
    }

    ReportDTO updateReportByIdReport(Integer idReport, Report report) {
        Report report1 = ReportCatShelterRepository.getReportById(idReport).put(report);

        return ReportDTO.fromReport();
    }
}*/

