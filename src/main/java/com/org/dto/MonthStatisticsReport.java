package com.org.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class MonthStatisticsReport implements Serializable {
    private String fuelType;
    private BigDecimal volume;
    private Double averagePrice;
    private BigDecimal totalPrice;
    @JsonIgnore
    private int year;
    @JsonIgnore
    private int month;
}
