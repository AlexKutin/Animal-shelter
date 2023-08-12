package pro.sky.animalshelter.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import pro.sky.animalshelter.dto.ShelterDTO;
import pro.sky.animalshelter.exception.ShelterNotFoundException;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.service.ShelterService;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShelterControllerTest {

    private static final ShelterType CAT_SHELTER_TYPE = ShelterType.CAT_SHELTER;

    @Mock
    ShelterService shelterService;

    @InjectMocks
    ShelterController shelterController;

    @Test
    void editShelterByExistingShelterType() {
        ShelterDTO shelterDTO = new ShelterDTO();
        when(shelterService.editShelter(CAT_SHELTER_TYPE, shelterDTO)).thenReturn(shelterDTO);
        ResponseEntity<ShelterDTO> actual = shelterController.editShelterByShelterType(CAT_SHELTER_TYPE, shelterDTO);

        Mockito.verify(shelterService, Mockito.times(1)).editShelter(CAT_SHELTER_TYPE, shelterDTO);
        Assertions.assertThat(actual).isEqualTo(ResponseEntity.ok(shelterDTO));
    }

    @Test
    void editShelterByNullShelterType() {
        ShelterDTO shelterDTO = new ShelterDTO();
        when(shelterService.editShelter(null, shelterDTO)).thenThrow(ShelterNotFoundException.class);

        ResponseEntity<ShelterDTO> actual = shelterController.editShelterByShelterType(null, shelterDTO);

        Mockito.verify(shelterService, Mockito.times(1)).editShelter(null, shelterDTO);
        Assertions.assertThat(actual).isEqualTo(ResponseEntity.notFound().build());
    }
}