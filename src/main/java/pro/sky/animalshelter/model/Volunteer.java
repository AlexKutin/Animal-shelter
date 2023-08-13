package pro.sky.animalshelter.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "volunteers")
public class Volunteer implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "volunteer_id")
    private Integer id;

    @Column(name = "volunteer_name", nullable = false)
    private String name;

    @Column(name = "volunteer_telegram")
    private String telegramAddress;

    @Column(name = "volunteer_phone")
    private String phone;

    @Column(name = "volunteer_active")
    private boolean isActive;
    //Для получения идентификатора чата, волонтер должен отправить команду боту /getchatid,
    //после это значение необходимо добавить в таблицу в соответствующюю таблицу.
    // Отправка сообщений волонтерам о помощи реализуется на сновании идентификатора чата волонтера с ботом
    @Column(name = "chat_id")
    private Long chatId;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", telegramAddress='" + telegramAddress + '\'' +
                ", phone='" + phone + '\'' +
                ", isActive=" + isActive +
                ", shelter=" + shelter.getShelterName() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volunteer volunteer = (Volunteer) o;
        return isActive == volunteer.isActive && Objects.equals(id, volunteer.id) && Objects.equals(name, volunteer.name) && Objects.equals(telegramAddress, volunteer.telegramAddress) && Objects.equals(phone, volunteer.phone) && Objects.equals(shelter, volunteer.shelter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, telegramAddress, phone, isActive, shelter);
    }
}
