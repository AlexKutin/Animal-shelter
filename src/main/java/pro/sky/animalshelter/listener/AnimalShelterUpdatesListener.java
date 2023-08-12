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
import pro.sky.animalshelter.dto.RulesDTO;
import pro.sky.animalshelter.exception.UserChatIdNotFoundException;
import pro.sky.animalshelter.keyBoard.InlineKeyboardMarkupHelper;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.service.RulesService;
import pro.sky.animalshelter.service.ShelterService;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AnimalShelterUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(AnimalShelterUpdatesListener.class);
    private final Map<Long, String> userContactMap = new HashMap<>();

    private final TelegramBot animalShelterBot;

    private final ShelterService shelterService;
    private final RulesService rulesService;
    private String chosenShelter;

    private Map<Long, ShelterType> chooseShelterType = new HashMap<>();

    public AnimalShelterUpdatesListener(TelegramBot animalShelterBot, ShelterService shelterService, RulesService rulesService) {
        this.animalShelterBot = animalShelterBot;
        this.shelterService = shelterService;
        this.rulesService = rulesService;
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
        } else if (userContactMap.containsKey(chatId)) {
            String chosenShelter = userContactMap.get(chatId);
            String userContacts = text;
            saveUserContacts(telegramId, chosenShelter, userContacts);
            userContactMap.remove(chatId);
            sendMessage(chatId, "Спасибо! Ваши контакты сохранены.", InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard());
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
        } else if ("AdoptionRulesIntroduction".equals(data)) {
            sendAdoptionRulesIntroduction(chatId);
        } else if ("AdoptionRulesDocuments".equals(data)) {
            sendListDocForTakePet(chatId);
        } else if ("AdoptionRulesTransport".equals(data)) {
            sendRulesTransport(chatId);
        } else if ("AdoptionRulesHouseSetup".equals(data)) {
            sendRulesHouseSetup(chatId);
        } else if ("AdoptionRulesHouseSetupPuppyKitten".equals(data)) {
            sendRulesHouseSetupForChild(chatId);
        } else if ("AdoptionRulesHouseSetupAdult".equals(data)) {
            sendRulesHouseSetupForAdult(chatId);
        } else if ("AdoptionRulesHouseSetupSpecialNeeds".equals(data)) {
            sendRulesHouseSetupForSpecial(chatId);
        } else if ("BackToPetHouseSelection".equals(data)) {
            sendRulesHouseSetup(chatId);
        } else if ("AdoptionRulesRejectionReasons".equals(data)) {
            sendReasonsRefusal(chatId);
        } else if ("Cynologist".equals(data)) {
            sendCynologist(chatId);
        } else if ("AdviceFromCynologist".equals(data)) {
            sendAdviceFromCynologist(chatId);
        } else if ("ListCynologist".equals(data)) {
            sendListCynologist(chatId);
        } else if ("SendReport".equals(data)) {
            sendReportMessage(chatId);
        } else if ("CallVolunteer".equals(data)) {
            sendVolunteerMessage(chatId);
        } else if ("BackShelterMenu".equals(data)) {
            sendMainMenuMessage(chatId);
        } else if ("BackMainMenu".equals(data)) {
            backMainMenu(chatId);
        } else if ("BackShelterInfo".equals(data)) {
            sendShelterInfoOptions(chatId);
        } else if ("BackAdoptionRules".equals(data)) {
            sendAdoptionRules(chatId);
        } else if ("BackToCynologist".equals(data)) {
            sendCynologist(chatId);
        } else if ("CancelContactInput".equals(data)) {
            userContactMap.remove(chatId);
            sendMessage(chatId, "Вы отменили ввод контактов.", null);
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
            chooseShelterType.put(chatId, ShelterType.DOG_SHELTER);
        } else if (data.contains("кошек")) {
            shelterName = shelterService.getCatShelter().getShelterName();
            chooseShelterType.put(chatId, ShelterType.CAT_SHELTER);
        }

        if (shelterName != null) {
            chosenShelter = shelterName;
            SendMessage response = new SendMessage(chatId, data + " " + shelterName +
                    " приветствует Вас! Спасибо за выбор! Выберите, что вас интересует:\n" +
                    "1. \uD83C\uDFE0 Информация о приюте\n" +
                    "2. \uD83D\uDC3E Как забрать животное\n" +
                    "3. \uD83D\uDCDD Отправить отчет о животном\n" +
                    "4. \uD83E\uDDE1 Позвать волонтера\n" +
                    "⬅ Назад");
            InlineKeyboardMarkup menuKeyboard = InlineKeyboardMarkupHelper.createMainMenuInlineKeyboard();
            response.replyMarkup(menuKeyboard);
            SendResponse sendResponse = animalShelterBot.execute(response);
            logger.info("Message sent status: {}", sendResponse.isOk());
        } else {
            sendMessage(chatId, "\uD83D\uDEAB Ошибка в выборе приюта.", null);
        }
    }

    private void sendShelterInfoOptions(Long chatId) {
        SendMessage responseInfo = new SendMessage(chatId, "Выберите что вы хотите узнать о приюте:\n" +
                "1. \uD83C\uDFE0 О приюте\n" +
                "2. \uD83D\uDDD3 Расписание и адрес\n" +
                "3. \uD83D\uDCDE Контакты приюта\n" +
                "4. \uD83D\uDC6E Контакты охраны для получения пропуска\n" +
                //схема проезда
                "5. \uD83D\uDEE1 Техника безопасности на территории приюта\n" +
                "6. \uD83D\uDCDD Оставить контакты\n" +
                "7. \uD83E\uDDE1 Позвать волонтера\n" +
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
        InlineKeyboardMarkup keyboardMarkupCancelContactInput = InlineKeyboardMarkupHelper.createCancelContactInputInlineKeyboard();
        responseShelterInfoLeaveContacts.replyMarkup(keyboardMarkupCancelContactInput);
        SendResponse sendResponse = animalShelterBot.execute(responseShelterInfoLeaveContacts);
        logger.info("Message sent status: {}", sendResponse.isOk());
        userContactMap.put(chatId, chosenShelter);
    }

    private void saveUserContacts(Long telegramId, String chosenShelter, String userContacts) {
        try {
            String url = "jdbc:postgresql://localhost:5432/shelter_db";
            String username = "shelter_svc";
            String password = "AnimalShelter";

            Connection connection = DriverManager.getConnection(url, username, password);
            String tableName = chosenShelter.equals("Happy dog") ? "dog_shelter_users" : "cat_shelter_users";
            String insertQuery = "INSERT INTO " + tableName + " (telegram_id, user_contacts, shelter_id) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setLong(1, telegramId);
            preparedStatement.setString(2, userContacts);
            preparedStatement.setInt(3, chosenShelter.equals("Happy dog") ? 1 : 2);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void backMainMenu(Long chatId) {
        SendMessage response = new SendMessage(chatId,
                "Вы вернулись в главное меню приюта. Выберите, что вас интересует:\n" +
                        "1. \uD83C\uDFE0 Информация о приюте\n" +
                        "2. \uD83D\uDC3E Как забрать животное\n" +
                        "3. \uD83D\uDCDD Отправить отчет о животном\n" +
                        "4. \uD83E\uDDE1 Позвать волонтера\n" +
                        "⬅ Назад");
        InlineKeyboardMarkup menuKeyboard = InlineKeyboardMarkupHelper.createMainMenuInlineKeyboard();
        response.replyMarkup(menuKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(response);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendAdoptionRules(Long chatId) {
        SendMessage responseAdoptionRules = null;
        if (chosenShelter.equals("Happy dog")) {
            responseAdoptionRules = new SendMessage(chatId, "Мы рады, что вы заинтересованы помочь нашим пушистым друзьям. Но перед этим ознакомьтесь с правилами:\n" +
                    "1. ✨ Правила знакомства с животными \n" +
                    "2. \uD83D\uDCDC Список необходимых документов для усыновления\n" +
                    "3. \uD83D\uDE9A Транспортировка животного\n" +
                    "4. \uD83C\uDFE0 Обустройство дома для животных\n" +
                    "5. ❌ Причины отказа\n" +
                    "6. \uD83D\uDC3E Советы, а также контакты проверенных кинологов\n" +
                    "7. \uD83E\uDDE1 Позвать волонтера\n" +
                    "⬅ Назад");
            InlineKeyboardMarkup adoptionRulesKeyboard = InlineKeyboardMarkupHelper.createAdoptionRulesForDogInlineKeyboard();
            responseAdoptionRules.replyMarkup(adoptionRulesKeyboard);
            SendResponse sendResponseAdoptionRules = animalShelterBot.execute(responseAdoptionRules);
            logger.info("Message sent status: {}", sendResponseAdoptionRules.isOk());
        } else {
            responseAdoptionRules = new SendMessage(chatId, "Мы рады, что вы заинтересованы помочь нашим пушистым друзьям. Но перед этим ознакомьтесь с правилами:\n" +
                    "1. ✨ Правила знакомства с животными \n" +
                    "2. \uD83D\uDCDC Список необходимых документов для усыновления\n" +
                    "3. \uD83D\uDE9A Транспортировка животного\n" +
                    "4. \uD83C\uDFE0 Обустройство дома для животных\n" +
                    "5. ❌ Причины отказа\n" +
                    "6. \uD83E\uDDE1 Позвать волонтера\n" +
                    "⬅ Назад");
            InlineKeyboardMarkup adoptionRulesKeyboard = InlineKeyboardMarkupHelper.createAdoptionRulesInlineKeyboard();
            responseAdoptionRules.replyMarkup(adoptionRulesKeyboard);
            SendResponse sendResponseAdoptionRules = animalShelterBot.execute(responseAdoptionRules);
            logger.info("Message sent status: {}", sendResponseAdoptionRules.isOk());
        }
    }

    private void sendAdoptionRulesIntroduction(Long chatId) {
        String adoptionRulesIntroduction = rulesService.getAllRules().getRilesMeeting();
        SendMessage responseAdoptionRulesIntroduction = new SendMessage(chatId, adoptionRulesIntroduction);
        InlineKeyboardMarkup BackToAdoptionRulesInlineKeyboard = InlineKeyboardMarkupHelper.createBackToAdoptionRulesInlineKeyboard();
        responseAdoptionRulesIntroduction.replyMarkup(BackToAdoptionRulesInlineKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(responseAdoptionRulesIntroduction);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendListDocForTakePet(Long chatId) {
//        String rules = shelterService.getCatShelter()
        String listDocForTakePet = rulesService.getAllRules().getListDocForTakePet();
        SendMessage responseListDocForTakePet = new SendMessage(chatId, listDocForTakePet);
        InlineKeyboardMarkup BackToAdoptionRulesInlineKeyboard = InlineKeyboardMarkupHelper.createBackToAdoptionRulesInlineKeyboard();
        responseListDocForTakePet.replyMarkup(BackToAdoptionRulesInlineKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(responseListDocForTakePet);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    public void sendRulesTransport(Long chatId) {
        String rulesTransport = rulesService.getAllRules().getRulesTransportation();
        SendMessage responseRulesTransport = new SendMessage(chatId, rulesTransport);
        InlineKeyboardMarkup backToAdoptionRulesInlineKeyboard = InlineKeyboardMarkupHelper.createBackToAdoptionRulesInlineKeyboard();
        responseRulesTransport.replyMarkup(backToAdoptionRulesInlineKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(responseRulesTransport);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    public void sendRulesHouseSetup(Long chatId) {
        SendMessage responseRulesHouseSetup = new SendMessage(chatId, "Выберите обустройство дома какого животного вас интересует:\n" +
                "1. \uD83D\uDC23 Обустройство дома котенка или щенка\n" +
                "2. \uD83C\uDFE0 Обустройство дома взрослого животного\n" +
                "3. ♿ Обустройство дома животного с ограниченными возможностями\n" +
                "⬅ Назад");
        InlineKeyboardMarkup petHouseSelectionKeyBoard = InlineKeyboardMarkupHelper.createPetHouseSelectionKeyBoard();
        responseRulesHouseSetup.replyMarkup(petHouseSelectionKeyBoard);
        SendResponse sendResponse = animalShelterBot.execute(responseRulesHouseSetup);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    public void sendRulesHouseSetupForChild(Long chatId) {
        RulesDTO rulesDTO = rulesService.getRulesByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseRulesHouseSetupPuppyKitten = new SendMessage(chatId, rulesDTO.getRulesGHForChildPet());
        InlineKeyboardMarkup backToPetHouseSelectionKeyBoard = InlineKeyboardMarkupHelper.createBackToPetHouseSelectionKeyBoard();
        responseRulesHouseSetupPuppyKitten.replyMarkup(backToPetHouseSelectionKeyBoard);
        SendResponse sendResponse = animalShelterBot.execute(responseRulesHouseSetupPuppyKitten);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    public void sendRulesHouseSetupForAdult(Long chatId) {
        SendMessage responseRulesHouseSetup = new SendMessage(chatId, rulesService.getAllRules().getRulesGHForAdultPet());
        InlineKeyboardMarkup backToPetHouseSelectionKeyBoard = InlineKeyboardMarkupHelper.createBackToPetHouseSelectionKeyBoard();
        responseRulesHouseSetup.replyMarkup(backToPetHouseSelectionKeyBoard);
        SendResponse sendResponse = animalShelterBot.execute(responseRulesHouseSetup);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    public void sendRulesHouseSetupForSpecial(Long chatId) {
        SendMessage responseRulesHouseSetup = new SendMessage(chatId, rulesService.getAllRules().getRulesGHForSpecialPet());
        InlineKeyboardMarkup backToPetHouseSelectionKeyBoard = InlineKeyboardMarkupHelper.createBackToPetHouseSelectionKeyBoard();
        responseRulesHouseSetup.replyMarkup(backToPetHouseSelectionKeyBoard);
        SendResponse sendResponse = animalShelterBot.execute(responseRulesHouseSetup);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    public void sendCynologist(Long chatId) {
        SendMessage responseCynologist = new SendMessage(chatId, "Выберите что вы хотите узнать:\n" +
                "1. \uD83D\uDC3E Советы кинолога\n" +
                "2. \uD83D\uDCDE Контакты провереных кинологов\n" +
                "⬅ Назад");
        InlineKeyboardMarkup cynologistKeyBoard = InlineKeyboardMarkupHelper.createCynologistKeyBoard();
        responseCynologist.replyMarkup(cynologistKeyBoard);
        SendResponse sendResponse = animalShelterBot.execute(responseCynologist);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    public void sendAdviceFromCynologist(Long chatId) {
        SendMessage responseAdviceFromCynologist = new SendMessage(chatId, rulesService.getAllRules().getAdviceFromCynologist());
        InlineKeyboardMarkup backToCynologist = InlineKeyboardMarkupHelper.createBackToCynologist();
        responseAdviceFromCynologist.replyMarkup(backToCynologist);
        SendResponse sendResponse = animalShelterBot.execute(responseAdviceFromCynologist);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    public void sendListCynologist(Long chatId) {
        SendMessage responseListCynologist = new SendMessage(chatId, rulesService.getAllRules().getListCynologist());
        InlineKeyboardMarkup backToCynologist = InlineKeyboardMarkupHelper.createBackToCynologist();
        responseListCynologist.replyMarkup(backToCynologist);
        SendResponse sendResponse = animalShelterBot.execute(responseListCynologist);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    public void sendReasonsRefusal(Long chatId) {
        String rulesReasonsRefusal = rulesService.getAllRules().getReasonsRefusal();
        SendMessage responseRulesTransport = new SendMessage(chatId, rulesReasonsRefusal);
        InlineKeyboardMarkup backToAdoptionRulesInlineKeyboard = InlineKeyboardMarkupHelper.createBackToAdoptionRulesInlineKeyboard();
        responseRulesTransport.replyMarkup(backToAdoptionRulesInlineKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(responseRulesTransport);
        logger.info("Message sent status: {}", sendResponse.isOk());
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

    private ShelterType getShelterTypeByUserChatId(Long chatId) {
        if (chooseShelterType.containsKey(chatId)) {
            return chooseShelterType.get(chatId);
        }
        throw new UserChatIdNotFoundException(String.format("Telegram user chatId = %d not found", chatId));
    }
}
