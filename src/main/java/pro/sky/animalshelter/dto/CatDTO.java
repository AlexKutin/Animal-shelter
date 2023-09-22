package pro.sky.animalshelter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Кошка")
public class CatDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Идентификатор кошки")
    private Integer catId;
    @Schema(description = "Имя кошки")
    private String catName;
    @Schema(description = "Возраст")
    private Integer age;
    @Schema(description = "Пол")
    private String gender;
    @Schema(description = "Порода")
    private String breed;

    public CatDTO(Integer catId, String catName, Integer age, String gender, String breed) {
        this.catId = catId;
        this.catName = catName;
        this.age = age;
        this.gender = gender;
        this.breed = breed;
    }

    public CatDTO() {
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
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
