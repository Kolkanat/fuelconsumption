package com.org.controllers.errorhandling;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.org.converter.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FuelAppRestError {
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;
}
