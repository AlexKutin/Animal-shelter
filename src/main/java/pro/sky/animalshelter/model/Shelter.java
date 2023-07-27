package pro.sky.animalshelter.model;

import javax.persistence.*;

@MappedSuperclass
public abstract class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shelter_id")
    protected Integer id;

//    @Column(name = "shelter_type", nullable = false)
//    @Enumerated(EnumType.STRING)
//    protected ShelterType shelterType;

    @Column(name = "shelter_name", nullable = false, unique = true, length = 100)
    protected String shelterName;

    @Column(name = "shelter_description", nullable = false, length = 1024)
    protected String shelterDescription;

    @Column(name = "shelter_address", nullable = false)
    protected String shelterAddress;

    @Column(name = "shelter_contacts", nullable = false, length = 100)
    protected String shelterContacts;

    @Column(name = "security_contacts", length = 50)
    protected String securityContacts;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id", referencedColumnName = "volunteer_id", nullable = false)
    protected Volunteer volunteer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Shelter{" +
                "id=" + id +
                ", shelterName='" + shelterName + '\'' +
                ", shelterDescription='" + shelterDescription + '\'' +
                ", shelterAddress='" + shelterAddress + '\'' +
                ", shelterContacts='" + shelterContacts + '\'' +
                ", securityContacts='" + securityContacts + '\'' +
                ", volunteer=" + volunteer +
                '}';
    }
}
