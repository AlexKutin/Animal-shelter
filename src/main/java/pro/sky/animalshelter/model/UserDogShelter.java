package pro.sky.animalshelter.model;

import javax.persistence.*;

@Entity
@Table(name = "dog_shelter_users")
public class UserDogShelter extends UserShelter {
    public UserDogShelter() {
    }

    public UserDogShelter(Long telegramId, String firstName, String lastName, String userContacts, Shelter shelter) {
        super(telegramId, firstName, lastName, userContacts, shelter);
    }
}
