package pro.sky.animalshelter.model;

import javax.persistence.*;

@Entity
@Table(name = "volunteers")
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "volunteer_id")
    protected Integer id;

    @Column(name = "volunteer_name", nullable = false)
    protected String name;

    @Column(name = "volunteer_telegram")
    protected String telegramAddress;

    @Column(name = "volunteer_phone")
    protected String phone;

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

    @Override
    public String toString() {
        return "Volunteer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", telegramAddress='" + telegramAddress + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
