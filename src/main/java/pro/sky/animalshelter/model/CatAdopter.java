package pro.sky.animalshelter.model;

import pro.sky.animalshelter.dto.AnimalAdopterDTO;

import javax.persistence.*;

@Entity
@Table(name = "cat_adopters")
public class CatAdopter extends Adopter {
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserCatShelter user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    private Cat cat;

    @Override
    public UserCatShelter getUser() {
        return user;
    }

    public void setUser(UserCatShelter user) {
        this.user = user;
    }

    @Override
    public Cat getAnimal() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public AnimalAdopterDTO toDTO() {
        AnimalAdopterDTO animalAdopterDTO = super.toDTO();
        animalAdopterDTO.setShelterType(ShelterType.CAT_SHELTER);
        animalAdopterDTO.setAnimalId(this.getAnimal().getCatId());
        animalAdopterDTO.setAnimalName(this.getAnimal().getName());

        return animalAdopterDTO;
    }
}
