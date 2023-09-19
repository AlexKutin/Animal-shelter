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
    private Integer reportId;

    @Schema(description = "Статус отчета")
    private ReportStatus reportStatus;

    @Schema(description = "chatId усыновителя (информация от/для бота)")
    private Long chatId;

    @Schema(description = "Идентификатор усыновителя")
    private Integer adopterId;

    @Schema(description = "Текстовая часть отчета")
    private String description;

    @Schema(description = "Путь к файлу фотографии в отчете")
    private String filePath;

    @Schema(description = "Размер файла фотографии")
    private long fileSize;

    @Schema(description = "Тип файла фотографии")
    private String mediaType;

    @Schema(description = "Дата получения отчета")
    private LocalDate dateReport;

    public ShelterType getShelterType() {
        return shelterType;
    }

    public void setShelterType(ShelterType shelterType) {
        this.shelterType = shelterType;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public String toString() {
        return "ReportAnimalDTO{" +
                "shelterType=" + shelterType +
                ", reportId=" + reportId +
                ", reportStatus=" + reportStatus +
                ", chatId=" + chatId +
                ", adopterId=" + adopterId +
                ", description='" + description + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", mediaType='" + mediaType + '\'' +
                ", dateReport=" + dateReport +
                '}';
    }
}
