package pro.sky.animalshelter.service;

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

