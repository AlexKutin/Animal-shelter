package pro.sky.animalshelter.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "report_cat_shelter")
public class ReportCatShelter {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_report")
        private Integer idReport;
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
        private UserCatShelter userDogShelter;
        private String description;
        private String photo;
        private LocalDate dateReport;

    public Integer getIdReport() {
        return idReport;
    }

    public void setIdReport(Integer idReport) {
        this.idReport = idReport;
    }

    public UserCatShelter getUserDogShelter() {
        return userDogShelter;
    }

    public void setUserDogShelter(UserCatShelter userDogShelter) {
        this.userDogShelter = userDogShelter;
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

    @Override
    public String toString() {
        return "ReportCatShelter{" +
                "idReport=" + idReport +
                ", userDogShelter=" + userDogShelter +
                ", description='" + description + '\'' +
                ", photo='" + photo + '\'' +
                ", dateReport=" + dateReport +
                '}';
    }
}

