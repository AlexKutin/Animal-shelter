package pro.sky.animalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pro.sky.animalshelter.keyBoard.InlineKeyboardMarkupHelper;
import pro.sky.animalshelter.service.ShelterService;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class AnimalShelterUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(AnimalShelterUpdatesListener.class);

    private final TelegramBot animalShelterBot;

    private final ShelterService shelterService;
    private String chosenShelter;

    public AnimalShelterUpdatesListener(TelegramBot animalShelterBot, ShelterService shelterService) {
        this.animalShelterBot = animalShelterBot;
        this.shelterService = shelterService;
    }

    @PostConstruct
    public void init() {
        animalShelterBot.setUpdatesListener(this);
    }

    private void sendMessage(Long chatId, String message, InlineKeyboardMarkup inlineKeyboardMarkupHelper) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        SendResponse sendResponse = animalShelterBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                if (update.message() != null) {
                    handleTextMessage(update.message());
                } else if (update.callbackQuery() != null) {
                    handleCallbackQuery(update.callbackQuery());
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void handleTextMessage(Message message) {
        Long chatId = message.chat().id();
        String text = message.text();

        if ("/start".equals(text)) {
            sendStartMessage(chatId);
        } else {
            sendInvalidMessage(chatId);
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.message().chat().id();
        String data = callbackQuery.data();

        if ("Приют кошек\uD83D\uDC31".equals(data) || "Приют собак\uD83D\uDC36".equals(data)) {
            handleShelterInfoMessage(chatId, data);
        } else if ("Info".equals(data)) {
            sendShelterInfoOptions(chatId);
        } else if ("ShelterInfoAbout".equals(data)) {
            sendShelterInfoAbout(chatId);
        } else if ("ShelterInfoAddress".equals(data)) {
            sendShelterInfoAddress(chatId);
        } else if ("ShelterContacts".equals(data)) {
            sendShelterContacts(chatId);
        } else if ("ShelterInfoSecurityContacts".equals(data)) {
            sendShelterSecurityContacts(chatId);
        } else if ("ShelterInfoSafetyTips".equals(data)) {
            sendShelterSafetyTips(chatId);
        } else if ("ShelterInfoLeaveContacts".equals(data)) {
            sendShelterLeaveContacts(chatId);
        } else if ("AdoptionRules".equals(data)) {
            sendAdoptionRules(chatId);
        } else if ("SendReport".equals(data)) {
            sendReportMessage(chatId);
        } else if ("CallVolunteer".equals(data)) {
            sendVolunteerMessage(chatId);
        } else if ("BackShelterMenu".equals(data)) {
            sendMainMenuMessage(chatId);
        } else if ("BackMainMenu".equals(data)) {
            sendShelterInfoOptions(chatId);
        } else if ("BackShelterInfo".equals(data)) {
            sendShelterInfoOptions(chatId);
        }
    }

    private void sendStartMessage(Long chatId) {
        SendMessage response = new SendMessage(chatId,
                "Добро пожаловать в приют для животных города Астаны! Выберите приют для каких животных вас интересует:");
        InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkupHelper.createInlineKeyboard();
        response.replyMarkup(keyboardMarkup);
        SendResponse sendResponse = animalShelterBot.execute(response);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendInvalidMessage(Long chatId) {
        InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkupHelper.createInlineKeyboard();
        sendMessage(chatId, "Некорректное сообщение.", keyboardMarkup);
    }

    private void handleShelterInfoMessage(Long chatId, String data) {
        String shelterName = null;
        if (data.contains("собак")) {
            shelterName = shelterService.getDogShelter().getShelterName();
        } else if (data.contains("кошек")) {
            shelterName = shelterService.getCatShelter().getShelterName();
        }

        if (shelterName != null) {
            chosenShelter = shelterName;
            SendMessage response = new SendMessage(chatId, data + " " + shelterName +
                    " приветствует Вас! Спасибо за выбор! Выберите, что вас интересует:\n" +
                    "1. Информация о приюте\n" +
                    "2. Как забрать животное\n" +
                    "3. Отправить отчет о животном\n" +
                    "4. Позвать волонтера\n" +
                    "⬅ Назад");
            InlineKeyboardMarkup menuKeyboard = InlineKeyboardMarkupHelper.createMainMenuInlineKeyboard();
            response.replyMarkup(menuKeyboard);
            SendResponse sendResponse = animalShelterBot.execute(response);
            logger.info("Message sent status: {}", sendResponse.isOk());
        } else {
            sendMessage(chatId, "Ошибка в выборе приюта.", null);
        }
    }

    private void sendShelterInfoOptions(Long chatId) {
        SendMessage responseInfo = new SendMessage(chatId, "Выберите что вы хотите узнать о приюте:\n" +
                "1. О приюте\n" +
                "2. Расписание и адрес\n" +
                "3. Контакты приюта\n" +
                "4. Контакты охраны для получения пропуска\n" +
                //схема проезда
                "5. Техника безопасности на территории приюта\n" +
                "6. Оставить контакты\n" +
                "7. Позвать волонтера\n" +
                "⬅ Назад");
        InlineKeyboardMarkup shelterInfoKeyboard = InlineKeyboardMarkupHelper.createShelterInfoInlineKeyboard();
        responseInfo.replyMarkup(shelterInfoKeyboard);
        SendResponse sendResponseInfo = animalShelterBot.execute(responseInfo);
        logger.info("Message sent status: {}", sendResponseInfo.isOk());
    }

    private void sendShelterInfoAbout(Long chatId) {
        String shelterName;
        String shelterDescription;
        if (chosenShelter.equals("Happy dog")) {
            shelterName = shelterService.getDogShelter().getShelterName();
            shelterDescription = shelterService.getDogShelter().getShelterDescription();
        } else {
            shelterName = shelterService.getCatShelter().getShelterName();
            shelterDescription = shelterService.getCatShelter().getShelterDescription();
        }
        SendMessage responseShelterInfoAbout = new SendMessage(chatId, "Приют называется " + shelterName + ". " + shelterDescription);
        InlineKeyboardMarkup BackShelterInfoKeyboard = InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard();
        responseShelterInfoAbout.replyMarkup(BackShelterInfoKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(responseShelterInfoAbout);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendShelterInfoAddress(Long chatId) {
        String shelterAddress;
        if (chosenShelter.equals("Happy dog")) {
            shelterAddress = shelterService.getDogShelter().getShelterAddress();
        } else {
            shelterAddress = shelterService.getCatShelter().getShelterAddress();
        }
        SendMessage responseShelterInfoAddress = new SendMessage(chatId, shelterAddress);
        // Add logic for sending the location map if needed
        InlineKeyboardMarkup BackShelterInfoKeyboard = InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard();
        responseShelterInfoAddress.replyMarkup(BackShelterInfoKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(responseShelterInfoAddress);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendShelterContacts(Long chatId) {
        String shelterContacts;
        if (chosenShelter.equals("Happy dog")) {
            shelterContacts = shelterService.getDogShelter().getShelterContacts();
        } else {
            shelterContacts = shelterService.getCatShelter().getShelterContacts();
        }
        SendMessage responseShelterInfoContacts = new SendMessage(chatId, "Контакты для связи с нами:\nТел:" + shelterContacts);
        InlineKeyboardMarkup BackShelterInfoKeyboard = InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard();
        responseShelterInfoContacts.replyMarkup(BackShelterInfoKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(responseShelterInfoContacts);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendShelterSecurityContacts(Long chatId) {
        String shelterInfoSecurityContacts;
        if (chosenShelter.equals("Happy dog")) {
            shelterInfoSecurityContacts = shelterService.getDogShelter().getSecurityContacts();
        } else {
            shelterInfoSecurityContacts = shelterService.getCatShelter().getSecurityContacts();
        }
        SendMessage responseShelterInfoContacts = new SendMessage(chatId, "Контакты охраны для получения пропуска:\nТел:" + shelterInfoSecurityContacts);
        InlineKeyboardMarkup BackShelterInfoKeyboard = InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard();
        responseShelterInfoContacts.replyMarkup(BackShelterInfoKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(responseShelterInfoContacts);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendShelterSafetyTips(Long chatId) {
        String shelterInfoSafetyTips;
        if (chosenShelter.equals("Happy dog")) {
            shelterInfoSafetyTips = shelterService.getDogShelter().getSafetyInfo();
        } else {
            shelterInfoSafetyTips = shelterService.getCatShelter().getSafetyInfo();
        }
        SendMessage responseShelterInfoContacts = new SendMessage(chatId, shelterInfoSafetyTips);
        InlineKeyboardMarkup BackShelterInfoKeyboard = InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard();
        responseShelterInfoContacts.replyMarkup(BackShelterInfoKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(responseShelterInfoContacts);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendShelterLeaveContacts(Long chatId) {
        SendMessage responseShelterInfoLeaveContacts = new SendMessage(chatId, "Оставьте нам свои контакты и мы с вами свяжемся");
        InlineKeyboardMarkup BackShelterInfoKeyboard = InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard();
        responseShelterInfoLeaveContacts.replyMarkup(BackShelterInfoKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(responseShelterInfoLeaveContacts);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendAdoptionRules(Long chatId) {
        SendMessage responseAdoptionRules = new SendMessage(chatId, "Мы рады, что вы заинтересованы помочь нашим пушистым друзьям. Но перед этим ознакомьтесь с правилами:\n" +
                "1. Правила знакомства с животными \n" +
                "2. Список необходимых документов для усыновления\n" +
                "3. Транспортировка животного\n" +
                "4. Обустройство дома для самых маленьких\n" +
                "5. Обустройство дома для взрослого животного\n" +
                "6. Обустройство дома для животных с ограниченными возможностями\n" +
                "7. Причины отказа\n" +
                "8. Оставьте нам свои контакты для связи\n" +
                "9. Позвать волонтера\n" +
                "⬅ Назад");
        InlineKeyboardMarkup adoptionRulesKeyboard = InlineKeyboardMarkupHelper.createAdoptionRulesInlineKeyboard();
        responseAdoptionRules.replyMarkup(adoptionRulesKeyboard);
        SendResponse sendResponseAdoptionRules = animalShelterBot.execute(responseAdoptionRules);
        logger.info("Message sent status: {}", sendResponseAdoptionRules.isOk());
    }

    private void sendReportMessage(Long chatId) {
        String reportMessage = "Пожалуйста, пришлите:\n- Фото животного.\n- Рацион животного.\n- Общее самочувствие и привыкание к новому месту.\n- Изменение в поведении: отказ от старых привычек, приобретение новых.";
        sendMessage(chatId, reportMessage, null);
        // Add logic for processing the user's message and saving it to the database if needed
    }

    private void sendVolunteerMessage(Long chatId) {
        String volunteerMessage = "Волонтер в пути! Ожидайте!";
        sendMessage(chatId, volunteerMessage, null);
        // Add logic for connecting the user with a volunteer if needed
    }

    private void sendMainMenuMessage(Long chatId) {
        SendMessage response = new SendMessage(chatId, "Вы вернулись в главное меню. Выберите приют для каких животных вас интересует: ");
        InlineKeyboardMarkup menuKeyboard = InlineKeyboardMarkupHelper.createInlineKeyboard();
        response.replyMarkup(menuKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(response);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }
}
