package pro.sky.animalshelter.timer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.service.AdopterService;

@Component
public class checkSkippedReports {
    public static final int SKIPPED_DAYS_WARNING = 1;
    public static final int SKIPPED_DAYS_SIGNAL_VOLUNTEER = 3;

    private final AdopterService adopterService;

    public checkSkippedReports(AdopterService adopterService) {
        this.adopterService = adopterService;
    }

    @Scheduled(cron = "${interval-in-cron_check_reports}")
    public void checkSkipReportsTask() {
        // Проверяем, когда от усыновителя нет отчетов на прощежший день и направлем ему оповещение
        adopterService.checkSkipReportAndCreateWarning(ShelterType.CAT_SHELTER, SKIPPED_DAYS_WARNING, TypeWarning.WARNING_ADOPTER);
        adopterService.checkSkipReportAndCreateWarning(ShelterType.DOG_SHELTER, SKIPPED_DAYS_WARNING, TypeWarning.WARNING_ADOPTER);

        // Проверяем, когда от усыновителя нет отчета более, чем 2 дня и отправляем сообщение волонтерами приюта
        adopterService.checkSkipReportAndCreateWarning(ShelterType.CAT_SHELTER, SKIPPED_DAYS_SIGNAL_VOLUNTEER, TypeWarning.WARNING_VOLUNTEER);
        adopterService.checkSkipReportAndCreateWarning(ShelterType.DOG_SHELTER, SKIPPED_DAYS_SIGNAL_VOLUNTEER, TypeWarning.WARNING_VOLUNTEER);
    }
}
