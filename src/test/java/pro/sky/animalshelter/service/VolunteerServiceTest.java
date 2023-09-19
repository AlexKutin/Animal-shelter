package pro.sky.animalshelter.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.exception.VolunteerNotFoundException;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.VolunteerRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static pro.sky.animalshelter.Constants.TextConstants.VOLUNTEER_BY_ID_NOT_FOUND_MESSAGE;

@ExtendWith(MockitoExtension.class)
class VolunteerServiceTest {
    public static final Integer DOG_VOLUNTEER_PRESENT_ID = 76;
    public static final Long DOG_VOLUNTEER_PRESENT_CHAT_ID = 812818905L;

    public static final Integer CAT_VOLUNTEER_PRESENT_ID = 61;
    public static final Integer CAT_VOLUNTEER_NOT_PRESENT_ID = 12;
    public static final Long CAT_VOLUNTEER_PRESENT_CHAT_ID = 812818900L;

    private static final String CAT_VOLUNTEER_TG_ADDR = "@Cat_Keeper";
    private static final String DOG_VOLUNTEER_TG_ADDR = "@Dog_Keeper";

    private Volunteer catVolunteer;
    private Volunteer dogVolunteer;

    @Captor
    private ArgumentCaptor<Volunteer> volunteerCaptor;

    @Mock
    private VolunteerRepository volunteerRepository;

    @InjectMocks
    private VolunteerService volunteerService;

    @BeforeEach
    public void init() {
        Shelter catShelter = new Shelter();
        catShelter.setShelterType(ShelterType.CAT_SHELTER);
        catShelter.setId(1);
        catShelter.setShelterName("Cat shelter");
        catShelter.setShelterDescription("Cat shelter description");
        catShelter.setShelterAddress("Cat shelter address");
        catShelter.setShelterContacts("Cat shelter contacts");
        catShelter.setSecurityContacts("Cat shelter security contacts");
        catShelter.setSafetyInfo("Cat safety info");
        catShelter.setDrivingDirection("Cat shelter driven direction");

        Shelter dogShelter = new Shelter();
        dogShelter.setShelterType(ShelterType.DOG_SHELTER);
        dogShelter.setId(2);
        dogShelter.setShelterName("Dog shelter");
        dogShelter.setShelterDescription("Dog shelter description");
        dogShelter.setShelterAddress("Dog address");

        catVolunteer = new Volunteer(CAT_VOLUNTEER_PRESENT_ID, "Mike", true, CAT_VOLUNTEER_PRESENT_CHAT_ID);
        catVolunteer.setTelegramAddress(CAT_VOLUNTEER_TG_ADDR);
        catVolunteer.setShelter(catShelter);

        dogVolunteer = new Volunteer(DOG_VOLUNTEER_PRESENT_ID, "Alex", true, DOG_VOLUNTEER_PRESENT_CHAT_ID);
        dogVolunteer.setTelegramAddress(DOG_VOLUNTEER_TG_ADDR);
        dogVolunteer.setShelter(dogShelter);
    }

    @Test
    public void blockVolunteerById_NotFound() {
        VolunteerNotFoundException thrown = Assertions.assertThrows(
                VolunteerNotFoundException.class, () -> volunteerService.blockVolunteerById(CAT_VOLUNTEER_NOT_PRESENT_ID));
        String message = String.format(VOLUNTEER_BY_ID_NOT_FOUND_MESSAGE,  CAT_VOLUNTEER_NOT_PRESENT_ID);
        assertEquals(thrown.getMessage(), message);
    }

    @Test
    public void blockVolunteerById_Present() {
        when(volunteerRepository.findById(DOG_VOLUNTEER_PRESENT_ID)).thenReturn(Optional.of(dogVolunteer));
        when(volunteerRepository.save(volunteerCaptor.capture())).thenReturn(dogVolunteer);
        volunteerService.blockVolunteerById(DOG_VOLUNTEER_PRESENT_ID);
        Mockito.verify(volunteerRepository, times(1)).findById(DOG_VOLUNTEER_PRESENT_ID);
        Mockito.verify(volunteerRepository, times(1)).save(any(Volunteer.class));
        Mockito.verifyNoMoreInteractions(volunteerRepository);
        assertFalse(volunteerCaptor.getValue().isActive());
    }

    @Test
    public void unlockVolunteerById() {
        catVolunteer.setActive(false);
        when(volunteerRepository.findById(CAT_VOLUNTEER_PRESENT_ID)).thenReturn(Optional.of(catVolunteer));
        when(volunteerRepository.save(volunteerCaptor.capture())).thenReturn(dogVolunteer);
        volunteerService.unlockVolunteerById(CAT_VOLUNTEER_PRESENT_ID);
        Mockito.verify(volunteerRepository, times(1)).findById(CAT_VOLUNTEER_PRESENT_ID);
        Mockito.verify(volunteerRepository, times(1)).save(any(Volunteer.class));
        Mockito.verifyNoMoreInteractions(volunteerRepository);
        assertTrue(volunteerCaptor.getValue().isActive());
    }

    @Test
    public void findVolunteersByShelterType() {
        volunteerService.findVolunteersByShelterType(ShelterType.DOG_SHELTER);
        Mockito.verify(volunteerRepository, times(1)).
                findVolunteersByShelter_ShelterType(ShelterType.DOG_SHELTER);
        Mockito.verifyNoMoreInteractions(volunteerRepository);

        volunteerService.findVolunteersByShelterType(ShelterType.CAT_SHELTER);
        Mockito.verify(volunteerRepository, times(1)).
                findVolunteersByShelter_ShelterType(ShelterType.CAT_SHELTER);
        Mockito.verifyNoMoreInteractions(volunteerRepository);
    }

    @Test
    public void findAvailableVolunteerTelegram() {
        when(volunteerRepository.findVolunteersByShelter_ShelterType(ShelterType.DOG_SHELTER)).
                thenReturn(Collections.singletonList(dogVolunteer));
        String availableVolunteerTelegram = volunteerService.findAvailableVolunteerTelegram(ShelterType.DOG_SHELTER);
        Mockito.verify(volunteerRepository, times(1)).
                findVolunteersByShelter_ShelterType(ShelterType.DOG_SHELTER);
        Mockito.verifyNoMoreInteractions(volunteerRepository);
        assertEquals(availableVolunteerTelegram, DOG_VOLUNTEER_TG_ADDR);

        when(volunteerRepository.findVolunteersByShelter_ShelterType(ShelterType.CAT_SHELTER)).
                thenReturn(Collections.emptyList());
        availableVolunteerTelegram = volunteerService.findAvailableVolunteerTelegram(ShelterType.CAT_SHELTER);
        Mockito.verify(volunteerRepository, times(1)).
                findVolunteersByShelter_ShelterType(ShelterType.CAT_SHELTER);
        Mockito.verifyNoMoreInteractions(volunteerRepository);
        assertNull(availableVolunteerTelegram);
    }

    @Test
    public void findAvailableVolunteerChatId() {
        volunteerService.findAvailableVolunteerChatId(ShelterType.DOG_SHELTER);
        Mockito.verify(volunteerRepository, times(1)).
                findAllByIsActiveTrueAndShelter_ShelterType(ShelterType.DOG_SHELTER);
        Mockito.verifyNoMoreInteractions(volunteerRepository);

        volunteerService.findAvailableVolunteerChatId(ShelterType.CAT_SHELTER);
        Mockito.verify(volunteerRepository, times(1)).
                findAllByIsActiveTrueAndShelter_ShelterType(ShelterType.CAT_SHELTER);
        Mockito.verifyNoMoreInteractions(volunteerRepository);
    }
}