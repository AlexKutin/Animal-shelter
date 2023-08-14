package pro.sky.animalshelter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import pro.sky.animalshelter.model.Report;

import java.sql.Timestamp;

@Schema(name = "отчеты")
public class ReportDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(name = "идентификатор отчета")
    private Integer idReport;
    @Schema(name = "содержание отчета")
    private String description;
    @Schema(name = "Фото отчета")
    private String photo;
    @Schema(name = "дата и время отчета")
    private Timestamp dateTimeReport;

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

    public Timestamp getDateTimeReport() {
        return dateTimeReport;
    }

    public void setDateTimeReport(Timestamp dateTimeReportReport) {
        this.dateTimeReport = dateTimeReportReport;
    }

    public static ReportDTO fromReport(Report report) {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setIdReport(report.getIdReport());
        reportDTO.setDescription(report.getDescription());
        reportDTO.setPhoto(report.getPhoto());
        reportDTO.setDateTimeReport(report.getDateTimeReport());

        return reportDTO;
    }

    public Report toReport() {
        Report report = new Report();
        report.setIdReport(this.getIdReport());
        report.setDescription(this.getDescription());
        report.setPhoto(this.getPhoto());
        report.setDateTimeReport(this.getDateTimeReport());

        return report;
    }
}
