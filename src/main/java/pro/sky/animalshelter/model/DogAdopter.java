package pro.sky.animalshelter.model;

import pro.sky.animalshelter.dto.AnimalAdopterDTO;

import javax.persistence.*;

@Entity
@Table(name = "dog_adopters")
public class DogAdopter extends Adopter {
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserDogShelter user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "dog_id", referencedColumnName = "dog_id")
    private Dog dog;

    public UserDogShelter getUser() {
        return user;
    }

    public void setUser(UserDogShelter userDogShelter) {
        this.user = userDogShelter;
    }

    @Override
    public Dog getAnimal() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public AnimalAdopterDTO toDTO() {
        AnimalAdopterDTO animalAdopterDTO = super.toDTO();
        animalAdopterDTO.setShelterType(ShelterType.DOG_SHELTER);
        animalAdopterDTO.setAnimalId(this.getAnimal().getDogId());
        animalAdopterDTO.setAnimalName(this.getAnimal().getName());

        return animalAdopterDTO;
    }
}
