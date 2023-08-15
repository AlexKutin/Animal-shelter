package pro.sky.animalshelter.model;

import pro.sky.animalshelter.dto.DogDTO;

import javax.persistence.*;

@Entity
@Table(name = "dogs_list")
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dog_id")
    private Integer dogId;
    @Column(name = "dogName")
    private String dogName;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter shelterId;
    @Column(name = "age")
    private Integer age;
    @Column(name = "gender")
    private String gender;
    @Column(name = "breed")
    private String breed;

    public Dog(Integer dogId, String dogName, Shelter shelterId, Integer age, String gender, String breed) {
        this.dogId = dogId;
        this.dogName = dogName;
        this.shelterId = shelterId;
        this.age = age;
        this.gender = gender;
        this.breed = breed;
    }

    public Dog() {
    }

    public Integer getDogId() {
        return dogId;
    }

    public void setDogId(Integer dogId) {
        this.dogId = dogId;
    }

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public Shelter getShelterId() {
        return shelterId;
    }

    public void setShelterId(Shelter shelterId) {
        this.shelterId = shelterId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public static Dog fromDTO(DogDTO dogDTO) {
        return new Dog(dogDTO.getDogId(),
                dogDTO.getDogName(),
                null,
                dogDTO.getAge(),
                dogDTO.getGender(),
                dogDTO.getBreed());
    }

    public DogDTO toDTO() {
        return new DogDTO(this.dogId, this.dogName,this.age, this.gender, this.breed);
    }
}
