package pro.sky.animalshelter.model;

public enum ShelterType {
    CAT_SHELTER("Nice cat"),
    DOG_SHELTER("Happy dog");

    public final String shelterName;

    ShelterType(String shelterName) {
        this.shelterName = shelterName;
    }
}
