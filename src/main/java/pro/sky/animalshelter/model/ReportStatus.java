package pro.sky.animalshelter.model;

import static pro.sky.animalshelter.Constants.TextConstants.REPORT_ACCEPTED_MESSAGE;
import static pro.sky.animalshelter.Constants.TextConstants.REPORT_WARNING_MESSAGE;

/**
 * Статусы для отчетов усыновителя
 */
public enum ReportStatus {
    REPORT_NEW("Новый отчет"),            // Новый отчет (подлежит рассмотрению)
    REPORT_ACCEPTED(REPORT_ACCEPTED_MESSAGE),  // Отчет принят
    REPORT_WARNING(REPORT_WARNING_MESSAGE);    // Отчет заполнен плохо (предупреждение пользователя)

    private final String text;

    ReportStatus(String text) {
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
