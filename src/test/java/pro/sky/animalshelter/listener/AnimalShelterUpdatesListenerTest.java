package pro.sky.animalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class AnimalShelterUpdatesListenerTest {

    @Mock
    private TelegramBot animalShelterBot;

    @InjectMocks
    private AnimalShelterUpdatesListener updatesListener;

    @BeforeEach
    public void setUp() {
        updatesListener.setAnimalShelterBot(animalShelterBot);
    }

    @Test
    public void testInit() {
        updatesListener.init();
        Mockito.verify(animalShelterBot, Mockito.times(1)).setUpdatesListener(updatesListener);
    }
//    @Test
//    public void testProcess() {
//        Update mockUpdate = Mockito.mock(Update.class);
//        List<Update> mockUpdates = Collections.singletonList(mockUpdate);
//
//        updatesListener.process(mockUpdates);
//
//        Mockito.verify(animalShelterBot).execute(Mockito.any());
//    }

}
