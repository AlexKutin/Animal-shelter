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
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import pro.sky.animalshelter.Constants.CallbackConstants;
import pro.sky.animalshelter.Constants.TextConstants;
import pro.sky.animalshelter.dto.*;
import pro.sky.animalshelter.exception.UserChatIdNotFoundException;
import pro.sky.animalshelter.keyBoard.InlineKeyboardMarkupHelper;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.service.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
//@ComponentScan(basePackages = {"pro.sky.animalshelter.service", "pro.sky.animalshelter.listener", "pro.sky.animalshelter.repository"})
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
    private final Map<Long, ReportStage> reportStageMap = new HashMap<>();
    private final Map<Long, PhotoDataDTO> photoSizeMap = new HashMap<>();

    @Value("${application.report.photo.folder}")
    private String reportPhotoFolder;

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
            SendMessage response = new SendMessage(chatId, TextConstants.SEND_LEAVE_CONTACTS_SUCCESSFULLY);
            InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard();
            response.replyMarkup(keyboardMarkup);
            SendResponse sendResponse = animalShelterBot.execute(response);
            logger.info("Message sent status: {}", sendResponse.isOk());
        } else if ("/getchatid".equals(text)) {
            getChatId(chatId);
        } else if (reportStageMap.containsKey(chatId)) {
            ReportStage reportStage = reportStageMap.get(chatId);
            if (reportStage == ReportStage.AWAITING_PHOTO) {
                if (message.photo() != null && message.photo().length > 0) {
                    int indexPhoto = message.photo().length - 1;
                    PhotoDataDTO photoDataDTO = savePhotoDataToFile(message.photo()[indexPhoto].fileId(), chatId);

                    photoSizeMap.put(chatId, photoDataDTO);
                    reportStageMap.put(chatId, ReportStage.AWAITING_DESCRIPTION);
                    SendMessage requestTextMessage = new SendMessage(chatId, TextConstants.REPORT_MESSAGE_DESCRIPTION);
                    SendResponse requestTextResponse = animalShelterBot.execute(requestTextMessage);
                } else {
                    SendMessage requestPhotoMessage = new SendMessage(chatId, TextConstants.REPORT_MESSAGE_ERROR);
                    SendResponse requestPhotoResponse = animalShelterBot.execute(requestPhotoMessage);
                }
            } else if (reportStage == ReportStage.AWAITING_DESCRIPTION) {
                PhotoDataDTO photoDataDTO = photoSizeMap.get(chatId);
                ShelterType shelterType = getShelterTypeByUserChatId(chatId);

                if (photoDataDTO != null && shelterType != null) {
                    saveReportToService(chatId, message.text(), photoDataDTO, shelterType);
                    reportStageMap.put(chatId, ReportStage.COMPLETED);
                    SendMessage sendResponse = new SendMessage(chatId, TextConstants.REPORT_MESSAGE_SUCCESSFULLY);
                    SendResponse response = animalShelterBot.execute(sendResponse);
                }
            }
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
            ShelterType chosenShelterType = chooseShelterType.get(chatId);
            sendReportMessage(chatId, chosenShelterType);
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
            sendMessage(chatId, TextConstants.SEND_LEAVE_CONTACTS_CANCEL);
        }
    }

    private void sendStartMessage(Long chatId) {
        SendMessage response = new SendMessage(chatId, TextConstants.START_MESSAGE);
        InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkupHelper.createInlineKeyboard();
        response.replyMarkup(keyboardMarkup);
        SendResponse sendResponse = animalShelterBot.execute(response);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendInvalidMessage(Long chatId) {
        sendMessage(chatId, TextConstants.DEFAULT_MESSAGE);
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
            SendMessage response = new SendMessage(chatId, data + " " + shelterDTO.getShelterName() + TextConstants.SHELTER_INFO_MESSAGE);
            InlineKeyboardMarkup menuKeyboard = InlineKeyboardMarkupHelper.createMainMenuInlineKeyboard();
            response.replyMarkup(menuKeyboard);
            SendResponse sendResponse = animalShelterBot.execute(response);
            logger.info("Message sent status: {}", sendResponse.isOk());
        } else {
            sendMessage(chatId, TextConstants.SHELTER_INFO_ERROR_MESSAGE);
        }
    }

    private void sendShelterInfoOptions(Long chatId) {
        SendMessage responseInfo = new SendMessage(chatId, TextConstants.WELCOME_MESSAGE);
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
        SendMessage responseShelterInfoLeaveContacts = new SendMessage(chatId, TextConstants.SHELTER_LEAVE_CONTACTS);
        InlineKeyboardMarkup keyboardMarkupCancelContactInput = InlineKeyboardMarkupHelper.createCancelContactInputInlineKeyboard();
        responseShelterInfoLeaveContacts.replyMarkup(keyboardMarkupCancelContactInput);
        SendResponse sendResponse = animalShelterBot.execute(responseShelterInfoLeaveContacts);
        logger.info("Message sent status: {}", sendResponse.isOk());
        ShelterType shelterType = getShelterTypeByUserChatId(chatId);
        userContactMap.put(chatId, shelterType);
    }

    private void backMainMenu(Long chatId) {
        SendMessage response = new SendMessage(chatId, TextConstants.BACK_MAIN_MENU);
        InlineKeyboardMarkup menuKeyboard = InlineKeyboardMarkupHelper.createMainMenuInlineKeyboard();
        response.replyMarkup(menuKeyboard);
        SendResponse sendResponse = animalShelterBot.execute(response);
        logger.info("Message sent status: {}", sendResponse.isOk());
    }

    private void sendAdoptionRules(Long chatId) {
        ShelterType shelterType = getShelterTypeByUserChatId(chatId);
        if (shelterType == ShelterType.DOG_SHELTER) {
            SendMessage responseAdoptionRules = new SendMessage(chatId, TextConstants.ADOPTION_RULES_FOR_DOG);
            InlineKeyboardMarkup adoptionRulesKeyboard = InlineKeyboardMarkupHelper.createAdoptionRulesForDogInlineKeyboard();
            responseAdoptionRules.replyMarkup(adoptionRulesKeyboard);
            SendResponse sendResponseAdoptionRules = animalShelterBot.execute(responseAdoptionRules);
            logger.info("Message sent status: {}", sendResponseAdoptionRules.isOk());
        } else if (shelterType == ShelterType.CAT_SHELTER) {
            SendMessage responseAdoptionRules = new SendMessage(chatId, TextConstants.ADOPTION_RULES_FOR_CAT);
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
        SendMessage responseRulesHouseSetup = new SendMessage(chatId, TextConstants.ADOPTION_RULES_HOUSE_SETUP);
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
        SendMessage responseCynologist = new SendMessage(chatId, TextConstants.CYNOLOGIST);
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

    private void sendReportMessage(Long chatId, ShelterType chooseShelterType) {
        if (adopterService.getAdopterIdByChatId(chatId, chooseShelterType) == 0) {
            sendMessage(chatId, TextConstants.SEND_REPORT_MESSAGE_ERROR + volunteerService.findAvailableVolunteerTelegram(chooseShelterType));
        } else {
            sendMessage(chatId, TextConstants.REPORT_MESSAGE);
            reportStageMap.put(chatId, ReportStage.AWAITING_PHOTO);
        }
    }
    // Формирование отчета о животном (создание DTO) для сохранения в сервисе
    private void saveReportToService(Long chatId, String description, PhotoDataDTO photoDataDTO, ShelterType shelterType) {
        // Создаем DTO для сохранения в сервисе
        ReportAnimalDTO reportAnimalDTO = new ReportAnimalDTO();
        reportAnimalDTO.setShelterType(shelterType);
        reportAnimalDTO.setChatId(chatId);
        reportAnimalDTO.setDescription(description);
        reportAnimalDTO.setFilePath(photoDataDTO.getFilePath());
        reportAnimalDTO.setFileSize(photoDataDTO.getFileSize());
        reportAnimalDTO.setMediaType(photoDataDTO.getMediaType());
        reportAnimalDTO.setDateReport(LocalDate.now());
        reportAnimalDTO.setReportStatus(ReportStatus.REPORT_NEW);

        reportService.saveReport(reportAnimalDTO);
    }

    private PhotoDataDTO savePhotoDataToFile(String fileId, long chatId) {
        GetFile getFile = new GetFile(fileId);
        GetFileResponse file = animalShelterBot.execute(getFile);
        String filePath = file.file().fileId();

        String fileName = file.file().filePath();
        long fileSize = file.file().fileSize();

        GetFile fileDataRequest = new GetFile(filePath);
        GetFileResponse imgFile = animalShelterBot.execute(fileDataRequest);
        if (imgFile == null) {
            logger.error("imgFile is null");
            return null;
        }

        String photoExtension = FilenameUtils.getExtension(fileName);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        ShelterType shelterType = getShelterTypeByUserChatId(chatId);
        String photoFilename = shelterType + "_" + chatId + "_" + timestamp;

        Path photoFilePath = Path.of(reportPhotoFolder, photoFilename + "." + photoExtension);

        // Получаем InputStream фотографии и сохраняем в виде файла на сервер
        try (InputStream inputStream = new URL(animalShelterBot.getFullFilePath(imgFile.file())).openStream()) {
            Files.createDirectories(photoFilePath.getParent());
            Files.deleteIfExists(photoFilePath);
            Files.copy(inputStream, photoFilePath, StandardCopyOption.REPLACE_EXISTING);
            PhotoDataDTO photoDataDTO = new PhotoDataDTO();
            photoDataDTO.setFilePath(photoFilePath.toString());
            photoDataDTO.setFileSize(fileSize);
            photoDataDTO.setMediaType(Files.probeContentType(photoFilePath));
            logger.info("Photo file saved: " + photoDataDTO.getFilePath());

            return photoDataDTO;
        } catch (IOException e) {
            logger.error("Error with saving the photo data to file: {}", e.getMessage());
            return null;
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
        SendMessage response = new SendMessage(chatId, TextConstants.MAIN_MENU_MESSAGE);
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