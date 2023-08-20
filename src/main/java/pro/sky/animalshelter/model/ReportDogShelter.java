package pro.sky.animalshelter.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "report_dog_shelter")
public class ReportDogShelter extends Report{

    public ReportDogShelter(Integer idReport, Integer userShelter, String description, String photo, Timestamp dateTimeReport) {
        super(idReport, userShelter, description, photo, dateTimeReport);
    }
}