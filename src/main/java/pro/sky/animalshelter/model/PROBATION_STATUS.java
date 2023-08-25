package pro.sky.animalshelter.model;

/**
 * Статусы для испытательного срока для усыновителя
 */
public enum PROBATION_STATUS {
    PROBATION_ACTIVE("Испытательный срок активен"),

    PROBATION_SUCCESS("Испытательный срок пройден"),

    PROBATION_ADD_14("Испытательный срок продлен на 14 дней"),

    PROBATION_ADD_30("Испытательный срок продлен на 30 дней"),

    PROBATION_REJECT("Испытательный срок не пройден");

    private final String text;

    PROBATION_STATUS(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
