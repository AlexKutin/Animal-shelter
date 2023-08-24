package pro.sky.animalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.GetChatMemberResponse;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import pro.sky.animalshelter.Constants.CallbackConstants;
import pro.sky.animalshelter.dto.ReportAnimalDTO;
import pro.sky.animalshelter.dto.RulesDTO;
import pro.sky.animalshelter.dto.ShelterDTO;
import pro.sky.animalshelter.dto.UserShelterDTO;
import pro.sky.animalshelter.exception.UserChatIdNotFoundException;
import pro.sky.animalshelter.keyBoard.InlineKeyboardMarkupHelper;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.service.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@ComponentScan(basePackages = {"pro.sky.animalshelter.service", "pro.sky.animalshelter.listener", "pro.sky.animalshelter.repository"})
public class AnimalShelterUpdatesListener implements UpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(AnimalShelterUpdatesListener.class);
    private final Map<Long, ShelterType> userContactMap = new HashMap<>();
    private TelegramBot animalShelterBot;
    private final ShelterService shelterService;
    private final RulesService rulesService;
    private final UserShelterService userShelterService;
    private final VolunteerService volunteerService;
    private final ReportService reportService;
    private final AdopterService adopterService;
    private final Map<Long, ShelterType> chooseShelterType = new HashMap<>();
    private final Map<Long, ShelterType> reportDataMap = new HashMap<>();

    @Autowired
    public AnimalShelterUpdatesListener(TelegramBot animalShelterBot, ShelterService shelterService,
                                        RulesService rulesService, UserShelterService userShelterService, VolunteerService volunteerService, ReportService reportService, AdopterService adopterService) {
        this.animalShelterBot = animalShelterBot;
        this.shelterService = shelterService;
        this.rulesService = rulesService;
        this.userShelterService = userShelterService;
        this.volunteerService = volunteerService;
        this.reportService = reportService;
        this.adopterService = adopterService;
    }

    @PostConstruct
    public void init() {
        animalShelterBot.setUpdatesListener(this);
    }

    private void sendMessage(Long chatId, String message) {
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
            UserShelterDTO userShelterDTO = new UserShelterDTO();
            userShelterDTO.setShelterType(chosenShelterType);
            userShelterDTO.setChatId(chatId);
            userShelterDTO.setTelegramId(telegramId);
            userShelterDTO.setUserName(userName);
            userShelterDTO.setFirstName(firstName);
            userShelterDTO.setLastName(lastName);
            userShelterDTO.setUserContacts(userContacts);

            userShelterService.saveUserContacts(userShelterDTO);
            SendMessage response = new SendMessage(chatId, "Спасибо! Ваши контакты сохранены. Наши волонтеры свяжутся с вами в ближайшее время.");
            InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard();
            response.replyMarkup(keyboardMarkup);
            SendResponse sendResponse = animalShelterBot.execute(response);
            logger.info("Message sent status: {}", sendResponse.isOk());
        } else if ("/getchatid".equals(text)) {
            getChatId(chatId);
        } else if (reportDataMap.containsKey(chatId)) {
            PhotoSize[] photoSizesArray = message.photo();
            List<PhotoSize> photos = Arrays.asList(photoSizesArray);
            String fileId = photos.get(photos.size() - 1).fileId();

            // Получаем фактические данные файла
            byte[] photoData = getPhotoData(fileId);

            // Сохранение отчета
            saveReportToService(chatId, text, photoData);

            // Отправка подтверждающего сообщения
            SendMessage sendMessage = new SendMessage(chatId, "Спасибо! Ваш отчет сохранен.");
            SendResponse sendResponse = animalShelterBot.execute(sendMessage);
        } else {
            sendInvalidMessage(chatId);
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.message().chat().id();
        String data = callbackQuery.data();

        if (CallbackConstants.SHELTER_CAT.equals(data) || CallbackConstants.SHELTER_DOG.equals(data)) {
            handleShelterInfoMessage(chatId, data);
        } else if (CallbackConstants.INFO.equals(data)) {
            sendShelterInfoOptions(chatId);
        } else if (CallbackConstants.SHELTER_INFO_ABOUT.equals(data)) {
            sendShelterInfoAbout(chatId);
        } else if (CallbackConstants.SHELTER_INFO_ADDRESS.equals(data)) {
            sendShelterInfoAddress(chatId);
        } else if (CallbackConstants.SHELTER_INFO_CONTACTS.equals(data)) {
            sendShelterContacts(chatId);
        } else if (CallbackConstants.SHELTER_INFO_SECURITY_CONTACTS.equals(data)) {
            sendShelterSecurityContacts(chatId);
        } else if (CallbackConstants.SHELTER_INFO_SAFETY_TIPS.equals(data)) {
            sendShelterSafetyTips(chatId);
        } else if (CallbackConstants.SHELTER_INFO_LEAVE_CONTACTS.equals(data)) {
            sendShelterLeaveContacts(chatId);
        } else if (CallbackConstants.ADOPTIONS_RULES.equals(data)) {
            sendAdoptionRules(chatId);
        } else if (CallbackConstants.ADOPTIONS_RULES_INTRODUCTION.equals(data)) {
            sendAdoptionRulesIntroduction(chatId);
        } else if (CallbackConstants.ADOPTIONS_RULES_DOCUMENTS.equals(data)) {
            sendListDocForTakePet(chatId);
        } else if (CallbackConstants.ADOPTIONS_RULES_TRANSPORT.equals(data)) {
            sendRulesTransport(chatId);
        } else if (CallbackConstants.ADOPTIONS_RULES_HOUSE_SETUP.equals(data)) {
            sendRulesHouseSetup(chatId);
        } else if (CallbackConstants.ADOPTIONS_RULES_HOUSE_SETUP_PUPPY_KITTEN.equals(data)) {
            sendRulesHouseSetupForChild(chatId);
        } else if (CallbackConstants.ADOPTIONS_RULES_HOUSE_SETUP_ADULT.equals(data)) {
            sendRulesHouseSetupForAdult(chatId);
        } else if (CallbackConstants.ADOPTIONS_RULES_HOUSE_SETUP_SPECIAL_NEEDS.equals(data)) {
            sendRulesHouseSetupForSpecial(chatId);
        } else if (CallbackConstants.BACK_TO_HOUSE_SELECTION.equals(data)) {
            sendRulesHouseSetup(chatId);
        } else if (CallbackConstants.ADOPTION_RULES_REJECTION_REASONS.equals(data)) {
            sendReasonsRefusal(chatId);
        } else if (CallbackConstants.CYNOLOGIST.equals(data)) {
            sendCynologist(chatId);
        } else if (CallbackConstants.ADVICE_FROM_CYNOLOGIST.equals(data)) {
            sendAdviceFromCynologist(chatId);
        } else if (CallbackConstants.LIST_CYNOLOGIST.equals(data)) {
            sendListCynologist(chatId);
        } else if (CallbackConstants.SEND_REPORT.equals(data)) {
            sendReportMessage(chatId);
        } else if (CallbackConstants.CALL_VOLUNTEER.equals(data)) {
            ShelterType chosenShelterType = chooseShelterType.get(chatId);
            sendVolunteerMessage(chatId, chosenShelterType);
        } else if (CallbackConstants.BACK_SHELTER_MENU.equals(data)) {
            sendMainMenuMessage(chatId);
        } else if (CallbackConstants.BACK_MAIN_MENU.equals(data)) {
            backMainMenu(chatId);
        } else if (CallbackConstants.BACK_SHELTER_INFO.equals(data)) {
            sendShelterInfoOptions(chatId);
        } else if (CallbackConstants.BACK_ADOPTION_RULES.equals(data)) {
            sendAdoptionRules(chatId);
        } else if (CallbackConstants.BACK_TO_CYNOLOGIST.equals(data)) {
            sendCynologist(chatId);
        } else if (CallbackConstants.CANCEL_CONTACT_INPUT.equals(data)) {
            userContactMap.remove(chatId);
            sendMessage(chatId, "Вы отменили ввод контактов.");
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
        sendMessage(chatId, "Некорректное сообщение.");
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
            sendMessage(chatId, "\uD83D\uDEAB Ошибка в выборе приюта.");
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

    private void backMainMenu(Long chatId) {
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

    private void sendRulesTransport(Long chatId) {
        RulesDTO rulesDTO = rulesService.getRulesByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseRulesTransport = new SendMessage(chatId, rulesDTO.getRulesTransportation());
        InlineKeyboardMarkup backToAdoptionRulesInlineKeyboard = InlineKeyboardMarkupHelper.createBackToAdoptionRulesInlineKeyboard();
        responseRulesTransport.replyMarkup(backToAdoptionRulesInlineKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(responseRulesTransport);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendRulesHouseSetup(Long chatId) {
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

    private void sendRulesHouseSetupForChild(Long chatId) {
        RulesDTO rulesDTO = rulesService.getRulesByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseRulesHouseSetupPuppyKitten = new SendMessage(chatId, rulesDTO.getRulesGHForChildPet());
        InlineKeyboardMarkup backToPetHouseSelectionKeyBoard = InlineKeyboardMarkupHelper.createBackToPetHouseSelectionKeyBoard();
        responseRulesHouseSetupPuppyKitten.replyMarkup(backToPetHouseSelectionKeyBoard);
        SendResponse sendResponse = animalShelterBot.execute(responseRulesHouseSetupPuppyKitten);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendRulesHouseSetupForAdult(Long chatId) {
        RulesDTO rulesDTO = rulesService.getRulesByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseRulesHouseSetup = new SendMessage(chatId, rulesDTO.getRulesGHForAdultPet());
        InlineKeyboardMarkup backToPetHouseSelectionKeyBoard = InlineKeyboardMarkupHelper.createBackToPetHouseSelectionKeyBoard();
        responseRulesHouseSetup.replyMarkup(backToPetHouseSelectionKeyBoard);
        SendResponse sendResponse = animalShelterBot.execute(responseRulesHouseSetup);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendRulesHouseSetupForSpecial(Long chatId) {
        RulesDTO rulesDTO = rulesService.getRulesByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseRulesHouseSetup = new SendMessage(chatId, rulesDTO.getRulesGHForSpecialPet());
        InlineKeyboardMarkup backToPetHouseSelectionKeyBoard = InlineKeyboardMarkupHelper.createBackToPetHouseSelectionKeyBoard();
        responseRulesHouseSetup.replyMarkup(backToPetHouseSelectionKeyBoard);
        SendResponse sendResponse = animalShelterBot.execute(responseRulesHouseSetup);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendCynologist(Long chatId) {
        SendMessage responseCynologist = new SendMessage(chatId, "Выберите что вы хотите узнать:\n" +
                "1. \uD83D\uDC3E Советы кинолога\n" +
                "2. \uD83D\uDCDE Контакты провереных кинологов\n" +
                "⬅ Назад");
        InlineKeyboardMarkup cynologistKeyBoard = InlineKeyboardMarkupHelper.createCynologistKeyBoard();
        responseCynologist.replyMarkup(cynologistKeyBoard);
        SendResponse sendResponse = animalShelterBot.execute(responseCynologist);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendAdviceFromCynologist(Long chatId) {
        RulesDTO rulesDTO = rulesService.getRulesByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseAdviceFromCynologist = new SendMessage(chatId, rulesDTO.getAdviceFromCynologist());
        InlineKeyboardMarkup backToCynologist = InlineKeyboardMarkupHelper.createBackToCynologist();
        responseAdviceFromCynologist.replyMarkup(backToCynologist);
        SendResponse sendResponse = animalShelterBot.execute(responseAdviceFromCynologist);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendListCynologist(Long chatId) {
        RulesDTO rulesDTO = rulesService.getRulesByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseListCynologist = new SendMessage(chatId, rulesDTO.getListCynologist());
        InlineKeyboardMarkup backToCynologist = InlineKeyboardMarkupHelper.createBackToCynologist();
        responseListCynologist.replyMarkup(backToCynologist);
        SendResponse sendResponse = animalShelterBot.execute(responseListCynologist);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendReasonsRefusal(Long chatId) {
        RulesDTO rulesDTO = rulesService.getRulesByShelterType(getShelterTypeByUserChatId(chatId));
        SendMessage responseRulesTransport = new SendMessage(chatId, rulesDTO.getReasonsRefusal());
        InlineKeyboardMarkup backToAdoptionRulesInlineKeyboard = InlineKeyboardMarkupHelper.createBackToAdoptionRulesInlineKeyboard();
        responseRulesTransport.replyMarkup(backToAdoptionRulesInlineKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(responseRulesTransport);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendReportMessage(Long chatId) {
        String reportMessage = "Пожалуйста, пришлите:\n- Фото животного.\n- Рацион животного.\n- Общее самочувствие и привыкание к новому месту.\n- Изменение в поведении: отказ от старых привычек, приобретение новых.";
        sendMessage(chatId, reportMessage);
        ShelterType shelterType = getShelterTypeByUserChatId(chatId);
        reportDataMap.put(chatId, shelterType);
    }
    // Метод сохранения отчета в сервисе

    private void saveReportToService(Long chatId, String text, byte[] photoData) {
        ShelterType shelterType = getShelterTypeByUserChatId(chatId);
//        int userId = getUserIdFromChatId(chatId);
//        Integer adopterId = adopterService.getAdopterIdByUserId(userId);
        // Генерируем уникальное имя файла
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String photoFilename = shelterType + "_" + timestamp + ".jpg";
        // Создаем DTO для сохранения в сервисе
        ReportAnimalDTO reportAnimalDTO = new ReportAnimalDTO();
        reportAnimalDTO.setShelterType(shelterType);
        reportAnimalDTO.setChatId(chatId); // Устанавливаем chatId
//        reportAnimalDTO.setAdopterId(adopterId); // Получаем adopterId в зависимости от chatId
        reportAnimalDTO.setDescription(text);
        reportAnimalDTO.setPhotoFilename(photoFilename); // Устанавливаем имя файла (замените на фактическое имя)
        reportAnimalDTO.setPhotoData(photoData);
        reportAnimalDTO.setDateReport(LocalDate.now()); // Устанавливаем текущую дату
        reportAnimalDTO.setReportStatus(ReportStatus.REPORT_NEW); // Устанавливаем начальный статус

        // Вызываем метод сервиса для сохранения отчета
        // Добавть обработку исключения UserNotFoundException - выводить сообщение пользователю !
        reportService.saveReport(reportAnimalDTO);
    }

    private byte[] getPhotoData(String fileId) {
        GetFile getFile = new GetFile(fileId);
        GetFileResponse file = animalShelterBot.execute(getFile);

        // Получаем ссылку на файл
        String filePath = file.file().fileId();

        // Вызываем метод бота для получения фотографии как массива байтов
        GetFile fileDataRequest = new GetFile(filePath);
        GetFileResponse imgFile = animalShelterBot.execute(fileDataRequest);
        if (imgFile == null) {
            logger.error("imgFile is null");
            return null;
        }
        logger.info("id: {}", fileId);
        logger.info("imgFile: {}", imgFile);
        logger.info("filePath: {}", filePath);
        // Получаем InputStream фотографии и конвертируем в массив байтов
        try (InputStream inputStream = new URL(animalShelterBot.getFullFilePath(imgFile.file())).openStream()) {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            logger.error("Error converting photo data to bytes: {}", e.getMessage());
            return null;
        }
    }

    private int getUserIdFromChatId(Long chatId) {
        GetChat getChatRequest = new GetChat(chatId.toString());
        Chat chat = animalShelterBot.execute(getChatRequest).chat();

        if (chat != null && chat.id() != null) {
            return Math.toIntExact(chat.id());
        } else {
            return Math.toIntExact(-1L);
        }

    }

    private void sendVolunteerMessage(Long chatId, ShelterType chosenShelterType) {
        String userName = getUserNameByChatId(chatId);

        if ("Default Id".equals(userName)) {
            SendMessage sendMessage = new SendMessage(chatId, "Мы не смогли найти ваш Username в телеграме, пожалуйста, свяжитесь с волонтером самостоятельно и опишите ему вашу проблему:\n"
                    + volunteerService.findAvailableVolunteerTelegram(chosenShelterType));
            InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard();
            sendMessage.replyMarkup(inlineKeyboardMarkup);
            SendResponse sendResponse = animalShelterBot.execute(sendMessage);
            logger.info("Message sent status: {}", sendResponse.isOk());
        } else {
            SendMessage sendMessage = new SendMessage(chatId, "Волонтер в пути! Ожидайте!");
            InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard();
            sendMessage.replyMarkup(inlineKeyboardMarkup);
            SendResponse response = animalShelterBot.execute(sendMessage);
            logger.info("Message sent status: {}", response.isOk());

            Collection<Volunteer> volunteers = volunteerService.findVolunteersByShelterType(chosenShelterType);

            for (Volunteer volunteer : volunteers) {
                String volunteerMessage = "Пользователь с id @" + userName + " нуждается в помощи. Свяжитесь с ним через Telegram.";
                sendMessageToVolunteer(volunteer.getChatId(), volunteerMessage);
            }
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

        return "Default Id";
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

    public void setAnimalShelterBot(TelegramBot animalShelterBot) {
        this.animalShelterBot = animalShelterBot;
    }
}