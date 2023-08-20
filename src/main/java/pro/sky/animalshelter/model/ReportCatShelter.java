package pro.sky.animalshelter.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "report_cat_shelter")
public class ReportCatShelter extends Report {

    public ReportCatShelter(Integer idReport, Integer userShelter, String description, String photo, Timestamp dateTimeReport) {
        super(idReport, userShelter, description, photo, dateTimeReport);
    }
}
