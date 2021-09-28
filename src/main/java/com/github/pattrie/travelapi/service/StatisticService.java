package com.github.pattrie.travelapi.service;

import com.github.pattrie.travelapi.model.Statistic;
import com.github.pattrie.travelapi.model.Travel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static java.math.RoundingMode.HALF_UP;

@Service
public class StatisticService {
    public Statistic create(List<Travel> travels) {
        var statistics = new Statistic();
        statistics.setCount(travels.stream().count());
        statistics.setAvg(
                BigDecimal.valueOf(
                                travels.stream()
                                        .mapToDouble(t -> t.getAmount().doubleValue())
                                        .average()
                                        .orElse(0.0))
                        .setScale(2, HALF_UP));
        statistics.setMin(
                BigDecimal.valueOf(
                                travels.stream().mapToDouble(t -> t.getAmount().doubleValue()).min().orElse(0.0))
                        .setScale(2, HALF_UP));
        statistics.setMax(
                BigDecimal.valueOf(
                                travels.stream().mapToDouble(t -> t.getAmount().doubleValue()).max().orElse(0.0))
                        .setScale(2, HALF_UP));
        statistics.setSum(
                BigDecimal.valueOf(travels.stream().mapToDouble(t -> t.getAmount().doubleValue()).sum())
                        .setScale(2, HALF_UP));

        return statistics;
    }
}