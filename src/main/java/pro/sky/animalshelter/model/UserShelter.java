package pro.sky.animalshelter.model;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
public abstract class UserShelter implements Comparable<UserShelter> {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer userId;

    @Column(name = "telegram_id", nullable = false, unique = true)
    protected Long telegramId;

    @Column(name = "user_name", length = 50)
    private String userName;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "user_contacts")
    protected String userContacts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    protected Shelter shelter;

    public UserShelter() {
    }

    public UserShelter(Long telegramId, String userName, String firstName, String lastName, String userContacts, Shelter shelter) {
        this.telegramId = telegramId;
        fillUserInfo(userName, firstName, lastName, userContacts, shelter);
    }

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

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    @Override
    public String toString() {
        return "UserShelter{" +
                "userId=" + userId +
                ", telegramId=" + telegramId +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userContacts='" + userContacts + '\'' +
                ", shelter=" + shelter.getShelterName() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserShelter that = (UserShelter) o;
        return Objects.equals(userId, that.userId) && Objects.equals(telegramId, that.telegramId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, telegramId);
    }

    public void fillUserInfo(String userName, String firstName, String lastName, String userContacts, Shelter shelter) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userContacts = userContacts;
        this.shelter = shelter;
    }

    @Override
    public int compareTo(@NotNull UserShelter o) {
        return Integer.compare(this.getUserId(), o.getUserId());
    }
}
