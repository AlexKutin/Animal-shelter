package pro.sky.animalshelter.timer;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.animalshelter.model.NotificationTask;
import pro.sky.animalshelter.repository.NotificationTaskRepository;

import java.util.List;

@Component
public class NotificationTaskTimer {
    private final Logger logger = LoggerFactory.getLogger(NotificationTaskTimer.class);

    private final NotificationTaskRepository notificationTaskRepository;
    private final TelegramBot telegramBot;

    public NotificationTaskTimer(NotificationTaskRepository notificationTaskRepository, TelegramBot telegramBot) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramBot = telegramBot;
    }

    @Scheduled(cron = "${interval-in-cron}")
    public void task() {
        List<NotificationTask> tasks = notificationTaskRepository.findAllByIsProcessedIsFalse();
//                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        tasks.forEach(notificationTask -> {
            SendMessage sendMessage = new SendMessage(notificationTask.getChatId(), notificationTask.getMessage());
            SendResponse sendResponse = telegramBot.execute(sendMessage);
            if (sendResponse.isOk()) {
                notificationTask.setProcessed(Boolean.TRUE);
                notificationTaskRepository.save(notificationTask);
                logger.info("Сообщение пользователю с chatId: {} отправлено успешно ({})",
                        notificationTask.getChatId(), notificationTask.getMessage());
            }
        });
    }

}
