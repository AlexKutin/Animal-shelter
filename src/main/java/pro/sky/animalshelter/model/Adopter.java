package pro.sky.animalshelter.model;

import org.jetbrains.annotations.NotNull;
import pro.sky.animalshelter.dto.AnimalAdopterDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@MappedSuperclass
public abstract class Adopter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adopter_id")
    private Integer adopterId;

    @Column(name = "adoption_date")
    private LocalDateTime adoptionDate;

    @Column(name = "adopter_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AdopterStatus adopterStatus;

    @Column(name = "end_probation_date")
    private LocalDateTime endProbationDate;

    public Integer getAdopterId() {
        return adopterId;
    }

    public void setAdopterId(Integer adopterId) {
        this.adopterId = adopterId;
    }

    public abstract UserShelter getUser();

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

    public AnimalAdopterDTO toDTO() {
        AnimalAdopterDTO animalAdopterDTO = new AnimalAdopterDTO();
        animalAdopterDTO.setShelterType(ShelterType.DOG_SHELTER);
        animalAdopterDTO.setAdopterId(this.getAdopterId());

        UserShelter userShelter = this.getUser();
        animalAdopterDTO.setUserId(userShelter.getUserId());
        animalAdopterDTO.setUserName(getNotNullUserName(userShelter));
        animalAdopterDTO.setAdoptionDate(this.getAdoptionDate().
                format(DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm")));
        animalAdopterDTO.setAdopterStatus(this.getAdopterStatus());
        animalAdopterDTO.setEndProbationDate(this.getEndProbationDate().
                format(DateTimeFormatter.ofPattern("dd:MM:yyyy")));

        return animalAdopterDTO;
    }

    @NotNull
    public static String getNotNullUserName(UserShelter userShelter) {
        return Optional.of(userShelter.getUserName())
                .orElse(userShelter.getFirstName() + " " + userShelter.getLastName());
    }

    public String getNotNullUserName() {
        UserShelter userShelter = this.getUser();
        return Optional.of(userShelter.getUserName())
                .orElse(userShelter.getFirstName() + " " + userShelter.getLastName());
    }

//    protected AnimalAdopterDTO fillUserInfo(UserShelter userShelter, AnimalAdopterDTO animalAdopterDTO) {
//        animalAdopterDTO.setUserId(userShelter.getUserId());
//        animalAdopterDTO.setUserName(Optional.of(userShelter.getUserName())
//                .orElse(userShelter.getFirstName() + " " + userShelter.getLastName()));
//    }
}
