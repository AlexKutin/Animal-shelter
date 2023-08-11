package pro.sky.animalshelter.model;

import javax.persistence.*;

@MappedSuperclass
public abstract class UserShelter {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer userId;

    @Column(name = "telegram_id", nullable = false, unique = true)
    protected Long telegramId;

    @Column(name = "user_contacts")
    protected String userContacts;

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

    public String getUserContacts() {
        return userContacts;
    }

    public void setUserContacts(String userContacts) {
        this.userContacts = userContacts;
    }

    @Override
    public String toString() {
        return "UserShelter{" +
                "userId=" + userId +
                ", telegramId=" + telegramId +
                ", userContacts='" + userContacts + '\'' +
                '}';
    }
}
