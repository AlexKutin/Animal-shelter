package pro.sky.animalshelter.model;

import pro.sky.animalshelter.dto.AnimalAdopterDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Entity
@Table(name = "dog_adopter_list")
public class DogAdopter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adopter_id")
    private Integer adopterId;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserDogShelter user;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "dog_id", referencedColumnName = "dog_id")
    private Dog dog;
    @Column(name = "adoption_date")
    private LocalDateTime adoptionDate;

    @Column(name = "adopter_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AdopterStatus adopterStatus;

    @Column(name = "end_probation_date")
    private LocalDateTime endProbationDate;

    public DogAdopter() {
    }

    /*public DogAdopter(Integer adoptionId, UserDogShelter user, Dog dog, LocalDateTime adoptionDate) {
        this.adoptionId = adoptionId;
        this.user = user;
        this.dog = dog;
        this.adoptionDate = adoptionDate;
    }*/

    public Integer getAdopterId() {
        return adopterId;
    }

    public void setAdopterId(Integer adopterId) {
        this.adopterId = adopterId;
    }

    public UserDogShelter getUser() {
        return user;
    }

    public void setUser(UserDogShelter userDogShelter) {
        this.user = userDogShelter;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public LocalDateTime getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(LocalDateTime adoptionDate) {
        this.adoptionDate = adoptionDate;
    }

    public AdopterStatus getAdopterStatus() {
        return adopterStatus;
    }

    public void setAdopterStatus(AdopterStatus adopterStatus) {
        this.adopterStatus = adopterStatus;
    }

    public LocalDateTime getEndProbationDate() {
        return endProbationDate;
    }

    public void setEndProbationDate(LocalDateTime endProbationDate) {
        this.endProbationDate = endProbationDate;
    }

    //    public static DogAdopter fromDTO(DogAdopterDTO dogAdopterDTO) {
//        return new DogAdopter(dogAdopterDTO.getAdoptionId(),
//                null,
//                null,
//                dogAdopterDTO.getAdoptionDate());
//    }

    public AnimalAdopterDTO toDTO() {
        AnimalAdopterDTO animalAdopterDTO = new AnimalAdopterDTO();
        animalAdopterDTO.setShelterType(ShelterType.DOG_SHELTER);
        animalAdopterDTO.setAnimalId(this.getDog().getDogId());
        animalAdopterDTO.setAnimalName(this.getDog().getDogName());
        animalAdopterDTO.setAdopterId(this.getAdopterId());

        UserDogShelter userDogShelter = this.getUser();
        animalAdopterDTO.setUserId(userDogShelter.getUserId());
        animalAdopterDTO.setUserName(Optional.of(userDogShelter.getUserName())
                .orElse(userDogShelter.getFirstName() + " " + userDogShelter.getLastName()));
        animalAdopterDTO.setAdoptionDate(this.getAdoptionDate().
                format(DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm")));
        animalAdopterDTO.setAdopterStatus(this.getAdopterStatus());
        animalAdopterDTO.setEndProbationDate(this.getEndProbationDate().
                format(DateTimeFormatter.ofPattern("dd:MM:yyyy")));

        return animalAdopterDTO;
    }
}
