package pro.sky.animalshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.exception.ShelterNotFoundException;
import pro.sky.animalshelter.model.Rules;
import pro.sky.animalshelter.dto.RulesDTO;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.repository.RulesRepository;

@Service
public class RulesService {

    private final RulesRepository rulesRepository;
    private final ShelterService shelterService;

    @Autowired
    public RulesService(RulesRepository rulesRepository, ShelterService shelterService) {
        this.rulesRepository = rulesRepository;
        this.shelterService = shelterService;
    }

    public RulesDTO getAllRules() {
        Rules rules = rulesRepository.findAll().stream().findFirst().orElseThrow(() -> new ShelterNotFoundException("Rules not found in database"));
        return RulesDTO.fromRules(rules);
    }

    public RulesDTO getRulesByShelterType(ShelterType shelterType) {
        return RulesDTO.fromRules(shelterService.findShelterByShelterType(shelterType).getRules());
    }

//
//    public Rules createOrUpdateRules(Rules rules) {
//        return rulesRepository.save(rules);
//    }
//
//    public void deleteRulesById(Integer id) {
//        rulesRepository.deleteById(id);
//    }
}