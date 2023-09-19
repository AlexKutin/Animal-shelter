package pro.sky.animalshelter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import pro.sky.animalshelter.model.AdopterStatus;
import pro.sky.animalshelter.model.ShelterType;

import java.util.Objects;

@Schema(description = "Усыновитель животного")
public class AnimalAdopterDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Тип приюта")
    private ShelterType shelterType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Идентификатор усыновителя")
    private Integer adopterId;

    @Schema(description = "Идентификатор пользователя")
    private Integer userId;
    @Schema(description = "Идентификатор чата")
    private Long chatId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Имя пользователя")
    private String userName;

    @Schema(description = "Идентификатор животного")
    private Integer animalId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Имя животного")
    private String animalName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Дата и время усыновления животного")
    private String adoptionDate;

    @Schema(description = "Статус испытательного срока")
    private AdopterStatus adopterStatus;

    @Schema(description = "Дата окончания испытательного срока")
    private String endProbationDate;

    public ShelterType getShelterType() {
        return shelterType;
    }

    public void setShelterType(ShelterType shelterType) {
        this.shelterType = shelterType;
    }

    public Integer getAdopterId() {
        return adopterId;
    }

    public void setAdopterId(Integer adopterId) {
        this.adopterId = adopterId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Integer animalId) {
        this.animalId = animalId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public String getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(String adoptionDate) {
        this.adoptionDate = adoptionDate;
    }

    public AdopterStatus getAdopterStatus() {
        return adopterStatus;
    }

    public void setAdopterStatus(AdopterStatus adopterStatus) {
        this.adopterStatus = adopterStatus;
    }

    public String getEndProbationDate() {
        return endProbationDate;
    }

    public void setEndProbationDate(String endProbationDate) {
        this.endProbationDate = endProbationDate;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return "AnimalAdopterDTO{" +
                "shelterType=" + shelterType +
                ", adopterId=" + adopterId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", animalId=" + animalId +
                ", animalName='" + animalName + '\'' +
                ", adoptionDate=" + adoptionDate +
                ", adopterStatus=" + adopterStatus +
                ", endProbationDate=" + endProbationDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnimalAdopterDTO)) return false;
        AnimalAdopterDTO that = (AnimalAdopterDTO) o;
        return shelterType == that.shelterType && Objects.equals(adopterId, that.adopterId) && Objects.equals(userId, that.userId) && Objects.equals(chatId, that.chatId) && Objects.equals(userName, that.userName) && Objects.equals(animalId, that.animalId) && Objects.equals(animalName, that.animalName) && Objects.equals(adoptionDate, that.adoptionDate) && adopterStatus == that.adopterStatus && Objects.equals(endProbationDate, that.endProbationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shelterType, adopterId, userId, chatId, userName, animalId, animalName, adoptionDate, adopterStatus, endProbationDate);
    }
}
