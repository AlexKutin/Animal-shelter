package pro.sky.animalshelter.controller;

import org.springframework.web.bind.annotation.RestController;
import pro.sky.animalshelter.service.VolunteerService;

@RestController
public class VolunteerController {
    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }
}
