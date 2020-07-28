package com.org.utils;

import com.org.model.Fuel;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TestConstants {
    Fuel f1_d_2018_02_12th = Fuel.builder()
            .fuelType("D")
            .date(LocalDate.of(2018,2,12))
            .driverId(120L)
            .price(BigDecimal.valueOf(111.09))
            .volume(BigDecimal.valueOf(15.5))
            .build();

    Fuel f2_98_2018_02_27th = Fuel.builder()
            .fuelType("98")
            .date(LocalDate.of(2018,2,27))
            .driverId(120L)
            .price(BigDecimal.valueOf(100.1))
            .volume(BigDecimal.valueOf(12))
            .build();

    Fuel f3_98_2018_02_9th = Fuel.builder()
            .fuelType("98")
            .date(LocalDate.of(2018,2,9))
            .driverId(130L)
            .price(BigDecimal.valueOf(200.1))
            .volume(BigDecimal.valueOf(12))
            .build();
}
