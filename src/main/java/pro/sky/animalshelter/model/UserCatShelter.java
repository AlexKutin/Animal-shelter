package pro.sky.animalshelter.model;

import pro.sky.animalshelter.dto.UserShelterDTO;

import javax.persistence.*;

@Entity
@Table(name = "cat_shelter_users")
public class UserCatShelter extends UserShelter {
    public UserCatShelter() {
    }

    @Override
    public UserShelterDTO toDTO() {
        UserShelterDTO userShelterDTO = super.toDTO();
        userShelterDTO.setShelterType(ShelterType.CAT_SHELTER);
        return userShelterDTO;
    }

    public static UserCatShelter fromDTO(UserShelterDTO userShelterDTO) {
        UserCatShelter userCatShelter = new UserCatShelter();
        userCatShelter.fillUserInfoFromDTO(userShelterDTO);
        return userCatShelter;
    }
}
