package pro.sky.animalshelter.model;

/**
 * Состояния для отчетов усыновителя
 */
public enum ReportStatus {
    REPORT_NEW,             // Новый отчет (подлежит рассмотрению)
    REPORT_ACCEPTED,        // Отчет принят
    REPORT_NEED_PHOTO,      // Отчет не содержит фото
    REPORT_NEED_TEXT,       // Отчет не содержит описание
    REPORT_WARNING_NEED,    // Отчет заполнен плохо (предупреждение пользователя)
    REPORT_WARNING_SEND     // Отчет заполнен плохо (предупреждение отправлено)
}
