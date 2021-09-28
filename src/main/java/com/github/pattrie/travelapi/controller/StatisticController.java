package com.github.pattrie.travelapi.controller;

import com.github.pattrie.travelapi.model.Statistic;
import com.github.pattrie.travelapi.model.Travel;
import com.github.pattrie.travelapi.service.StatisticService;
import com.github.pattrie.travelapi.service.TravelService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api-travel/v1/statistic")
public class StatisticController {
    private TravelService travelService;
    private StatisticService statisticService;

    public StatisticController(TravelService travelService, StatisticService statisticService) {
        this.travelService = travelService;
        this.statisticService = statisticService;
    }

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<Statistic> getStatistics() {
        List<Travel> travels = travelService.find();
        Statistic statistics = statisticService.create(travels);
        log.info(statistics);
        return ResponseEntity.ok(statistics);
    }
}
