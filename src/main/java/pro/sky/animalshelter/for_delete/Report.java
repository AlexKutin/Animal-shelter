package pro.sky.animalshelter.for_delete;

import javax.persistence.*;
import java.sql.Timestamp;

public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_report")
    private Integer idReport;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private Integer userShelter;
    private String description;
    private String photo;
    @Column(name = "date_report")
    private Timestamp dateTimeReport;

    public Report(Integer userShelter, String description, String photo, Timestamp dateTimeReport) {
        this.userShelter = userShelter;
        this.description = description;
        this.photo = photo;
        this.dateTimeReport = dateTimeReport;
    }

    public Integer getIdReport() {
        return idReport;
    }

    public void setIdReport(Integer idReport) {
        this.idReport = idReport;
    }

    public Integer getUserShelter() {
        return userShelter;
    }

    public void setUserShelter(Integer userShelter) {
        this.userShelter = userShelter;
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

    public void setDateTimeReport(Timestamp dateTimeReport) {
        this.dateTimeReport = dateTimeReport;
    }

    @Override
    public String toString() {
        return "Report{" +
                "idReport=" + idReport +
                ", userShelter=" + userShelter +
                ", description='" + description + '\'' +
                ", photo='" + photo + '\'' +
                ", dateTimeReport=" + dateTimeReport +
                '}';
    }

    public Report put(Report report) {
        report.put(report);
        return report;
    }
}
