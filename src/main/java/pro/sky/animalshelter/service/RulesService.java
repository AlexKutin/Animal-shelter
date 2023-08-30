package pro.sky.animalshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.dto.RulesDTO;
import pro.sky.animalshelter.model.ShelterType;

@Service
public class RulesService {

    private final ShelterService shelterService;

    @Autowired
    public RulesService(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    public RulesDTO getRulesByShelterType(ShelterType shelterType) {
        return RulesDTO.fromRules(shelterService.findShelterByShelterType(shelterType).getRules());
    }

}