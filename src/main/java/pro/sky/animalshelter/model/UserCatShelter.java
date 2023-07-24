package pro.sky.animalshelter.model;

import javax.persistence.*;

@Entity
@Table(name = "cat_shelter_users")
public class UserCatShelter extends UserShelter {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private CatShelter catShelter;

    @Override
    public String toString() {
        return "UserCatShelter{" +
                "userId=" + userId +
                ", telegramId=" + telegramId +
                ", userContacts='" + userContacts + '\'' +
                ", catShelter=" + catShelter.getShelterName() +
                '}';
    }
}
