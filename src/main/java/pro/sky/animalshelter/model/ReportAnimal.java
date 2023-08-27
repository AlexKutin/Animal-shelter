package pro.sky.animalshelter.model;

import pro.sky.animalshelter.dto.ReportAnimalDTO;

import javax.persistence.*;
import java.time.LocalDate;

@MappedSuperclass
public abstract class ReportAnimal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_report")
    private Integer reportId;

    @Column(name = "description")
    private String description;

    @Column(name = "date_report", nullable = false)
    private LocalDate dateReport;
    //    private Timestamp dateTimeReport;

    @Column(name = "file_path")
    private String photoFilePath;

    @Column(name = "file_size")
    private Long photoFileSize;

    @Column(name = "media_type")
    private String photoMediaType;

    @Column(name = "report_status")
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

    public ReportAnimal() {
    }

    public abstract Adopter getAdopter();

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public Timestamp getDateTimeReport() {
//        return dateTimeReport;
//    }
//
//    public void setDateTimeReport(Timestamp dateTimeReport) {
//        this.dateTimeReport = dateTimeReport;
//    }

    public String getPhotoFilePath() {
        return photoFilePath;
    }

    public void setPhotoFilePath(String photoFilePath) {
        this.photoFilePath = photoFilePath;
    }

    public Long getPhotoFileSize() {
        return photoFileSize;
    }

    public void setPhotoFileSize(Long photoFileSize) {
        this.photoFileSize = photoFileSize;
    }

    public String getPhotoMediaType() {
        return photoMediaType;
    }

    public void setPhotoMediaType(String photoMediaType) {
        this.photoMediaType = photoMediaType;
    }

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
        reportAnimalDTO.setReportId(this.getReportId());
        reportAnimalDTO.setAdopterId(this.getAdopter().getAdopterId());
        reportAnimalDTO.setDescription(this.getDescription());
        reportAnimalDTO.setFilePath(this.getPhotoFilePath());
        reportAnimalDTO.setMediaType(this.getPhotoMediaType());
        reportAnimalDTO.setFileSize(this.getPhotoFileSize());
        reportAnimalDTO.setDateReport(this.getDateReport());
        reportAnimalDTO.setReportStatus(this.getReportStatus());
        reportAnimalDTO.setChatId(this.getAdopter().getChatId());

        return reportAnimalDTO;
    }

    protected void fillDataFromDTO(ReportAnimalDTO reportAnimalDTO) {
        this.setDescription(reportAnimalDTO.getDescription());

        this.setPhotoFilePath(reportAnimalDTO.getFilePath());
        this.setPhotoFileSize(reportAnimalDTO.getFileSize());
        this.setPhotoMediaType(reportAnimalDTO.getMediaType());

        this.setDateReport(reportAnimalDTO.getDateReport());
        this.setReportStatus(reportAnimalDTO.getReportStatus());
    }
}
