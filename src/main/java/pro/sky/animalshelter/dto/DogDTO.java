package pro.sky.animalshelter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Собака")
public class DogDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Идентификатор собаки")
    private Integer dogId;

    @Schema(description = "Имя собаки")
    private String dogName;

    @Schema(description = "Возраст")
    private Integer age;

    @Schema(description = "Пол")
    private String gender;

    @Schema(description = "Порода")
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
