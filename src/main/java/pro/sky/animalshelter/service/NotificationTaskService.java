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
    private static final String PROBATION_ACTIVE_MESSAGE = "Дорогой усыновитель, Bы только что взяли к себе животное %s, " +
            "просим отнестись ответственно к Вашему новому другу. " +
            "Мы будем наблюдать за ним, поэтому просим Вас каждый день присылать отчеты о его состоянии. " +
            "Для Вас установлен испытательный срок до %s включительно.";
    private static final String PROBATION_SUCCESS_MESSAGE =
            "Дорогой усыновитель, Вы успешно выдержали испытательный срок и мы Вас поздравляем!";
    private static final String PROBATION_ADD_14_DAYS_MESSAGE =
            "Дорогой усыновитель, мы заметили, что Вы недостаточно хорошо относитесь к своим обязанностям. " +
                    "Мы вынуждены увеличить для Вас испытательный срок на 14 дней, до %s включительно.";
    private static final String PROBATION_ADD_30_DAYS_MESSAGE =
            "Дорогой усыновитель, мы заметили, что Вы недостаточно хорошо относитесь к своим обязанностям. " +
                    "Мы вынуждены увеличить для Вас испытательный срок на 30 дней, до %s включительно.";
    private static final String PROBATION_REJECT_MESSAGE =
            "Дорогой усыновитель, мы сожалеем, но Вы не выдержали испытательный срок. Мы вынуждены забрать у Вас животное. " +
                    "Мы просим Вас внимательнее ознакомиться с нашими рекомендациями и попробовать снова через месяц";
    private static final String REPORT_WARNING_MESSAGE =
            "Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. " +
                    "Пожалуйста, подойди ответственнее к этому занятию. " +
                    "В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного";

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final NotificationTaskRepository notificationTaskRepository;

    public NotificationTaskService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    public void saveNotificationTask(Long chatId, String notificationText, ShelterType shelterType, LocalDateTime notificationDateTime) {
        NotificationTask notificationTask = new NotificationTask();
        notificationTask.setChatId(chatId);
        notificationTask.setMessage(notificationText);
        notificationTask.setNotificationDateTime(notificationDateTime);
        notificationTask.setProcessed(Boolean.FALSE);
        notificationTask.setShelterType(shelterType);

        notificationTaskRepository.save(notificationTask);
    }

    public void saveNotificationTaskNow(Long chatId, String notificationText, ShelterType shelterType) {
        saveNotificationTask(chatId, notificationText, shelterType, LocalDateTime.now());
    }

    public void probationActiveNotification(Adopter adopter, ShelterType shelterType) {
        saveNotificationTaskNow(adopter.getChatId(),
                String.format(PROBATION_ACTIVE_MESSAGE, adopter.getAnimal().getName(), adopter.getEndProbationDate().format(formatter)), shelterType);
    }

    public void probationSuccessNotification(Adopter adopter, ShelterType shelterType) {
        saveNotificationTaskNow(adopter.getChatId(), PROBATION_SUCCESS_MESSAGE, shelterType);
    }

    public void probationAdd14DaysNotification(Adopter adopter, ShelterType shelterType) {
        saveNotificationTaskNow(adopter.getChatId(),
                String.format(PROBATION_ADD_14_DAYS_MESSAGE, adopter.getEndProbationDate().format(formatter)), shelterType);
    }

    public void probationAdd30DaysNotification(Adopter adopter, ShelterType shelterType) {
        saveNotificationTaskNow(adopter.getChatId(),
                String.format(PROBATION_ADD_30_DAYS_MESSAGE, adopter.getEndProbationDate().format(formatter)), shelterType);
    }

    public void probationRejectNotification(Adopter adopter, ShelterType shelterType) {
        saveNotificationTaskNow(adopter.getChatId(), PROBATION_REJECT_MESSAGE, shelterType);
    }

    public void reportWarningNotification(Adopter adopter, ShelterType shelterType) {
        saveNotificationTaskNow(adopter.getChatId(), REPORT_WARNING_MESSAGE, shelterType);
    }

}
