package pro.sky.animalshelter.model;

import javax.persistence.*;

@Entity
@Table(name = "volunteers")
public class Volunteer {
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
}
