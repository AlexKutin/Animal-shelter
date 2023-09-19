package pro.sky.animalshelter.model;

import pro.sky.animalshelter.dto.UserShelterDTO;

import javax.persistence.*;

@Entity
@Table(name = "dog_shelter_users")
public class UserDogShelter extends UserShelter {
    public UserDogShelter() {
    }

    @Override
    public UserShelterDTO toDTO() {
        UserShelterDTO userShelterDTO = super.toDTO();
        userShelterDTO.setShelterType(ShelterType.DOG_SHELTER);
        return userShelterDTO;
    }

    public static UserDogShelter fromDTO(UserShelterDTO userShelterDTO) {
        UserDogShelter userDogShelter = new UserDogShelter();
        userDogShelter.fillUserInfoFromDTO(userShelterDTO);
        return userDogShelter;
    }
}
