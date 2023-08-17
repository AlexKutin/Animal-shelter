package pro.sky.animalshelter.dto;
import pro.sky.animalshelter.model.Dog;
import pro.sky.animalshelter.model.UserDogShelter;
import java.time.LocalDateTime;

public class DogAdopterListDTO {
    private Integer adoptionId;
    private LocalDateTime adoptionDate;

    public DogAdopterListDTO(Integer adoptionId, LocalDateTime adoptionDate) {
        this.adoptionId = adoptionId;
        this.adoptionDate = adoptionDate;
    }

    public DogAdopterListDTO() {
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
