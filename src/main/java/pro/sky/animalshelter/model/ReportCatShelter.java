package pro.sky.animalshelter.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "report_cat_shelter")
public class ReportCatShelter extends Report {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserCatShelter userCatShelter;

    public UserCatShelter getUserCatShelter() {
        return userCatShelter;
    }

    public void setUserCatShelter(UserCatShelter userCatShelter) {
        this.userCatShelter = userCatShelter;
    }
}

