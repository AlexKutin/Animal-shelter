package pro.sky.animalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.Constants.CallbackConstants;
import pro.sky.animalshelter.Constants.TextConstants;
import pro.sky.animalshelter.dto.RulesDTO;
import pro.sky.animalshelter.dto.ShelterDTO;
import pro.sky.animalshelter.keyBoard.InlineKeyboardMarkupHelper;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.service.RulesService;
import pro.sky.animalshelter.service.ShelterService;
import pro.sky.animalshelter.service.UserShelterService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AnimalShelterUpdatesListenerTest {

    @Mock
    private TelegramBot animalShelterBot;
    @Mock
    ShelterService shelterService;
    @Mock
    RulesService rulesService;
    @Captor
    ArgumentCaptor<SendMessage> captor;

    @InjectMocks
    private AnimalShelterUpdatesListener updatesListener;
    private final Map<Long, ShelterType> userContactMap = new HashMap<>();
    private final Map<Long, ShelterType> chooseShelterType = new HashMap<>();

    @BeforeEach
    public void setUp() {
        updatesListener.setAnimalShelterBot(animalShelterBot);
    }

    @Test
    public void testInit() {
        updatesListener.init();
        verify(animalShelterBot, Mockito.times(1)).setUpdatesListener(updatesListener);
    }

    @Test
    void sendStartMessageTest() {
        var upd = mock(Update.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(upd.message()).thenReturn(message);
        when(message.text()).thenReturn("/start");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);
        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);


        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();

        assertEquals(value.getParameters().get("text"), TextConstants.START_MESSAGE);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createInlineKeyboard());
    }

    @Test
    void sendInvalidMessageTest() {
        var upd = mock(Update.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(upd.message()).thenReturn(message);
        when(message.text()).thenReturn(null);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();


        assertEquals(value.getParameters().get("text"), TextConstants.DEFAULT_MESSAGE);
    }

    @Test
    void sendBackShelterInfoOptionsTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.data()).thenReturn(CallbackConstants.INFO);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();

        assertEquals(value.getParameters().get("chat_id"), 1L);
        assertEquals(value.getParameters().get("text"), TextConstants.WELCOME_MESSAGE);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createShelterInfoInlineKeyboard());
    }

    @Test
    void sendShelterInfoOptionsTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.data()).thenReturn(CallbackConstants.BACK_SHELTER_INFO);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();

        assertEquals(value.getParameters().get("chat_id"), 1L);
        assertEquals(value.getParameters().get("text"), TextConstants.WELCOME_MESSAGE);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createShelterInfoInlineKeyboard());
    }

    @Test
    void handleShelterInfoMessageTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_CAT);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);
        chooseShelterType.put(1L, ShelterType.CAT_SHELTER);

        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), CallbackConstants.SHELTER_CAT + " " + TextConstants.SHELTER_INFO_MESSAGE);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createMainMenuInlineKeyboard());
        assertEquals(chooseShelterType.get(1L), ShelterType.CAT_SHELTER);
    }

    @Test
    void sendShelterInfoAboutTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        var responseMock = mock(SendResponse.class); // добавил мок ответа, так как в метод у него вызывается метод isOk()
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        ShelterDTO dto = new ShelterDTO(); // добавил ДТО, и замокал сервис, чтобы он возвращал ДТО при вызове метода getShelterByShelterType
        // мок у тебя тут до этого был, но ты создал его внутри этого метода, он был как локальная переменная.
        dto.setShelterName("test_name");
        dto.setShelterDescription("test_description");

        when(shelterService.getShelterByShelterType(any())).thenReturn(dto);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_CAT);

        updatesListener.process(List.of(upd));

        when(callback.data()).thenReturn(CallbackConstants.SHELTER_INFO_ABOUT);
        updatesListener.process(List.of(upd));

        verify(animalShelterBot, times(2)).execute(captor.capture());

        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), "Приют называется " + dto.getShelterName() + ". " + dto.getShelterDescription());
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard());
    }

    @Test
    void sendShelterInfoAddressTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);

        when(animalShelterBot.execute(any(SendPhoto.class))).thenReturn(responseMock);

        when(animalShelterBot.execute(any(SendMessage.class))).thenReturn(responseMock);

        ShelterDTO dto = new ShelterDTO();
        dto.setShelterAddress("test_address");
        dto.setShelterName("Nice Cat");

        when(shelterService.getShelterByShelterType(any())).thenReturn(dto);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_CAT);

        updatesListener.process(List.of(upd));

        when(callback.data()).thenReturn(CallbackConstants.SHELTER_INFO_ADDRESS);
        updatesListener.process(List.of(upd));

        verify(animalShelterBot, times(3)).execute(captor.capture());

        verify(animalShelterBot, times(3)).execute(captor.capture());

        var valuePhoto = captor.getValue();
        assertNotNull(valuePhoto);
        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), dto.getShelterAddress() + "\n" + TextConstants.SHELTER_INFO_ADDRESS);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard());
    }

    @Test
    void sendShelterContactsTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        ShelterDTO dto = new ShelterDTO();
        dto.setShelterName("test_name");
        dto.setShelterDescription("test_description");

        when(shelterService.getShelterByShelterType(any())).thenReturn(dto);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_CAT);

        updatesListener.process(List.of(upd));

        when(callback.data()).thenReturn(CallbackConstants.SHELTER_INFO_CONTACTS);
        updatesListener.process(List.of(upd));

        verify(animalShelterBot, times(2)).execute(captor.capture());

        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), TextConstants.SHELTER_INFO_CONTACTS + dto.getShelterContacts());
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard());
    }

    @Test
    void sendShelterSecurityContactsTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        ShelterDTO dto = new ShelterDTO();
        dto.setShelterName("test_name");
        dto.setShelterDescription("test_description");

        when(shelterService.getShelterByShelterType(any())).thenReturn(dto);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_CAT);

        updatesListener.process(List.of(upd));

        when(callback.data()).thenReturn(CallbackConstants.SHELTER_INFO_SECURITY_CONTACTS);
        updatesListener.process(List.of(upd));

        verify(animalShelterBot, times(2)).execute(captor.capture());

        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), TextConstants.SHELTER_INFO_SECURITY_CONTACTS + dto.getSecurityContacts());
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard());
    }

    @Test
    void sendShelterSafetyTipsTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        ShelterDTO dto = new ShelterDTO();
        dto.setShelterName("test_name");
        dto.setShelterDescription("test_description");

        when(shelterService.getShelterByShelterType(any())).thenReturn(dto);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_CAT);

        updatesListener.process(List.of(upd));

        when(callback.data()).thenReturn(CallbackConstants.SHELTER_INFO_SAFETY_TIPS);
        updatesListener.process(List.of(upd));

        verify(animalShelterBot, times(2)).execute(captor.capture());

        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), dto.getSafetyInfo());
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard());
    }

    @Test
    void sendShelterLeaveContactsTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_INFO_LEAVE_CONTACTS);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);
        updatesListener.chooseShelterType.put(1L, ShelterType.CAT_SHELTER);
        updatesListener.userContactMap.put(1L, ShelterType.CAT_SHELTER);
        updatesListener.process(List.of(upd));


        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();

        assertEquals(value.getParameters().get("chat_id"), 1L);
        assertEquals(value.getParameters().get("text"), TextConstants.SHELTER_LEAVE_CONTACTS);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createCancelContactInputInlineKeyboard());
        assertEquals(updatesListener.userContactMap.get(1L),ShelterType.CAT_SHELTER );
    }

    @Test
    void backMainMenuTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.data()).thenReturn(CallbackConstants.BACK_MAIN_MENU);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();

        assertEquals(value.getParameters().get("chat_id"), 1L);
        assertEquals(value.getParameters().get("text"), TextConstants.BACK_MAIN_MENU);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createMainMenuInlineKeyboard());
    }

    @Test
    void sendAdoptionRulesTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        ShelterDTO dto = new ShelterDTO();
        dto.setShelterName("test_name");
        dto.setShelterDescription("test_description");

        when(shelterService.getShelterByShelterType(any())).thenReturn(dto);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_CAT);

        updatesListener.process(List.of(upd));

        when(callback.data()).thenReturn(CallbackConstants.ADOPTIONS_RULES);
        updatesListener.process(List.of(upd));

        verify(animalShelterBot, times(2)).execute(captor.capture());

        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), TextConstants.ADOPTION_RULES_FOR_CAT);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createAdoptionRulesInlineKeyboard());
    }

    @Test
    void sendBackAdoptionRulesTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        ShelterDTO dto = new ShelterDTO();
        dto.setShelterName("test_name");
        dto.setShelterDescription("test_description");

        when(shelterService.getShelterByShelterType(any())).thenReturn(dto);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_CAT);

        updatesListener.process(List.of(upd));

        when(callback.data()).thenReturn(CallbackConstants.BACK_ADOPTION_RULES);
        updatesListener.process(List.of(upd));

        verify(animalShelterBot, times(2)).execute(captor.capture());

        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), TextConstants.ADOPTION_RULES_FOR_CAT);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createAdoptionRulesInlineKeyboard());
    }


    @Test
    void sendAdoptionRulesIntroductionTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        RulesDTO dto = new RulesDTO();
        dto.setRilesMeeting("test_rules_meeting");

        when(rulesService.getRulesByShelterType(any())).thenReturn(dto);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_CAT);

        updatesListener.process(List.of(upd));

        when(callback.data()).thenReturn(CallbackConstants.ADOPTIONS_RULES_INTRODUCTION);
        updatesListener.process(List.of(upd));

        verify(animalShelterBot, times(2)).execute(captor.capture());

        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), dto.getRilesMeeting());
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createBackToAdoptionRulesInlineKeyboard());
    }

    @Test
    void sendAdoptionRulesDogTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        ShelterDTO dto = new ShelterDTO();
        dto.setShelterName("test_name");
        dto.setShelterDescription("test_description");

        when(shelterService.getShelterByShelterType(any())).thenReturn(dto);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_DOG);

        updatesListener.process(List.of(upd));

        when(callback.data()).thenReturn(CallbackConstants.ADOPTIONS_RULES);
        updatesListener.process(List.of(upd));

        verify(animalShelterBot, times(2)).execute(captor.capture());

        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), TextConstants.ADOPTION_RULES_FOR_DOG);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createAdoptionRulesForDogInlineKeyboard());
    }

    @Test
    void sendListDocForTakePetTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        RulesDTO rulesDTO = new RulesDTO();
        rulesDTO.setListDocForTakePet("test_list");

        when(rulesService.getRulesByShelterType(any())).thenReturn(rulesDTO);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_CAT);

        updatesListener.process(List.of(upd));

        when(callback.data()).thenReturn(CallbackConstants.ADOPTIONS_RULES_DOCUMENTS);
        updatesListener.process(List.of(upd));

        verify(animalShelterBot, times(2)).execute(captor.capture());

        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), rulesDTO.getListDocForTakePet());
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createBackToAdoptionRulesInlineKeyboard());
    }

    @Test
    void sendRulesTransportTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        RulesDTO rulesDTO = new RulesDTO();
        rulesDTO.setRulesTransportation("test_transport");

        when(rulesService.getRulesByShelterType(any())).thenReturn(rulesDTO);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_CAT);

        updatesListener.process(List.of(upd));

        when(callback.data()).thenReturn(CallbackConstants.ADOPTIONS_RULES_TRANSPORT);
        updatesListener.process(List.of(upd));

        verify(animalShelterBot, times(2)).execute(captor.capture());

        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), rulesDTO.getRulesTransportation());
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createBackToAdoptionRulesInlineKeyboard());
    }

    @Test
    void sendRulesHouseTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.data()).thenReturn(CallbackConstants.ADOPTIONS_RULES_HOUSE_SETUP);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();

        assertEquals(value.getParameters().get("chat_id"), 1L);
        assertEquals(value.getParameters().get("text"), TextConstants.ADOPTION_RULES_HOUSE_SETUP);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createPetHouseSelectionKeyBoard());
    }

    @Test
    void backToSendRulesHouseTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.data()).thenReturn(CallbackConstants.BACK_TO_HOUSE_SELECTION);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();

        assertEquals(value.getParameters().get("chat_id"), 1L);
        assertEquals(value.getParameters().get("text"), TextConstants.ADOPTION_RULES_HOUSE_SETUP);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createPetHouseSelectionKeyBoard());
    }

    @Test
    void sendRulesHouseSetupForChildTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        RulesDTO rulesDTO = new RulesDTO();
        rulesDTO.setRulesGHForChildPet("test_house");

        when(rulesService.getRulesByShelterType(any())).thenReturn(rulesDTO);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_CAT);

        updatesListener.process(List.of(upd));

        when(callback.data()).thenReturn(CallbackConstants.ADOPTIONS_RULES_HOUSE_SETUP_PUPPY_KITTEN);
        updatesListener.process(List.of(upd));

        verify(animalShelterBot, times(2)).execute(captor.capture());

        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), rulesDTO.getRulesGHForChildPet());
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createBackToPetHouseSelectionKeyBoard());
    }

    @Test
    void sendRulesHouseSetupForAdultTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        RulesDTO rulesDTO = new RulesDTO();
        rulesDTO.setRulesGHForAdultPet("test_house");

        when(rulesService.getRulesByShelterType(any())).thenReturn(rulesDTO);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_CAT);

        updatesListener.process(List.of(upd));

        when(callback.data()).thenReturn(CallbackConstants.ADOPTIONS_RULES_HOUSE_SETUP_ADULT);
        updatesListener.process(List.of(upd));

        verify(animalShelterBot, times(2)).execute(captor.capture());

        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), rulesDTO.getRulesGHForAdultPet());
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createBackToPetHouseSelectionKeyBoard());
    }

    @Test
    void sendRulesHouseSetupForSpecialTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        RulesDTO rulesDTO = new RulesDTO();
        rulesDTO.setRulesGHForSpecialPet("test_house");

        when(rulesService.getRulesByShelterType(any())).thenReturn(rulesDTO);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_CAT);

        updatesListener.process(List.of(upd));

        when(callback.data()).thenReturn(CallbackConstants.ADOPTIONS_RULES_HOUSE_SETUP_SPECIAL_NEEDS);
        updatesListener.process(List.of(upd));

        verify(animalShelterBot, times(2)).execute(captor.capture());

        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), rulesDTO.getRulesGHForSpecialPet());
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createBackToPetHouseSelectionKeyBoard());
    }

    @Test
    void sendCynologistTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.data()).thenReturn(CallbackConstants.CYNOLOGIST);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();

        assertEquals(value.getParameters().get("chat_id"), 1L);
        assertEquals(value.getParameters().get("text"), TextConstants.CYNOLOGIST);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createCynologistKeyBoard());
    }

    @Test
    void sendBackCynologistTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.data()).thenReturn(CallbackConstants.BACK_TO_CYNOLOGIST);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();

        assertEquals(value.getParameters().get("chat_id"), 1L);
        assertEquals(value.getParameters().get("text"), TextConstants.CYNOLOGIST);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createCynologistKeyBoard());
    }

    @Test
    void sendAdviceFromCynologistTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        RulesDTO rulesDTO = new RulesDTO();
        rulesDTO.setAdviceFromCynologist("test_advice");

        when(rulesService.getRulesByShelterType(any())).thenReturn(rulesDTO);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_CAT);

        updatesListener.process(List.of(upd));

        when(callback.data()).thenReturn(CallbackConstants.ADVICE_FROM_CYNOLOGIST);
        updatesListener.process(List.of(upd));

        verify(animalShelterBot, times(2)).execute(captor.capture());

        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), rulesDTO.getAdviceFromCynologist());
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createBackToCynologist());
    }

    @Test
    void sendReasonsRefusalTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        RulesDTO rulesDTO = new RulesDTO();
        rulesDTO.setReasonsRefusal("test_reasons");

        when(rulesService.getRulesByShelterType(any())).thenReturn(rulesDTO);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_CAT);

        updatesListener.process(List.of(upd));

        when(callback.data()).thenReturn(CallbackConstants.ADOPTION_RULES_REJECTION_REASONS);
        updatesListener.process(List.of(upd));

        verify(animalShelterBot, times(2)).execute(captor.capture());

        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), rulesDTO.getReasonsRefusal());
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createBackToAdoptionRulesInlineKeyboard());
    }

    @Test
    void sendListCynologistTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        RulesDTO rulesDTO = new RulesDTO();
        rulesDTO.setListCynologist("test_list");

        when(rulesService.getRulesByShelterType(any())).thenReturn(rulesDTO);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_CAT);

        updatesListener.process(List.of(upd));

        when(callback.data()).thenReturn(CallbackConstants.LIST_CYNOLOGIST);
        updatesListener.process(List.of(upd));

        verify(animalShelterBot, times(2)).execute(captor.capture());

        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), rulesDTO.getListCynologist());
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createBackToCynologist());
    }

    @Test
    void sendMainMenuMessage() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.data()).thenReturn(CallbackConstants.BACK_SHELTER_MENU);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);

        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();

        assertEquals(value.getParameters().get("chat_id"), 1L);
        assertEquals(value.getParameters().get("text"), TextConstants.MAIN_MENU_MESSAGE);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createInlineKeyboard());
    }

    @Test
    void getChatIdTest() {
        var upd = mock(Update.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(upd.message()).thenReturn(message);
        when(message.text()).thenReturn("/getchatid");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);

        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();

        assertEquals(value.getParameters().get("text"), "Ваш ChatId: " + chat.id());
    }

    @Test
    void cancelContactInputTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.data()).thenReturn(CallbackConstants.CANCEL_CONTACT_INPUT);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);

        var responseMock = mock(SendResponse.class);
        when(responseMock.isOk()).thenReturn(true);
        when(animalShelterBot.execute(any())).thenReturn(responseMock);
        updatesListener.userContactMap.remove(1L);
        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();

        assertEquals(value.getParameters().get("chat_id"), 1L);
        assertEquals(value.getParameters().get("text"), TextConstants.SEND_LEAVE_CONTACTS_CANCEL);
    }
}