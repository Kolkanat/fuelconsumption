package com.org.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.org.converter.LocalDateConverter;
import com.org.converter.LocalDateDeserializer;
import com.org.converter.LocalDateSerializer;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "fuel")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Fuel implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue
    @EqualsAndHashCode.Exclude
    private Long id;
    @NotNull
    @Column(name = "fuel_type")
    private String fuelType;
    @NotNull
    @Column(name = "price")
    private BigDecimal price;
    @NotNull
    @Column(name = "volume")
    private BigDecimal volume;
    @NotNull
    @Column(name = "date")
    @Convert(converter = LocalDateConverter.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;
    @NotNull
    @Column(name = "driver_id")
    private Long driverId;
}
