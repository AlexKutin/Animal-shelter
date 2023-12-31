package pro.sky.animalshelter.model;

import pro.sky.animalshelter.dto.AnimalAdopterDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@MappedSuperclass
public abstract class Adopter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adopter_id")
    private Integer adopterId;
    @Column(name = "chat_id", unique = true)
    private Long chatId;

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

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public abstract Animal getAnimal();

    public AnimalAdopterDTO toDTO() {
        AnimalAdopterDTO animalAdopterDTO = new AnimalAdopterDTO();

        animalAdopterDTO.setAdopterId(this.getAdopterId());

        UserShelter userShelter = this.getUser();
        animalAdopterDTO.setUserId(userShelter.getUserId());
        animalAdopterDTO.setUserName(this.getNotNullUserName());
        animalAdopterDTO.setAdoptionDate(this.getAdoptionDate().
                format(DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm")));
        animalAdopterDTO.setAdopterStatus(this.getAdopterStatus());
        animalAdopterDTO.setEndProbationDate(this.getEndProbationDate().
                format(DateTimeFormatter.ofPattern("dd:MM:yyyy")));
        animalAdopterDTO.setChatId(this.chatId);

        return animalAdopterDTO;
    }

    public String getNotNullUserName() {
        UserShelter userShelter = this.getUser();
        return Optional.ofNullable(userShelter.getUserName())
                .orElse(userShelter.getFirstName() + " " + userShelter.getLastName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Adopter)) return false;
        Adopter adopter = (Adopter) o;
        return adopterId.equals(adopter.adopterId) && chatId.equals(adopter.chatId) && adoptionDate.equals(adopter.adoptionDate) && adopterStatus == adopter.adopterStatus && endProbationDate.equals(adopter.endProbationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adopterId, chatId, adoptionDate, adopterStatus, endProbationDate);
    }
}
