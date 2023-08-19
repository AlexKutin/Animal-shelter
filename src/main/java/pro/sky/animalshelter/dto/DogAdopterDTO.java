package pro.sky.animalshelter.dto;
import java.time.LocalDateTime;

public class DogAdopterDTO {
    private Integer adoptionId;
    private LocalDateTime adoptionDate;

    public DogAdopterDTO(Integer adoptionId, LocalDateTime adoptionDate) {
        this.adoptionId = adoptionId;
        this.adoptionDate = adoptionDate;
    }

    public DogAdopterDTO() {
    }

    public Integer getAdoptionId() {
        return adoptionId;
    }

    public void setAdoptionId(Integer adoptionId) {
        this.adoptionId = adoptionId;
    }

    public LocalDateTime getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(LocalDateTime adoptionDate) {
        this.adoptionDate = adoptionDate;
    }
}
