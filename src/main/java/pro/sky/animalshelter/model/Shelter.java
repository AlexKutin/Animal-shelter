package pro.sky.animalshelter.model;

import javax.persistence.*;

@Entity
@Table(name = "shelters")
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shelter_id")
    private Integer id;

    @Column(name = "shelter_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShelterType shelterType;

    @Column(name = "shelter_name", nullable = false, unique = true, length = 100)
    private String shelterName;

    @Column(name = "shelter_description", nullable = false, length = 1024)
    private String shelterDescription;

    @Column(name = "shelter_address", nullable = false)
    private String shelterAddress;

    // ToDo добавить схему проезда

    @Column(name = "shelter_contacts", nullable = false, length = 100)
    private String shelterContacts;

    @Column(name = "security_contacts", length = 50)
    private String securityContacts;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "volunteer_id", referencedColumnName = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rules", referencedColumnName = "id_rules", nullable = false)
    private Rules rules;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ShelterType getShelterType() {
        return shelterType;
    }

    public void setShelterType(ShelterType shelterType) {
        this.shelterType = shelterType;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public String getShelterDescription() {
        return shelterDescription;
    }

    public void setShelterDescription(String shelterDescription) {
        this.shelterDescription = shelterDescription;
    }

    public String getShelterAddress() {
        return shelterAddress;
    }

    public void setShelterAddress(String shelterAddress) {
        this.shelterAddress = shelterAddress;
    }

    public String getShelterContacts() {
        return shelterContacts;
    }

    public void setShelterContacts(String shelterContacts) {
        this.shelterContacts = shelterContacts;
    }

    public String getSecurityContacts() {
        return securityContacts;
    }

    public void setSecurityContacts(String securityContacts) {
        this.securityContacts = securityContacts;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    @Override
    public String toString() {
        return "Shelter{" +
                "id=" + id +
                ", shelterType=" + shelterType +
                ", shelterName='" + shelterName + '\'' +
                ", shelterDescription='" + shelterDescription + '\'' +
                ", shelterAddress='" + shelterAddress + '\'' +
                ", shelterContacts='" + shelterContacts + '\'' +
                ", securityContacts='" + securityContacts + '\'' +
                ", volunteer=" + volunteer +
                '}';
    }
}
