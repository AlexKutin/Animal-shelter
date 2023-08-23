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

    public static ReportCatShelter fromDTO(ReportAnimalDTO reportAnimalDTO) {
        ReportCatShelter reportCatShelter = new ReportCatShelter();
        reportCatShelter.setDescription(reportAnimalDTO.getDescription());
        reportCatShelter.setPhotoData(reportAnimalDTO.getPhotoData());
        reportCatShelter.setDateReport(reportAnimalDTO.getDateReport());
        reportCatShelter.setReportStatus(reportAnimalDTO.getReportStatus());
        reportCatShelter.setPhotoFilename(reportAnimalDTO.getPhotoFilename());
        return reportCatShelter;
    }

    @Override
    public String toString() {
        return "ReportCatShelter{" +
                "reportId=" + getReportId() +
                ", adopter=" + getAdopter().getNotNullUserName() +
                ", description='" + getDescription() + '\'' +
//                ", photo='" + getPhotoData() + '\'' +
                "photoFileName=" + getPhotoFilename()+
                ", dateReport=" + getDateReport() +
                ", reportStatus=" + getReportStatus() +
                '}';
    }
}

