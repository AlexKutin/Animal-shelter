package pro.sky.animalshelter.model;

import javax.persistence.*;

@Entity
@Table(name = "rules")
public class Rules {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rules")
    private Integer idRules;

    @Column(name = "riles_meeting", nullable = false, length = 1024)
    private String rilesMeeting;

    @Column(name = "list_doc_for_take_pet", nullable = false, length = 1024)
    private String listDocForTakePet;

    @Column(name = "rules_transportation", nullable = false, length = 1024)
    private String rulesTransportation;

    @Column(name = "rules_gh_for_adult_pet", nullable = false, length = 1024)
    private String rulesGHForAdultPet;

    @Column(name = "rules_gh_for_child_pet", nullable = false, length = 1024)
    private String rulesGHForChildPet;

    @Column(name = "rules_gh_for_special_pet", nullable = false, length = 1024)
    private String rulesGHForSpecialPet;

    @Column(name = "advice_from_cynologist", length = 1024)
    private String adviceFromCynologist;

    @Column(name = "list_cynologist", length = 1024)
    private String listCynologist;

    @Column(name = "reasons_refusal", nullable = false, length = 1024)
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

    @Override
    public String toString() {
        return "Rules{" +
                "idRules=" + idRules +
                ", rilesMeeting='" + rilesMeeting + '\'' +
                ", listDocForTakePet='" + listDocForTakePet + '\'' +
                ", rulesTransportation='" + rulesTransportation + '\'' +
                ", rulesGHForAdultPet='" + rulesGHForAdultPet + '\'' +
                ", rulesGHForChildPet='" + rulesGHForChildPet + '\'' +
                ", rulesGHForSpecialPet='" + rulesGHForSpecialPet + '\'' +
                ", adviceFromCynologist='" + adviceFromCynologist + '\'' +
                ", listCynologist='" + listCynologist + '\'' +
                ", reasonsRefusal='" + reasonsRefusal + '\'' +
                '}';
    }
}
