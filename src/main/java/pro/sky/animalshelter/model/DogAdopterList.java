package pro.sky.animalshelter.model;

import pro.sky.animalshelter.dto.DogAdopterListDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dog_adopter_list")
public class DogAdopterList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adoption_id")
    private Integer adoptionId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopter_id")
    private UserDogShelter adopter;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id")
    private Dog dog;
    @Column(name = "adoption_date")
    private LocalDateTime adoptionDate;

    public DogAdopterList() {
    }

    public DogAdopterList(Integer adoptionId, UserDogShelter adopterId, Dog dogId, LocalDateTime adoptionDate) {
        this.adoptionId = adoptionId;
        this.adopter = adopterId;
        this.dog = dogId;
        this.adoptionDate = adoptionDate;
    }

    public Integer getAdoptionId() {
        return adoptionId;
    }

    public void setAdoptionId(Integer adoptionId) {
        this.adoptionId = adoptionId;
    }

    public UserDogShelter getAdopterId() {
        return adopter;
    }

    public void setAdopterId(UserDogShelter adopterId) {
        this.adopter = adopterId;
    }

    public Dog getDogId() {
        return dog;
    }

    public void setDogId(Dog dogId) {
        this.dog = dogId;
    }

    public LocalDateTime getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(LocalDateTime adoptionDate) {
        this.adoptionDate = adoptionDate;
    }

    public static DogAdopterList fromDTO(DogAdopterListDTO dogAdopterListDTO) {
        return new DogAdopterList(dogAdopterListDTO.getAdoptionId(),
                null,
                null,
                dogAdopterListDTO.getAdoptionDate());
    }

    public DogAdopterListDTO toDTO() {
        return new DogAdopterListDTO(this.adoptionId, this.adoptionDate);
    }
}
