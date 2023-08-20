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
}
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pro.sky.animalshelter.dto.ReportDTO;
import pro.sky.animalshelter.dto.UserShelterDTO;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.ReportCatShelterRepository;
import pro.sky.animalshelter.repository.ReportDogShelterRepository;
import pro.sky.animalshelter.repository.UserCatShelterRepository;
import pro.sky.animalshelter.repository.UsersRepository;

import java.sql.Timestamp;

@Service
public class ReportService {
    private final Logger logger = LoggerFactory.getLogger(ReportService.class);

    private final UserCatShelter userCatShelter;
    private final UsersRepository usersRepository;
    private final Report report;
    private final UserShelterService userShelterService;
    private final UserShelterDTO userShelterDTO;
    private final ReportCatShelterRepository reportCatShelterRepository;
    private final ReportDogShelterRepository reportDogShelterRepository;


    @Autowired

    public ReportService(UserCatShelter userCatShelter, UserCatShelterRepository userCatShelterRepository, UsersRepository usersRepository, Report report, UserShelterService userShelterService, UserShelterDTO userShelterDTO, ReportCatShelterRepository reportCatShelterRepository, ReportDogShelterRepository reportDogShelterRepository) {
        this.userCatShelter = userCatShelter;
        this.usersRepository = usersRepository;
        this.report = report;
        this.userShelterService = userShelterService;
        this.userShelterDTO = userShelterDTO;
        this.reportCatShelterRepository = reportCatShelterRepository;
        this.reportDogShelterRepository = reportDogShelterRepository;
    }


    public void saveReport(ReportDTO reportDTO, Integer userShelter, String description, String photo, Timestamp dateTimeReport) throws IllegalArgumentException {
        UserShelterDTO userShelterDTO = UserShelterDTO.fromUserShelter(reportDTO.getUserShelter());
        try {
            if (userShelterDTO == null) {
                System.out.println("Этого пользователя нет в базе усыновителей");

            } else if (userShelterDTO.getShelterType() == ShelterType.DOG_SHELTER) {
                ReportDogShelter reportDogShelter = new ReportDogShelter(userShelter, description, photo, dateTimeReport);
                reportDogShelterRepository.save(reportDogShelter);
            } else if (userShelterDTO.getShelterType() == ShelterType.CAT_SHELTER) {
                ReportCatShelter reportCatShelter = new ReportCatShelter(userShelter, description, photo, dateTimeReport);
                reportCatShelterRepository.save(reportCatShelter);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Не корректное значение");
        }
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

