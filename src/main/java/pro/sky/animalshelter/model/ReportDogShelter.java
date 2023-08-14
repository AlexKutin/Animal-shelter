package pro.sky.animalshelter.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "report_dog_shelter")
public class ReportDogShelter extends Report{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserDogShelter userDogShelter;

    public UserDogShelter getUserDogShelter() {
        return userDogShelter;
    }

    public void setUserDogShelter(UserDogShelter userDogShelter) {
        this.userDogShelter = userDogShelter;
    }
}