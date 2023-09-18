package pro.sky.animalshelter.model;

import pro.sky.animalshelter.dto.CatDTO;

import javax.persistence.*;
import java.util.Objects;

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
    private Shelter shelter;
    @Column(name = "age")
    private Integer age;
    @Column(name = "gender")
    private String gender;
    @Column(name = "breed")
    private String breed;

    public Cat(Integer catId, String catName, Shelter shelter, Integer age, String gender, String breed) {
        this.catId = catId;
        this.catName = catName;
        this.shelter = shelter;
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

    @Override
    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelterId) {
        this.shelter = shelterId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cat)) return false;
        Cat cat = (Cat) o;
        return catId.equals(cat.catId) && catName.equals(cat.catName) && age.equals(cat.age) && Objects.equals(gender, cat.gender) && Objects.equals(breed, cat.breed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catId, catName, age, gender, breed);
    }
}
