package pro.sky.animalshelter.model;

public enum ShelterType {
    CAT_SHELTER("Nice cat"),
    DOG_SHELTER("Happy dog"),
    NOT_SUPPORTED("Not supported"); // for tests only

    public final String shelterName;

    ShelterType(String shelterName) {
        this.shelterName = shelterName;
    }
}
