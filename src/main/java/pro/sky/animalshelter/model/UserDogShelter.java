package pro.sky.animalshelter.model;

import javax.persistence.*;

@Entity
@Table(name = "dog_shelter_users")
public class UserDogShelter extends UserShelter {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter dogShelter;

    @Override
    public String toString() {
        return "UserCatShelter{" +
                "userId=" + userId +
                ", telegramId=" + telegramId +
                ", userContacts='" + userContacts + '\'' +
                ", dogShelter=" + dogShelter.getShelterName() +
                '}';
    }
}
