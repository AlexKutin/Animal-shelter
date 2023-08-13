package pro.sky.animalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.GetChatMember;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.GetChatMemberResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import pro.sky.animalshelter.dto.RulesDTO;
import pro.sky.animalshelter.dto.ShelterDTO;
import pro.sky.animalshelter.exception.UserChatIdNotFoundException;
import pro.sky.animalshelter.keyBoard.InlineKeyboardMarkupHelper;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.model.Volunteer;
import pro.sky.animalshelter.service.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ComponentScan(basePackages = {"pro.sky.animalshelter.service", "pro.sky.animalshelter.listener", "pro.sky.animalshelter.repository"})
public class AnimalShelterUpdatesListener implements UpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(AnimalShelterUpdatesListener.class);
    private final Map<Long, ShelterType> userContactMap = new HashMap<>();
    private final TelegramBot animalShelterBot;
    private final ShelterService shelterService;
    private final RulesService rulesService;
    private final UserShelterService userShelterService;
    private final VolunteerService volunteerService;
    private final Map<Long, ShelterType> chooseShelterType = new HashMap<>();

    @Autowired
    public AnimalShelterUpdatesListener(TelegramBot animalShelterBot, ShelterService shelterService,
                                        RulesService rulesService, UserShelterService userShelterService, VolunteerService volunteerService) {
        this.animalShelterBot = animalShelterBot;
        this.shelterService = shelterService;
        this.rulesService = rulesService;
        this.userShelterService = userShelterService;
        this.volunteerService = volunteerService;
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
        String userContacts = message.text();
        Long telegramId = message.from().id();          // Это telegram_id пользователя
        String userName = message.from().username();    // telegram username пользователя
        String firstName = message.from().firstName();  // telegram firstname пользователя
        String lastName = message.from().lastName();    // telegram lastname пользователя

        if ("/start".equals(text)) {
            sendStartMessage(chatId);
        } else if (userContactMap.containsKey(chatId)) {
            ShelterType chosenShelterType = getShelterTypeByUserChatId(chatId);
            userShelterService.saveUserContacts(chosenShelterType, telegramId, userName, firstName, lastName, userContacts);
            SendMessage response = new SendMessage(chatId, "Спасибо! Ваши контакты сохранены. Наши волонтеры свяжутся с вами в ближайшее время.");
            InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard();
            response.replyMarkup(keyboardMarkup);
            SendResponse sendResponse = animalShelterBot.execute(response);
            logger.info("Message sent status: {}", sendResponse.isOk());
        } else if ("/getchatid".equals(text)) {
            getChatId(chatId);
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
            ShelterType chosenShelterType = chooseShelterType.get(chatId);
            sendVolunteerMessage(chatId, chosenShelterType);
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
            ShelterDTO shelterDTO = shelterService.getShelterByShelterType(getShelterTypeByUserChatId(chatId));
            SendMessage response = new SendMessage(chatId, data + " " + shelterDTO.getShelterName() +
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
        ShelterDTO shelterDTO = shelterService.getShelterByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseShelterInfoAbout = new SendMessage(chatId, "Приют называется " + shelterDTO.getShelterName() + ". " + shelterDTO.getShelterDescription());
        InlineKeyboardMarkup BackShelterInfoKeyboard = InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard();
        responseShelterInfoAbout.replyMarkup(BackShelterInfoKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(responseShelterInfoAbout);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendShelterInfoAddress(Long chatId) {
        ShelterDTO shelterDTO = shelterService.getShelterByShelterType(getShelterTypeByUserChatId(chatId));
        String shelterName = shelterDTO.getShelterName();

        String imageFileName = shelterName.replace(" ", "") + "shelter.png";

        // Получить InputStream из ресурсов
        InputStream imageInputStream = getClass().getResourceAsStream("/" + imageFileName);

        if (imageInputStream != null) {
            try {
                // Создать временный файл
                File tempImageFile = File.createTempFile("tempImage", ".png");
                tempImageFile.deleteOnExit();

                // Копировать содержимое InputStream в временный файл
                FileUtils.copyInputStreamToFile(imageInputStream, tempImageFile);

                // Отправить фото с временного файла
                SendPhoto sendPhoto = new SendPhoto(chatId, tempImageFile);
                SendResponse sendPhotoResponse = animalShelterBot.execute(sendPhoto);

                if (sendPhotoResponse.isOk()) {
                    logger.info("Photo sent successfully");
                } else {
                    logger.error("Failed to send photo: {}", sendPhotoResponse.description());
                }
            } catch (IOException e) {
                logger.error("Error while sending photo: {}", e.getMessage());
            }
        } else {
            logger.error("Image file not found: {}", imageFileName);
        }

        SendMessage responseShelterInfoAddress = new SendMessage(chatId, shelterDTO.getShelterAddress() + "\nДля удобного проезда к нам, пожалуйста, используйте представленную выше схему.");
        InlineKeyboardMarkup BackShelterInfoKeyboard = InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard();
        responseShelterInfoAddress.replyMarkup(BackShelterInfoKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(responseShelterInfoAddress);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendShelterContacts(Long chatId) {
        ShelterDTO shelterDTO = shelterService.getShelterByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseShelterInfoContacts = new SendMessage(chatId, "Контакты для связи с нами:\nТел:" + shelterDTO.getShelterContacts());
        InlineKeyboardMarkup BackShelterInfoKeyboard = InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard();
        responseShelterInfoContacts.replyMarkup(BackShelterInfoKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(responseShelterInfoContacts);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendShelterSecurityContacts(Long chatId) {
        ShelterDTO shelterDTO = shelterService.getShelterByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseShelterInfoContacts = new SendMessage(chatId, "Контакты охраны для получения пропуска:\nТел:" + shelterDTO.getSecurityContacts());
        InlineKeyboardMarkup BackShelterInfoKeyboard = InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard();
        responseShelterInfoContacts.replyMarkup(BackShelterInfoKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(responseShelterInfoContacts);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendShelterSafetyTips(Long chatId) {
        ShelterDTO shelterDTO = shelterService.getShelterByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseShelterInfoContacts = new SendMessage(chatId, shelterDTO.getSafetyInfo());
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
        ShelterType shelterType = getShelterTypeByUserChatId(chatId);
        userContactMap.put(chatId, shelterType);
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
        ShelterType shelterType = getShelterTypeByUserChatId(chatId);
        if (shelterType == ShelterType.DOG_SHELTER) {
            SendMessage responseAdoptionRules = new SendMessage(chatId, "Мы рады, что вы заинтересованы помочь нашим пушистым друзьям. Но перед этим ознакомьтесь с правилами:\n" +
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
        } else if (shelterType == ShelterType.CAT_SHELTER) {
            SendMessage responseAdoptionRules = new SendMessage(chatId, "Мы рады, что вы заинтересованы помочь нашим пушистым друзьям. Но перед этим ознакомьтесь с правилами:\n" +
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
        RulesDTO rulesDTO = rulesService.getRulesByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseAdoptionRulesIntroduction = new SendMessage(chatId, rulesDTO.getRilesMeeting());
        InlineKeyboardMarkup BackToAdoptionRulesInlineKeyboard = InlineKeyboardMarkupHelper.createBackToAdoptionRulesInlineKeyboard();
        responseAdoptionRulesIntroduction.replyMarkup(BackToAdoptionRulesInlineKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(responseAdoptionRulesIntroduction);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendListDocForTakePet(Long chatId) {
        RulesDTO rulesDTO = rulesService.getRulesByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseListDocForTakePet = new SendMessage(chatId, rulesDTO.getListDocForTakePet());
        InlineKeyboardMarkup BackToAdoptionRulesInlineKeyboard = InlineKeyboardMarkupHelper.createBackToAdoptionRulesInlineKeyboard();
        responseListDocForTakePet.replyMarkup(BackToAdoptionRulesInlineKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(responseListDocForTakePet);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    public void sendRulesTransport(Long chatId) {
        RulesDTO rulesDTO = rulesService.getRulesByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseRulesTransport = new SendMessage(chatId, rulesDTO.getRulesTransportation());
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
        RulesDTO rulesDTO = rulesService.getRulesByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseRulesHouseSetup = new SendMessage(chatId, rulesDTO.getRulesGHForAdultPet());
        InlineKeyboardMarkup backToPetHouseSelectionKeyBoard = InlineKeyboardMarkupHelper.createBackToPetHouseSelectionKeyBoard();
        responseRulesHouseSetup.replyMarkup(backToPetHouseSelectionKeyBoard);
        SendResponse sendResponse = animalShelterBot.execute(responseRulesHouseSetup);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    public void sendRulesHouseSetupForSpecial(Long chatId) {
        RulesDTO rulesDTO = rulesService.getRulesByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseRulesHouseSetup = new SendMessage(chatId, rulesDTO.getRulesGHForSpecialPet());
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
        RulesDTO rulesDTO = rulesService.getRulesByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseAdviceFromCynologist = new SendMessage(chatId, rulesDTO.getAdviceFromCynologist());
        InlineKeyboardMarkup backToCynologist = InlineKeyboardMarkupHelper.createBackToCynologist();
        responseAdviceFromCynologist.replyMarkup(backToCynologist);
        SendResponse sendResponse = animalShelterBot.execute(responseAdviceFromCynologist);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    public void sendListCynologist(Long chatId) {
        RulesDTO rulesDTO = rulesService.getRulesByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseListCynologist = new SendMessage(chatId, rulesDTO.getListCynologist());
        InlineKeyboardMarkup backToCynologist = InlineKeyboardMarkupHelper.createBackToCynologist();
        responseListCynologist.replyMarkup(backToCynologist);
        SendResponse sendResponse = animalShelterBot.execute(responseListCynologist);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    public void sendReasonsRefusal(Long chatId) {
        RulesDTO rulesDTO = rulesService.getRulesByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseRulesTransport = new SendMessage(chatId, rulesDTO.getReasonsRefusal());
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

    //    private void sendVolunteerMessage(Long chatId) {
//        String volunteerMessage = "Волонтер в пути! Ожидайте!";
//        sendMessage(chatId, volunteerMessage, null);
//        // Add logic for connecting the user with a volunteer if needed
//    }
    private void sendVolunteerMessage(Long chatId, ShelterType chosenShelterType) {
        String volunteerMessageToUser = "Волонтер в пути! Ожидайте!";
        sendMessage(chatId, volunteerMessageToUser, null);

        Collection<Volunteer> volunteers = volunteerService.findVolunteersByShelterType(chosenShelterType);

        for (Volunteer volunteer : volunteers) {
            String volunteerMessage = "Пользователь с id @" + getUserNameByChatId(chatId) + " нуждается в помощи. Свяжитесь с ним через Telegram.";
            sendMessageToVolunteer(volunteer.getChatId(), volunteerMessage);
        }
    }

    private String getUserNameByChatId(Long chatId) {
        GetChatMember getChatMember = new GetChatMember(chatId.toString(), chatId.intValue());

        GetChatMemberResponse response = animalShelterBot.execute(getChatMember);
        if (response.isOk()) {
            ChatMember chatMember = response.chatMember();
            if (chatMember.user() != null && chatMember.user().username() != null) {
                return chatMember.user().username();
            }
        }

        return "";
    }

    private void sendMessageToVolunteer(Long volunteerChatId, String message) {
        SendMessage request = new SendMessage(volunteerChatId.toString(), message);
        animalShelterBot.execute(request);
    }

    private void sendMainMenuMessage(Long chatId) {
        SendMessage response = new SendMessage(chatId, "Вы вернулись в главное меню. Выберите приют для каких животных вас интересует: ");
        InlineKeyboardMarkup menuKeyboard = InlineKeyboardMarkupHelper.createInlineKeyboard();
        response.replyMarkup(menuKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(response);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }
    private void getChatId(Long chatId) {
        SendMessage request = new SendMessage(chatId.toString(), "Ваш ChatId: " + chatId);
        animalShelterBot.execute(request);
    }

    private ShelterType getShelterTypeByUserChatId(Long chatId) {
        if (chooseShelterType.containsKey(chatId)) {
            return chooseShelterType.get(chatId);
        }
        throw new UserChatIdNotFoundException(String.format("Telegram user chatId = %d not found", chatId));
    }
}
