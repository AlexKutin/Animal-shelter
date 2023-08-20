package pro.sky.animalshelter.model;

import pro.sky.animalshelter.dto.ReportAnimalDTO;

import javax.persistence.*;
import java.time.LocalDate;

@MappedSuperclass
public abstract class ReportAnimal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_report")
    private Integer idReport;

    @Column(name = "description")
    private String description;
    private String photo;

    @Column(name = "date_report", nullable = false)
    private LocalDate dateReport;
//    private Timestamp dateTimeReport;

    @Column(name = "report_status")
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

    public ReportAnimal() {
    }

//    public ReportAnimal(Integer userShelter, String description, String photo, Timestamp dateTimeReport) {
//        this.userShelter = userShelter;
//        this.description = description;
//        this.photo = photo;
//        this.dateTimeReport = dateTimeReport;
//    }

    public abstract Adopter getAdopter();

    public Integer getIdReport() {
        return idReport;
    }

    public void setIdReport(Integer idReport) {
        this.idReport = idReport;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

//    public Timestamp getDateTimeReport() {
//        return dateTimeReport;
//    }
//
//    public void setDateTimeReport(Timestamp dateTimeReport) {
//        this.dateTimeReport = dateTimeReport;
//    }

    public LocalDate getDateReport() {
        return dateReport;
    }

    public void setDateReport(LocalDate dateReport) {
        this.dateReport = dateReport;
    }

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public ReportAnimalDTO toDTO() {
        ReportAnimalDTO reportAnimalDTO = new ReportAnimalDTO();
        reportAnimalDTO.setIdReport(this.getIdReport());
        reportAnimalDTO.setAdopterId(this.getAdopter().getAdopterId());
        reportAnimalDTO.setDescription(this.getDescription());
        reportAnimalDTO.setPhoto(this.getPhoto());
        reportAnimalDTO.setDateReport(this.getDateReport());
        reportAnimalDTO.setReportStatus(this.getReportStatus());

        return reportAnimalDTO;
    }


}
