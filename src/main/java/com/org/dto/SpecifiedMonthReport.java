package com.org.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.org.converter.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpecifiedMonthReport implements Serializable {
    private String fuelType;
    private BigDecimal volume;
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private Long driverId;
}
