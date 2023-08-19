package pro.sky.animalshelter.model;

import pro.sky.animalshelter.dto.CatAdopterListDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cat_adopter_list")
public class CatAdopterList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adoption_id")
    private Integer adoptionId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopter_id")
    private UserCatShelter adopterId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    private Cat catId;
    @Column(name = "adoption_date")
    private LocalDateTime adoptionDate;

    public CatAdopterList(Integer adoptionId, UserCatShelter adopterId, Cat catId, LocalDateTime adoptionDate) {
        this.adoptionId = adoptionId;
        this.adopterId = adopterId;
        this.catId = catId;
        this.adoptionDate = adoptionDate;
    }

    public CatAdopterList() {
    }

    public Integer getAdoptionId() {
        return adoptionId;
    }

    public void setAdoptionId(Integer adoptionId) {
        this.adoptionId = adoptionId;
    }

    public UserCatShelter getAdopterId() {
        return adopterId;
    }

    public void setAdopterId(UserCatShelter adopterId) {
        this.adopterId = adopterId;
    }

    public Cat getCatId() {
        return catId;
    }

    public void setCatId(Cat catId) {
        this.catId = catId;
    }

    public LocalDateTime getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(LocalDateTime adoptionDate) {
        this.adoptionDate = adoptionDate;
    }

    public static CatAdopterList fromDTO(CatAdopterListDTO catAdopterListDTO) {
        return new CatAdopterList(catAdopterListDTO.getAdoptionId(),
                null,
                null,
                catAdopterListDTO.getAdoptionDate());
    }

    public CatAdopterListDTO toDTO() {
        return new CatAdopterListDTO(this.adoptionId, this.adoptionDate);
    }
}
