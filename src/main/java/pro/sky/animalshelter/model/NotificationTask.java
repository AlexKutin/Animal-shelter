package pro.sky.animalshelter.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_tasks")
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "notification_date_time", nullable = false)
    private LocalDateTime notificationDateTime;

    @Column(name = "processed", nullable = false, columnDefinition = "boolean default false")
    private Boolean isProcessed;

    @Column(name = "shelter_type")
    @Enumerated(EnumType.STRING)
    private ShelterType shelterType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getNotificationDateTime() {
        return notificationDateTime;
    }

    public void setNotificationDateTime(LocalDateTime dateTime) {
        this.notificationDateTime = dateTime;
    }

    public Boolean getProcessed() {
        return isProcessed;
    }

    public void setProcessed(Boolean processed) {
        isProcessed = processed;
    }

    public ShelterType getShelterType() {
        return shelterType;
    }

    public void setShelterType(ShelterType shelterType) {
        this.shelterType = shelterType;
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", message='" + message + '\'' +
                ", notificationDateTime=" + notificationDateTime +
                ", isProcessed=" + isProcessed +
                ", shelterType=" + shelterType +
                '}';
    }

    public static NotificationTask createNewTask(Long chatId, String notificationText, ShelterType shelterType, LocalDateTime notificationDateTime) {
        NotificationTask notificationTask = new NotificationTask();
        notificationTask.setChatId(chatId);
        notificationTask.setMessage(notificationText);
        notificationTask.setNotificationDateTime(notificationDateTime);
        notificationTask.setProcessed(Boolean.FALSE);
        notificationTask.setShelterType(shelterType);
        return notificationTask;
    }
}
