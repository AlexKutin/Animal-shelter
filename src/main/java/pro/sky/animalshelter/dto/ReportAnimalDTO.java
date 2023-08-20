package pro.sky.animalshelter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import pro.sky.animalshelter.model.ReportStatus;
import pro.sky.animalshelter.model.ShelterType;

import java.time.LocalDate;

@Schema(description = "Отчет усыновителя животного")
public class ReportAnimalDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Тип приюта")
    private ShelterType shelterType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Идентификатор отчета")
    private Integer idReport;

    @Schema(description = "Идентификатор усыновителя")
    private Integer adopterId;

    @Schema(description = "Текстовая часть отчета")
    private String description;

    @Schema(description = "Путь к фотографии отчета")
    private String photo;

    @Schema(description = "Дата получения отчета")
    private LocalDate dateReport;

    @Schema(description = "Статус отчета")
    private ReportStatus reportStatus;

    public ShelterType getShelterType() {
        return shelterType;
    }

    public void setShelterType(ShelterType shelterType) {
        this.shelterType = shelterType;
    }

    public Integer getIdReport() {
        return idReport;
    }

    public void setIdReport(Integer idReport) {
        this.idReport = idReport;
    }

    public Integer getAdopterId() {
        return adopterId;
    }

    public void setAdopterId(Integer adopterId) {
        this.adopterId = adopterId;
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
    public String toString() {
        return "ReportAnimalDTO{" +
                "shelterType=" + shelterType +
                ", idReport=" + idReport +
                ", adopterId=" + adopterId +
                ", description='" + description + '\'' +
                ", photo='" + photo + '\'' +
                ", dateReport=" + dateReport +
                ", reportStatus=" + reportStatus +
                '}';
    }
}
