package pro.sky.animalshelter.model;

import pro.sky.animalshelter.dto.ReportAnimalDTO;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "report_dog_shelter")
public class ReportDogShelter extends ReportAnimal {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id_report")
//    private Integer idReport;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
//    private UserDogShelter userDogShelter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopter_id", referencedColumnName = "adopter_id", nullable = false)
    private DogAdopter dogAdopter;

//    private String description;
//    private String photo;
//    private LocalDate dateReport;
//
//    public Integer getIdReport() {
//        return idReport;
//    }
//
//    public void setIdReport(Integer idReport) {
//        this.idReport = idReport;
//    }

    @Override
    public DogAdopter getAdopter() {
        return dogAdopter;
    }

    public void setDogAdopter(DogAdopter dogAdopter) {
        this.dogAdopter = dogAdopter;
    }

//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(String photo) {
//        this.photo = photo;
//    }
//
//    public LocalDate getDateReport() {
//        return dateReport;
//    }
//
//    public void setDateReport(LocalDate dateReport) {
//        this.dateReport = dateReport;
//    }

    @Override
    public ReportAnimalDTO toDTO() {
        ReportAnimalDTO reportAnimalDTO = super.toDTO();
        reportAnimalDTO.setShelterType(ShelterType.DOG_SHELTER);
        return reportAnimalDTO;
    }

    @Override
    public String toString() {
        return "ReportDogShelter{" +
                "idReport=" + getIdReport() +
                ", dogAdopter=" + dogAdopter +
                ", description='" + getDescription() + '\'' +
                ", photo='" + getPhoto() + '\'' +
                ", dateReport=" + getDateReport() +
                ", reportStatus=" + getReportStatus() +
                '}';
    }
}