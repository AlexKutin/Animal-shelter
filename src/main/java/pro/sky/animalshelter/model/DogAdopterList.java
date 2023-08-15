package pro.sky.animalshelter.model;

import pro.sky.animalshelter.dto.DogAdopterListDTO;
import pro.sky.animalshelter.dto.DogDTO;

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
    private UserDogShelter adopterId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id")
    private Dog dogId;
    @Column(name = "adoption_date")
    private LocalDateTime adoptionDate;

    public DogAdopterList() {
    }

    public DogAdopterList(Integer adoptionId, UserDogShelter adopterId, Dog dogId, LocalDateTime adoptionDate) {
        this.adoptionId = adoptionId;
        this.adopterId = adopterId;
        this.dogId = dogId;
        this.adoptionDate = adoptionDate;
    }

    public Integer getAdoptionId() {
        return adoptionId;
    }

    public void setAdoptionId(Integer adoptionId) {
        this.adoptionId = adoptionId;
    }

    public UserDogShelter getAdopterId() {
        return adopterId;
    }

    public void setAdopterId(UserDogShelter adopterId) {
        this.adopterId = adopterId;
    }

    public Dog getDogId() {
        return dogId;
    }

    public void setDogId(Dog dogId) {
        this.dogId = dogId;
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
