package pro.sky.animalshelter.model;

import pro.sky.animalshelter.dto.ReportAnimalDTO;

import javax.persistence.*;

@Entity
@Table(name = "report_dog_shelter")
public class ReportDogShelter extends ReportAnimal {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopter_id", referencedColumnName = "adopter_id", nullable = false)
    private DogAdopter dogAdopter;

    @Override
    public DogAdopter getAdopter() {
        return dogAdopter;
    }

    public void setDogAdopter(DogAdopter dogAdopter) {
        this.dogAdopter = dogAdopter;
    }

    @Override
    public ReportAnimalDTO toDTO() {
        ReportAnimalDTO reportAnimalDTO = super.toDTO();
        reportAnimalDTO.setShelterType(ShelterType.DOG_SHELTER);
        return reportAnimalDTO;
    }

    @Override
    public String toString() {
        return "ReportDogShelter{" +
                "reportId=" + getReportId() +
                ", adopter=" + getAdopter().getNotNullUserName() +
                ", description='" + getDescription() + '\'' +
                ", photoFilename='" + getPhotoFilename() + '\'' +
                ", dateReport=" + getDateReport() +
                ", reportStatus=" + getReportStatus() + "Photo" + getPhotoData() +
                '}';
    }

    public static ReportDogShelter fromDTO(ReportAnimalDTO reportAnimalDTO) {
        ReportDogShelter reportDogShelter = new ReportDogShelter();
        reportDogShelter.setDescription(reportAnimalDTO.getDescription());
        reportDogShelter.setPhotoFilename(reportAnimalDTO.getPhotoFilename());
        reportDogShelter.setDateReport(reportAnimalDTO.getDateReport());
        reportDogShelter.setReportStatus(reportAnimalDTO.getReportStatus());
        reportDogShelter.setPhotoData(reportAnimalDTO.getPhotoData());
        return reportDogShelter;

    }
}