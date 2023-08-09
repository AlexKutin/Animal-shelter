package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.ShelterType;

@Schema(description = "Приют")
public class ShelterDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Идентификатор приюта")
    private Integer id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Тип приюта")
    private ShelterType shelterType;

    @Schema(description = "Название приюта")
    private String shelterName;
    @Schema(description = "Информация о приюте")
    private String shelterDescription;
    @Schema(description = "Адрес приюта")
    private String shelterAddress;

    @Schema(description = "Схема проезда")
    private String drivingDirection;
    @Schema(description = "Контакты приюта")
    private String shelterContacts;
    @Schema(description = "Контакты охраны приюта")
    private String securityContacts;

    @Schema(description = "Техника безопасности на территории приюта")
    private String safetyInfo;

    public ShelterDTO(Integer id, ShelterType shelterType, String shelterName, String shelterDescription, String shelterAddress, String drivingDirection, String shelterContacts, String securityContacts) {
        this.id = id;
        this.shelterType = shelterType;
        this.shelterName = shelterName;
        this.shelterDescription = shelterDescription;
        this.shelterAddress = shelterAddress;
        this.drivingDirection = drivingDirection;
        this.shelterContacts = shelterContacts;
        this.securityContacts = securityContacts;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ShelterType getShelterType() {
        return shelterType;
    }

    public void setShelterType(ShelterType shelterType) {
        this.shelterType = shelterType;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public String getShelterDescription() {
        return shelterDescription;
    }

    public void setShelterDescription(String shelterDescription) {
        this.shelterDescription = shelterDescription;
    }

    public String getShelterAddress() {
        return shelterAddress;
    }

    public void setShelterAddress(String shelterAddress) {
        this.shelterAddress = shelterAddress;
    }

    public String getShelterContacts() {
        return shelterContacts;
    }

    public void setShelterContacts(String shelterContacts) {
        this.shelterContacts = shelterContacts;
    }

    public String getSecurityContacts() {
        return securityContacts;
    }

    public void setSecurityContacts(String securityContacts) {
        this.securityContacts = securityContacts;
    }

    public String getSafetyInfo() {
        return safetyInfo;
    }

    public void setSafetyInfo(String safetyInfo) {
        this.safetyInfo = safetyInfo;
    }

    @Override
    public String toString() {
        return "ShelterDTO{" +
                "id=" + id +
                ", shelterType=" + shelterType +
                ", shelterName='" + shelterName + '\'' +
                ", shelterDescription='" + shelterDescription + '\'' +
                ", shelterAddress='" + shelterAddress + '\'' +
                ", shelterContacts='" + shelterContacts + '\'' +
                ", securityContacts='" + securityContacts + '\'' +
                ", safetyInfo='" + safetyInfo + '\'' +
                '}';
    }

    public String getDrivingDirection() {
        return drivingDirection;
    }

    public void setDrivingDirection(String drivingDirection) {
        this.drivingDirection = drivingDirection;
    }

    /**
     * Создает объект класса ShelterDTO из объекта класса Shelter
     * @param shelter исходный объект класса Shelter
     * @return ShelterDTO, заполненный из значений параметра shelter
     */
    public static ShelterDTO fromShelter(Shelter shelter) {
        return new ShelterDTO(shelter.getId(),
                shelter.getShelterType(),
                shelter.getShelterName(),
                shelter.getShelterDescription(),
                shelter.getShelterAddress(),
                shelter.getDrivingDirection(),
                shelter.getShelterContacts(),
                shelter.getSecurityContacts());
        ShelterDTO shelterDTO = new ShelterDTO();
        shelterDTO.setId(shelter.getId());
        shelterDTO.setShelterType(shelter.getShelterType());
        shelterDTO.setShelterName(shelter.getShelterName());
        shelterDTO.setShelterDescription(shelter.getShelterDescription());
        shelterDTO.setShelterAddress(shelter.getShelterAddress());
        shelterDTO.setShelterContacts(shelter.getShelterContacts());
        shelterDTO.setSecurityContacts(shelter.getSecurityContacts());
        shelterDTO.setSafetyInfo(shelter.getSafetyInfo());

        return shelterDTO;
    }

    public Shelter toShelter() {
        Shelter shelter = new Shelter();
        shelter.setId(this.getId());
        shelter.setShelterType(this.getShelterType());
        shelter.setShelterName(this.getShelterName());
        shelter.setShelterDescription(this.getShelterDescription());
        shelter.setShelterAddress(this.getShelterAddress());
        shelter.setShelterContacts(this.getShelterContacts());
        shelter.setSecurityContacts(this.getSecurityContacts());
        shelter.setSafetyInfo(this.getSafetyInfo());

        return shelter;
    }
}
