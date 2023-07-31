package dto;

import io.swagger.v3.oas.annotations.media.Schema;
import pro.sky.animalshelter.model.Volunteer;

@Schema(description = "Волонтер")
public class VolunteerDTO {
    private Integer id;

    @Schema(description = "Имя волонтера")
    private String name;

    @Schema(description = "Telegram-адрес волонтера")
    private String telegramAddress;

    @Schema(description = "Телефон волонтера")
    private String phone;

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

    public static VolunteerDTO fromVolunteer(Volunteer volunteer) {
        VolunteerDTO volunteerDTO = new VolunteerDTO();
        volunteerDTO.setId(volunteer.getId());
        volunteerDTO.setName(volunteer.getName());
        volunteerDTO.setTelegramAddress(volunteer.getTelegramAddress());
        volunteerDTO.setPhone(volunteer.getPhone());

        return volunteerDTO;
    }

    public Volunteer toVolunteer() {
        Volunteer volunteer = new Volunteer();
        volunteer.setId(this.getId());
        volunteer.setName(this.getName());
        volunteer.setTelegramAddress(this.getTelegramAddress());
        volunteer.setPhone(this.getPhone());

        return volunteer;
    }
}
