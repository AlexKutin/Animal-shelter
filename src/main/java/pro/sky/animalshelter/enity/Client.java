package pro.sky.animalshelter.enity;

import pro.sky.animalshelter.model.Shelter;

import javax.persistence.*;
import java.util.List;

@Entity
public class Client {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "client_id")
    private Long clientId;
    @ManyToOne
    @JoinColumn (name = "shelterId")
    private Shelter shelter;
    private String name;
    private String phone;
    private Long chat_id;
    @OneToMany (mappedBy = "client")
    private List<Report> reports;

    public Long getClientId() {
        return clientId;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Long getChat_id() {
        return chat_id;
    }

}