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

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class AnimalShelterUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(AnimalShelterUpdatesListener.class);

    private final TelegramBot animalShelterBot;

    public AnimalShelterUpdatesListener(TelegramBot animalShelterBot) {
        this.animalShelterBot = animalShelterBot;
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
            updates.stream()
                    .filter(update -> update.message() != null)
                    .forEach(update -> {
                        logger.info("Handles update: {}", update);
                        Message message = update.message();
                        Long chatId = message.chat().id();
                        String text = message.text();

                        if ("/start".equals(text)) {
                            SendMessage response = new SendMessage(chatId,
                                    "Добро пожаловать в приют для животных города Астаны! Выберите приют для каких животных вас интересует:");
                            InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkupHelper.createInlineKeyboard(
                            );
                            response.replyMarkup(keyboardMarkup);
                            SendResponse sendResponse = animalShelterBot.execute(response);
                            logger.info("Message sent status: {}", sendResponse.isOk());
                        } else {
                            InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkupHelper.createInlineKeyboard(
                            );
                            sendMessage(chatId, "Некорректное сообщение.", keyboardMarkup);
                        }
                    });

            updates.stream()
                    .filter(update -> update.callbackQuery() != null)
                    .forEach(update -> {
                        logger.info("Handles callback query: {}", update);
                        CallbackQuery callbackQuery = update.callbackQuery();
                        Long chatId = callbackQuery.message().chat().id();
                        String data = callbackQuery.data();

                        // Обработка callback-запросов от нажатых кнопок
                        if ("Приют кошек\uD83D\uDC31".equals(data) || "Приют собак\uD83D\uDC36".equals(data)) {
                            SendMessage response = new SendMessage(chatId, data + " приветствует Вас! Спасибо за выбор! Выберите, что вас интересует:\n" +
                                    "1. Информация о приюте\n" +
                                    "2. Как забрать животное\n" +
                                    "3. Отправить отчет о животном\n" +
                                    "4. Позвать волонтера\n" +
                                    "⬅ Назад");
                            InlineKeyboardMarkup menuKeyboard = InlineKeyboardMarkupHelper.createMainMenuInlineKeyboard();
                            response.replyMarkup(menuKeyboard);
                            SendResponse sendResponse = animalShelterBot.execute(response);
                            logger.info("Message sent status: {}", sendResponse.isOk());
                        } else if ("Info".equals(data)) {
                            SendMessage responseInfo = new SendMessage(chatId, "Выберите что вы хотите узнать о прюте:\n" +
                                    "1. О приюте\n" +
                                    "2. Расписание и адрес\n" +
                                    "3. Контакты охраны\n" +
                                    "4. Техника безопасности\n" +
                                    "5. Оставить контакты\n" +
                                    "6. Позвать волонтера\n" +
                                    "⬅ Назад");
                            InlineKeyboardMarkup shelterInfoKeyboard = InlineKeyboardMarkupHelper.createShelterInfoInlineKeyboard();
                            responseInfo.replyMarkup(shelterInfoKeyboard);
                            SendResponse sendResponseInfo = animalShelterBot.execute(responseInfo);
                            logger.info("Message sent status: {}", sendResponseInfo.isOk());
                        } else if ("AdoptionRules".equals(data)) {
                            SendMessage responseAdoptionRules = new SendMessage(chatId, "Мы рады, что вы заинтересованы помочь нашим пушистым друзьям. Но перед этим ознакомтесь с правилами:\n" +
                                    "1. Правила знакомства с животными \n" +
                                    "2. Список необходимых документов для усыновления\n" +
                                    "3. Транспортировка животного\n" +
                                    "4. Обустройство дома для самых маленьких\n" +
                                    "5. Обустройство дома для взрослого животного\n" +
                                    "6. Обустройство дома для животных с ограниченными возможностями\n" +
                                    "7. Причины отказа\n" +
                                    "8. Оставьте нам свои контакты для связи\n" +
                                    "9. Позвать волонтера\n" +
                                    "⬅ Назад"

                                    //В теллеграм ограничение по выводимым кнопкам. Максимально 8шт. Нужно будет один пункт сократить.
                            );
                            InlineKeyboardMarkup adoptionRulesKeyboard = InlineKeyboardMarkupHelper.createAdoptionRulesInlineKeyboard();
                            responseAdoptionRules.replyMarkup(adoptionRulesKeyboard);
                            SendResponse sendResponseAdoptionRules = animalShelterBot.execute(responseAdoptionRules);
                            logger.info("Message sent status: {}", sendResponseAdoptionRules.isOk());
                        } else if ("SendReport".equals(data)) {
                            String reportMessage = "Пожалуйста, пришлите:\n- Фото животного.\n- Рацион животного.\n- Общее самочувствие и привыкание к новому месту.\n- Изменение в поведении: отказ от старых привычек, приобретение новых.";
                            sendMessage(chatId, reportMessage, null);
                            //обработать метод принятия сообщений от пользователя и внесение в БД
                        } else if ("CallVolunteer".equals(data)) {
                            String volunteerMessage = "Волонтер в пути! Ожидайте!";
                            sendMessage(chatId, volunteerMessage, null);
                            //придумать логику связи пользователя с волонтером
                        }else if ("BackShelterMenu".equals(data)) { // Обработка возврата в главное меню
                            SendMessage response = new SendMessage(chatId, "Вы вернулись в главное меню. Выберите приют для каких животных вас интересует: ");
                            InlineKeyboardMarkup menuKeyboard = InlineKeyboardMarkupHelper.createInlineKeyboard();
                            response.replyMarkup(menuKeyboard);
                            SendResponse sendResponse = animalShelterBot.execute(response);
                            logger.info("Message sent status: {}", sendResponse.isOk());
                        } else if ("BackMainMenu".equals(data)) { // Обработка возврата в меню "Информация о приюте"
                            SendMessage responseInfo = new SendMessage(chatId, "Вы вернулись в главное меню приюта. Выберите что вы хотите узнать о прюте:\n" +
                                    "1. Информация о приюте\n" +
                                    "2. Как забрать животное\n" +
                                    "3. Отправить отчет о животном\n" +
                                    "4. Позвать волонтера\n" +
                                    "⬅ Назад");
                            InlineKeyboardMarkup shelterInfoKeyboard = InlineKeyboardMarkupHelper.createMainMenuInlineKeyboard();
                            responseInfo.replyMarkup(shelterInfoKeyboard);
                            SendResponse sendResponseInfo = animalShelterBot.execute(responseInfo);
                            logger.info("Message sent status: {}", sendResponseInfo.isOk());
                        }
                    });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
// ----------- После обработки логики приложения выше написанный код будет обернут в switch на основе кода написанного ниже.
// ----------- Просьба пока не удалять ниде написанный код:))
//    private void handleCallbackQuery(CallbackQuery callbackQuery) {
//        logger.info("Handles callback query: {}", callbackQuery);
//        Long chatId = callbackQuery.message().chat().id();
//        String data = callbackQuery.data();
//
//        if (data.startsWith("ShelterInfo")) {
//            // Handle shelter information options
//            String shelterInfoOption = data.replace("ShelterInfo", "");
//            switch (shelterInfoOption) {
//                case "About":
//                    // Provide information about the shelter
//                    sendMessage(chatId, "О приюте: ...", null);
//                    break;
//                case "Address":
//                    // Provide schedule and address information
//                    sendMessage(chatId, "Расписание и адрес: ...", null);
//                    break;
//                case "SecurityContacts":
//                    // Provide security contacts for car passes
//                    sendMessage(chatId, "Контакты охраны: ...", null);
//                    break;
//                case "SafetyTips":
//                    // Provide general safety tips within the shelter
//                    sendMessage(chatId, "Техника безопасности: ...", null);
//                    break;
//                case "LeaveContacts":
//                    // Allow the user to leave their contacts for communication
//                    sendMessage(chatId, "Оставьте ваши контакты...", null);
//                    break;
//                case "CallVolunteer":
//                    // Initiate a live chat with a volunteer
//                    // Handle this functionality as needed based on your requirements
//                    break;
//                default:
//                    // Handle unknown shelter information option
//                    break;
//            }
//        } else if (data.startsWith("AdoptionRules")) {
//            // Handle adoption rules options
//            String adoptionRulesOption = data.replace("AdoptionRules", "");
//            switch (adoptionRulesOption) {
//                // Implement the logic for adoption rules options similarly to shelter information
//                // options above using switch-case statements.
//            }
//        }
//        // Handle other callback queries here if needed
//    }
}
