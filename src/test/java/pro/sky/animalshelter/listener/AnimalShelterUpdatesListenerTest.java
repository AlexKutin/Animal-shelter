package pro.sky.animalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.Constants.CallbackConstants;
import pro.sky.animalshelter.Constants.TextConstants;
import pro.sky.animalshelter.keyBoard.InlineKeyboardMarkupHelper;
import pro.sky.animalshelter.service.ShelterService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AnimalShelterUpdatesListenerTest {

    @Mock
    private TelegramBot animalShelterBot;
    @Mock
    ShelterService shelterService;

    @Captor
    ArgumentCaptor<SendMessage> captor;

    @InjectMocks
    private AnimalShelterUpdatesListener updatesListener;

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


        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();


        assertEquals(value.getParameters().get("text"), TextConstants.DEFAULT_MESSAGE);
    }

    @Test
    void sendShelterInfoOptionsTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.data()).thenReturn(CallbackConstants.INFO);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);

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

        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();
        assertEquals(value.getParameters().get("text"), CallbackConstants.SHELTER_CAT + " " + TextConstants.SHELTER_INFO_MESSAGE);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createMainMenuInlineKeyboard());
    }
//    @Test
//    void sendShelterInfoAboutTest() {
//        var upd = mock(Update.class);
//        var callback = mock(CallbackQuery.class);
//        var message = mock(Message.class);
//        var chat = mock(Chat.class);
//        var shelterService = mock(ShelterService.class);
//        when(chat.id()).thenReturn(1L);
//        when(upd.callbackQuery()).thenReturn(callback);
//        when(callback.data()).thenReturn(CallbackConstants.SHELTER_CAT);
//        verify(updatesListener.chooseShelterType.put(1L, ShelterType.CAT_SHELTER));
//        updatesListener.process(List.of(upd));
//        when(callback.data()).thenReturn(CallbackConstants.SHELTER_INFO_ABOUT);
//        when(callback.message()).thenReturn(message);
//        when(message.chat()).thenReturn(chat);
//        updatesListener.process(List.of(upd));
//
//        verify(animalShelterBot).execute(captor.capture());
//
//        var value = captor.getValue();
////        ShelterDTO shelterDTO = shelterService.getShelterByShelterType(updatesListener.getShelterTypeByUserChatId(826249875L));
////        assertEquals(value.getParameters().get("chat_id"), 826249875L);
//        assertEquals(value.getParameters().get("text"), "Приют называется " + shelterService.getCatShelter().getShelterName() + ". " + shelterService.getCatShelter().getShelterDescription());
//        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard());
//    }
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

    updatesListener.process(List.of(upd));

    verify(animalShelterBot).execute(captor.capture());

    var value = captor.getValue();

    assertEquals(value.getParameters().get("chat_id"), 1L);
    assertEquals(value.getParameters().get("text"), TextConstants.SHELTER_LEAVE_CONTACTS);
    assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createCancelContactInputInlineKeyboard());
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

        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();

        assertEquals(value.getParameters().get("chat_id"), 1L);
        assertEquals(value.getParameters().get("text"), TextConstants.BACK_MAIN_MENU);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createMainMenuInlineKeyboard());
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

        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();

        assertEquals(value.getParameters().get("chat_id"), 1L);
        assertEquals(value.getParameters().get("text"), TextConstants.ADOPTION_RULES_HOUSE_SETUP);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createPetHouseSelectionKeyBoard());
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

        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();

        assertEquals(value.getParameters().get("chat_id"), 1L);
        assertEquals(value.getParameters().get("text"), TextConstants.CYNOLOGIST);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createCynologistKeyBoard());
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

        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();

        assertEquals(value.getParameters().get("chat_id"), 1L);
        assertEquals(value.getParameters().get("text"), TextConstants.MAIN_MENU_MESSAGE);
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createInlineKeyboard());
    }
}