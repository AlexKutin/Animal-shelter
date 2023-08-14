package pro.sky.animalshelter.model;

import liquibase.structure.core.Data;
import org.glassfish.grizzly.http.util.TimeStamp;

import javax.persistence.*;

@MappedSuperclass
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_report")
    private Integer idReport;
    private String description;
    private String photo;
    @Column(name = "date_report")
    private TimeStamp dateTimeReport;

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

    public TimeStamp getDateTimeReport() {
        return dateTimeReport;
    }

    public void setDateTimeReport(TimeStamp dateTimeReport) {
        this.dateTimeReport = dateTimeReport;
    }

    @Override
    public String toString() {
        return "Report{" +
                "idReport=" + idReport +
                ", description='" + description + '\'' +
                ", photo='" + photo + '\'' +
                ", dateReport=" + dateTimeReport +
                '}';
    }

    public Report put(Report report) {
        report.put(report);
        return report;
    }
}
