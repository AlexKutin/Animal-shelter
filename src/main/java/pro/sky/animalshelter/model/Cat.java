package pro.sky.animalshelter.model;

import pro.sky.animalshelter.dto.CatDTO;

import javax.persistence.*;

@Entity
@Table(name = "cats_list")
public class Cat extends Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_id")
    private Integer catId;
    @Column(name = "cat_name")
    private String catName;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter shelterId;
    @Column(name = "age")
    private Integer age;
    @Column(name = "gender")
    private String gender;
    @Column(name = "breed")
    private String breed;

    public Cat(Integer catId, String catName, Shelter shelterId, Integer age, String gender, String breed) {
        this.catId = catId;
        this.catName = catName;
        this.shelterId = shelterId;
        this.age = age;
        this.gender = gender;
        this.breed = breed;
    }

    public Cat() {
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    @Override
    public String getName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
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

    public static Cat fromDTO(CatDTO catDTO) {
        return new Cat(catDTO.getCatId(),
                catDTO.getCatName(),
                null,
                catDTO.getAge(),
                catDTO.getGender(),
                catDTO.getBreed());
    }

    public CatDTO toDTO() {
        return new CatDTO(this.catId, this.catName,this.age, this.gender, this.breed);
    }
}
