package pro.sky.animalshelter.model;

import pro.sky.animalshelter.dto.ReportAnimalDTO;

import javax.persistence.*;

@Entity
@Table(name = "report_cat_shelter")
public class ReportCatShelter extends ReportAnimal {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopter_id", referencedColumnName = "adopter_id", nullable = false)
    private CatAdopter catAdopter;

    @Override
    public CatAdopter getAdopter() {
        return catAdopter;
    }

    public void setCatAdopter(CatAdopter catAdopter) {
        this.catAdopter = catAdopter;
    }

    @Override
    public ReportAnimalDTO toDTO() {
        ReportAnimalDTO reportAnimalDTO = super.toDTO();
        reportAnimalDTO.setShelterType(ShelterType.CAT_SHELTER);
        return reportAnimalDTO;
    }

    @Override
    public String toString() {
        return "ReportCatShelter{" +
                "idReport=" + getIdReport() +
                ", catAdopter=" + catAdopter +
                ", description='" + getDescription() + '\'' +
                ", photo='" + getPhoto() + '\'' +
                ", dateReport=" + getDateReport() +
                ", reportStatus=" + getReportStatus() +
                '}';
    }
}

