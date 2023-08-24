package pro.sky.animalshelter.model;

import org.hibernate.annotations.Type;
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
//    private String photo;

    @Column(name = "date_report", nullable = false)
    private LocalDate dateReport;
    //    private Timestamp dateTimeReport;

    @Lob // Добавляем аннотацию для бинарных данных (BLOB)
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "photo_data")
    @Basic(fetch = FetchType.LAZY)
    private byte[] photoData;

    @Column(name = "photo_filename")
    private String photoFilename;
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

//    public String getPhoto() {
//        return photo;
//    }

//    public void setPhoto(String photo) {
//        this.photo = photo;
//    }

//    public Timestamp getDateTimeReport() {
//        return dateTimeReport;
//    }
//
//    public void setDateTimeReport(Timestamp dateTimeReport) {
//        this.dateTimeReport = dateTimeReport;
//    }

    public byte[] getPhotoData() {
        return photoData;
    }

    public void setPhotoData(byte[] photoData) {
        this.photoData = photoData;
    }

    public String getPhotoFilename() {
        return photoFilename;
    }

    public void setPhotoFilename(String photoFilename) {
        this.photoFilename = photoFilename;
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
        reportAnimalDTO.setPhotoFilename(this.getPhotoFilename());
        reportAnimalDTO.setDateReport(this.getDateReport());
        reportAnimalDTO.setReportStatus(this.getReportStatus());
        reportAnimalDTO.setPhotoData(this.getPhotoData());
        reportAnimalDTO.setChatId(this.getAdopter().getUser().getChatId());
        return reportAnimalDTO;
    }
}
