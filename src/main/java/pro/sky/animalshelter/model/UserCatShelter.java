package pro.sky.animalshelter.model;

import javax.persistence.*;

@Entity
@Table(name = "cat_shelter_users")
public class UserCatShelter extends UserShelter {
    public UserCatShelter() {
    }

    public UserCatShelter(Long telegramId, String userName, String firstName, String lastName, String userContacts, Shelter shelter) {
        super(telegramId, userName, firstName, lastName, userContacts, shelter);
    }
}
