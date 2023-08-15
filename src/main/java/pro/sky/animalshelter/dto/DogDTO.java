package pro.sky.animalshelter.dto;

import pro.sky.animalshelter.model.Shelter;

import javax.persistence.*;

public class DogDTO {
    private Integer dogId;
    private String dogName;
    private Integer age;
    private String gender;
    private String breed;

    public DogDTO(Integer dogId, String dogName, Integer age, String gender, String breed) {
        this.dogId = dogId;
        this.dogName = dogName;
        this.age = age;
        this.gender = gender;
        this.breed = breed;
    }

    public DogDTO() {
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
}
