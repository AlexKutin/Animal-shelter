package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.model.Volunteer;

@Schema(description = "Волонтер")
public class VolunteerDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Schema(description = "Имя волонтера")
    private String name;

    @Schema(description = "Telegram-адрес волонтера")
    private String telegramAddress;

    @Schema(description = "Телефон волонтера")
    private String phone;

    @Schema(description = "Признак активности (не заблокирован)")
    private Boolean isActive;

    @Schema(description = "Тип приюта")
    private ShelterType shelterType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelegramAddress() {
        return telegramAddress;
    }

    public void setTelegramAddress(String telegramAddress) {
        this.telegramAddress = telegramAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public ShelterType getShelterType() {
        return shelterType;
    }

    public void setShelterType(ShelterType shelterType) {
        this.shelterType = shelterType;
    }

    @Override
    public String toString() {
        return "VolunteerDTO{" +
                "id=" + id +
                ", shelterType=" + shelterType +
                ", name='" + name + '\'' +
                ", telegramAddress='" + telegramAddress + '\'' +
                ", phone='" + phone + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    public static VolunteerDTO fromVolunteer(Volunteer volunteer) {
        VolunteerDTO volunteerDTO = new VolunteerDTO();
        volunteerDTO.setId(volunteer.getId());
        volunteerDTO.setName(volunteer.getName());
        volunteerDTO.setTelegramAddress(volunteer.getTelegramAddress());
        volunteerDTO.setPhone(volunteer.getPhone());
        volunteerDTO.setActive(volunteer.isActive());
        volunteerDTO.setShelterType(volunteer.getShelter().getShelterType());

        return volunteerDTO;
    }

    public Volunteer toVolunteer(Shelter shelter) {
        Volunteer volunteer = new Volunteer();
        volunteer.setId(this.getId());
        volunteer.setName(this.getName());
        volunteer.setTelegramAddress(this.getTelegramAddress());
        volunteer.setPhone(this.getPhone());
        volunteer.setActive(this.isActive());
        volunteer.setShelter(shelter);

        return volunteer;
    }
}
