package fun.timu.cloud.net.data.controller;

import fun.timu.cloud.net.data.service.VisitStatsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/visitStats/v1")
public class VisitStatsController {
    private final VisitStatsService statsService;

    public VisitStatsController(VisitStatsService statsService) {
        this.statsService = statsService;
    }
}
