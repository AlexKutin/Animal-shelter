package pro.sky.animalshelter.model;

import pro.sky.animalshelter.dto.ReportAnimalDTO;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@MappedSuperclass
public abstract class ReportAnimal implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_report")
    private Integer reportId;

    @Column(name = "description")
    private String description;

    @Column(name = "date_report", nullable = false)
    private LocalDate dateReport;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportAnimal)) return false;
        ReportAnimal that = (ReportAnimal) o;
        return Objects.equals(reportId, that.reportId) && Objects.equals(description, that.description) && Objects.equals(dateReport, that.dateReport) && Objects.equals(photoFilePath, that.photoFilePath) && Objects.equals(photoFileSize, that.photoFileSize) && Objects.equals(photoMediaType, that.photoMediaType) && reportStatus == that.reportStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportId, description, dateReport, photoFilePath, photoFileSize, photoMediaType, reportStatus);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
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
