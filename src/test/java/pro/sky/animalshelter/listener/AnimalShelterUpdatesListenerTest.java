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
import pro.sky.animalshelter.dto.ShelterDTO;
import pro.sky.animalshelter.keyBoard.InlineKeyboardMarkupHelper;
import pro.sky.animalshelter.model.ShelterType;
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

        assertEquals(value.getParameters().get("chat_id"), 1L); // проверили, что чат айди поставился верно
        assertEquals(value.getParameters().get("text"), TextConstants.WELCOME_MESSAGE); // проверили, что текст верный, однако сейчас текст записан строкой прям в коде, поэтому имеет смысл вынести все тексты сообщений в отдельные класс констант, в тестах будет проще
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createShelterInfoInlineKeyboard()); // проверили, что клавиатура поставилась верно
    }
    @Test
    void sendShelterInfoAboutTest() {
        var upd = mock(Update.class);
        var callback = mock(CallbackQuery.class);
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(upd.callbackQuery()).thenReturn(callback);
        when(callback.data()).thenReturn(CallbackConstants.SHELTER_INFO_ABOUT);
        when(callback.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        updatesListener.process(List.of(upd));

        verify(animalShelterBot).execute(captor.capture());

        var value = captor.getValue();
        ShelterDTO shelterDTO = shelterService.getShelterByShelterType(updatesListener.getShelterTypeByUserChatId(chat.id()));
        assertEquals(value.getParameters().get("chat_id"), 1L);
        assertEquals(value.getParameters().get("text"), "Приют называется " + shelterDTO.getShelterName() + ". " + shelterDTO.getShelterDescription());
        assertEquals(value.getParameters().get("reply_markup"), InlineKeyboardMarkupHelper.createBackToShelterInfoInlineKeyboard());
    }
}