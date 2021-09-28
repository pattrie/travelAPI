package com.github.pattrie.travelapi.model;

import com.github.pattrie.travelapi.enumaration.TravelTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Travel {
    private Integer id;
    private String orderNumber;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal amount;
    private TravelTypeEnum type;

    public Travel(TravelTypeEnum type) {
        this.type = type;
    }
}