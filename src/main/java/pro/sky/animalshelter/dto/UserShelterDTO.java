package pro.sky.animalshelter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.model.UserShelter;

@Schema(description = "Зрегистрированный пользователь приюта")
public class UserShelterDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Id пользователя")
    private Integer userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Telegram Id пользователя")
    private Long telegramId;

    @Schema(description = "Telegram Username пользователя")
    private String userName;

    @Schema(description = "Тип приюта")
    private ShelterType shelterType;

    @Schema(description = "Имя пользователя")
    private String firstName;

    @Schema(description = "Фамилия пользователя")
    private String lastName;

    @Schema(description = "Контакты пользователя")
    private String userContacts;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ShelterType getShelterType() {
        return shelterType;
    }

    public void setShelterType(ShelterType shelterType) {
        this.shelterType = shelterType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserContacts() {
        return userContacts;
    }

    public void setUserContacts(String userContacts) {
        this.userContacts = userContacts;
    }

    @Override
    public String toString() {
        return "UserShelterDTO{" +
                "userId=" + userId +
                ", telegramId=" + telegramId +
                ", userName='" + userName + '\'' +
                ", shelterType=" + shelterType +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userContacts='" + userContacts + '\'' +
                '}';
    }

    public static UserShelterDTO fromUserShelter(UserShelter userShelter) {
        UserShelterDTO userShelterDTO = new UserShelterDTO();
        userShelterDTO.setUserId(userShelter.getUserId());
        userShelterDTO.setTelegramId(userShelter.getTelegramId());
        userShelterDTO.setUserName(userShelter.getUserName());
        userShelterDTO.setShelterType(userShelter.getShelter().getShelterType());
        userShelterDTO.setFirstName(userShelter.getFirstName());
        userShelterDTO.setLastName(userShelter.getLastName());
        userShelterDTO.setUserContacts(userShelter.getUserContacts());

        return userShelterDTO;
    }
}
