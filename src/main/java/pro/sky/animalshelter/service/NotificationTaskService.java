package pro.sky.animalshelter.service;

import org.springframework.stereotype.Service;
import pro.sky.animalshelter.model.Adopter;
import pro.sky.animalshelter.model.NotificationTask;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class NotificationTaskService {
    public static final String PROBATION_ACTIVE_MESSAGE = "Дорогой усыновитель, Bы только что взяли к себе животное %s, " +
            "просим отнестись ответственно к Вашему новому другу. " +
            "Мы будем наблюдать за ним, поэтому просим Вас каждый день присылать отчеты о его состоянии. " +
            "Для Вас установлен испытательный срок до %s включительно.";
    public static final String PROBATION_SUCCESS_MESSAGE =
            "Дорогой усыновитель, Вы успешно выдержали испытательный срок и мы Вас поздравляем!";
    public static final String PROBATION_ADD_14_DAYS_MESSAGE =
            "Дорогой усыновитель, мы заметили, что Вы недостаточно хорошо относитесь к своим обязанностям. " +
                    "Мы вынуждены увеличить для Вас испытательный срок на 14 дней, до %s включительно.";
    public static final String PROBATION_ADD_30_DAYS_MESSAGE =
            "Дорогой усыновитель, мы заметили, что Вы недостаточно хорошо относитесь к своим обязанностям. " +
                    "Мы вынуждены увеличить для Вас испытательный срок на 30 дней, до %s включительно.";
    public static final String PROBATION_REJECT_MESSAGE =
            "Дорогой усыновитель, мы сожалеем, но Вы не выдержали испытательный срок. Мы вынуждены забрать у Вас животное. " +
                    "Мы просим Вас внимательнее ознакомиться с нашими рекомендациями и попробовать снова через месяц";
    public static final String REPORT_WARNING_MESSAGE =
            "Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. " +
                    "Пожалуйста, подойди ответственнее к этому занятию. " +
                    "В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного";
    public static final String REPORT_SKIPPED_WARNING_MESSAGE = "Дорогой усыновитель, мы заметили, что ты забыл предоставить отчет о животном за прошлый день!. " +
            "Пожалуйста, подойди ответственнее к этому занятию и направь нам отчет о животном в кратчайшее время. " +
            "В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного";
    public static final String REPORT_SKIPPED_VOLUNTEER_MESSAGE = "Уважаемый волонтер, мы заметили, что усыновитель: " +
            "%s, взявший животное: %s из приюта не предоставил отчеты уже %d (или более) дней. " +
            "Необходимо срочно связаться с данным усыновителем и выяснить, что происходит";

    public static final DateTimeFormatter notificationFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final NotificationTaskRepository notificationTaskRepository;

    public NotificationTaskService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    public void saveNotificationTask(Long chatId, String notificationText, ShelterType shelterType, LocalDateTime notificationDateTime) {
        NotificationTask notificationTask = NotificationTask.createNewTask(chatId, notificationText, shelterType, notificationDateTime);
        notificationTaskRepository.save(notificationTask);
    }

    public void saveNotificationTaskNow(Long chatId, String notificationText, ShelterType shelterType) {
        saveNotificationTask(chatId, notificationText, shelterType, LocalDateTime.now());
    }

    public void probationActiveNotification(Adopter adopter, ShelterType shelterType) {
        saveNotificationTaskNow(adopter.getChatId(),
                String.format(PROBATION_ACTIVE_MESSAGE, adopter.getAnimal().getName(), adopter.getEndProbationDate().format(notificationFormatter)), shelterType);
    }

    public void probationSuccessNotification(Adopter adopter, ShelterType shelterType) {
        saveNotificationTaskNow(adopter.getChatId(), PROBATION_SUCCESS_MESSAGE, shelterType);
    }

    public void probationAdd14DaysNotification(Adopter adopter, ShelterType shelterType) {
        saveNotificationTaskNow(adopter.getChatId(),
                String.format(PROBATION_ADD_14_DAYS_MESSAGE, adopter.getEndProbationDate().format(notificationFormatter)), shelterType);
    }

    public void probationAdd30DaysNotification(Adopter adopter, ShelterType shelterType) {
        saveNotificationTaskNow(adopter.getChatId(),
                String.format(PROBATION_ADD_30_DAYS_MESSAGE, adopter.getEndProbationDate().format(notificationFormatter)), shelterType);
    }

    public void probationRejectNotification(Adopter adopter, ShelterType shelterType) {
        saveNotificationTaskNow(adopter.getChatId(), PROBATION_REJECT_MESSAGE, shelterType);
    }

    public void reportWarningNotification(Adopter adopter, ShelterType shelterType) {
        saveNotificationTaskNow(adopter.getChatId(), REPORT_WARNING_MESSAGE, shelterType);
    }

    public void reportSkippedWarningAdopterNotification(Adopter adopter, ShelterType shelterType) {
        saveNotificationTaskNow(adopter.getChatId(), REPORT_SKIPPED_WARNING_MESSAGE, shelterType);
    }

    public void reportSkippedWarningVolunteerNotification(Long chatId, Adopter adopter, int skippedDays, ShelterType shelterType) {
        saveNotificationTaskNow(
                chatId,
                String.format(REPORT_SKIPPED_VOLUNTEER_MESSAGE, adopter.getNotNullUserName(), adopter.getAnimal().getName(), skippedDays),
                shelterType);
    }
}
