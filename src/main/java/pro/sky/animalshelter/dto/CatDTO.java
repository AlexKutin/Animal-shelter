package pro.sky.animalshelter.dto;

public class CatDTO {
    private Integer catId;
    private String catName;
    private Integer age;
    private String gender;
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
