package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import pro.sky.animalshelter.model.Rules;
import pro.sky.animalshelter.model.Shelter;

@Schema(description = "рекомендации")
public class RulesDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "идентификатор")
    private Integer idRules;

    @Schema(description = "рекомендации знакомству с животным")
    private String rilesMeeting;

    @Schema(description = "документы чтоб забрать животное")
    private String listDocForTakePet;

    @Schema(description = "рекомендации к транспортировке")
    private String rulesTransportation;

    @Schema(description = "условия проживания взрослого животного")
    private String rulesGHForAdultPet;

    @Schema(description = "условия проживания малышей")
    private String rulesGHForChildPet;

    @Schema(description = "условия проживания животного с ограниченными способностями")
    private String rulesGHForSpecialPet;

    @Schema(description = "рекомендации кинологов")
    private String adviceFromCynologist;

    @Schema(description = "список кинологов")
    private String listCynologist;

    @Schema(description = "причины отказа")
    private String reasonsRefusal;

    public Integer getIdRules() {
        return idRules;
    }

    public void setIdRules(Integer idRules) {
        this.idRules = idRules;
    }

    public String getRilesMeeting() {
        return rilesMeeting;
    }

    public void setRilesMeeting(String rilesMeeting) {
        this.rilesMeeting = rilesMeeting;
    }

    public String getListDocForTakePet() {
        return listDocForTakePet;
    }

    public void setListDocForTakePet(String listDocForTakePet) {
        this.listDocForTakePet = listDocForTakePet;
    }

    public String getRulesTransportation() {
        return rulesTransportation;
    }

    public void setRulesTransportation(String rulesTransportation) {
        this.rulesTransportation = rulesTransportation;
    }

    public String getRulesGHForAdultPet() {
        return rulesGHForAdultPet;
    }

    public void setRulesGHForAdultPet(String rulesGHForAdultPet) {
        this.rulesGHForAdultPet = rulesGHForAdultPet;
    }

    public String getRulesGHForChildPet() {
        return rulesGHForChildPet;
    }

    public void setRulesGHForChildPet(String rulesGHForChildPet) {
        this.rulesGHForChildPet = rulesGHForChildPet;
    }

    public String getRulesGHForSpecialPet() {
        return rulesGHForSpecialPet;
    }

    public void setRulesGHForSpecialPet(String rulesGHForSpecialPet) {
        this.rulesGHForSpecialPet = rulesGHForSpecialPet;
    }

    public String getAdviceFromCynologist() {
        return adviceFromCynologist;
    }

    public void setAdviceFromCynologist(String adviceFromCynologist) {
        this.adviceFromCynologist = adviceFromCynologist;
    }

    public String getListCynologist() {
        return listCynologist;
    }

    public void setListCynologist(String listCynologist) {
        this.listCynologist = listCynologist;
    }

    public String getReasonsRefusal() {
        return reasonsRefusal;
    }

    public void setReasonsRefusal(String reasonsRefusal) {
        this.reasonsRefusal = reasonsRefusal;
    }

    public static RulesDTO fromRules(Rules rules) {
        RulesDTO rulesDTO = new RulesDTO();
        rulesDTO.setIdRules(rules.getIdRules());
        rulesDTO.setAdviceFromCynologist(rules.getAdviceFromCynologist());
        rulesDTO.setRulesTransportation(rules.getRulesTransportation());
        rulesDTO.setListCynologist(rules.getListCynologist());
        rulesDTO.setListDocForTakePet(rules.getListDocForTakePet());
        rulesDTO.setRilesMeeting(rules.getRilesMeeting());
        rulesDTO.setRulesGHForAdultPet(rules.getRulesGHForAdultPet());
        rulesDTO.setRulesGHForChildPet(rules.getRulesGHForChildPet());
        rulesDTO.setRulesGHForSpecialPet(rules.getRulesGHForSpecialPet());
        rulesDTO.setReasonsRefusal(rules.getReasonsRefusal());

        return rulesDTO;
    }

    public Rules toRules() {
        Shelter shelter = new Shelter();
        Rules rules = new Rules();
        rules.setIdRules(rules.getIdRules());
        rules.setAdviceFromCynologist(this.getAdviceFromCynologist());
        rules.setRulesTransportation(this.getRulesTransportation());
        rules.setListCynologist(this.getListCynologist());
        rules.setListDocForTakePet(this.getListDocForTakePet());
        rules.setRilesMeeting(this.getRilesMeeting());
        rules.setRulesGHForAdultPet(this.getRulesGHForAdultPet());
        rules.setRulesGHForChildPet(this.getRulesGHForChildPet());
        rules.setRulesGHForSpecialPet(this.getRulesGHForSpecialPet());
        rules.setReasonsRefusal(this.getReasonsRefusal());

        return rules;
    }
}
