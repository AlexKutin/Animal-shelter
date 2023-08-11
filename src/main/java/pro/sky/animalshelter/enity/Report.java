package pro.sky.animalshelter.enity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Report {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "report_id")
    private Long reportId;
    @ManyToOne
    @JoinColumn (name = "clientId")
    private Client client;
    private LocalDateTime dataTimeReport;
    @Lob
    private byte[] photo;
    @Lob
    private byte[] report;

}