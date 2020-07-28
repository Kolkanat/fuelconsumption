package com.org.constants;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.org.model.Fuel;

import java.time.format.DateTimeFormatter;

public interface AppConstants {
    DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("MM.dd.yyyy HH:mm:ss");
    DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM.dd.yyyy");
    String NULL_CHECK_MSG = "Null value on row: %s and column: %s";
    String FILE_EXTENSION = "csv";
    char CSV_FILE_SEPARATOR = '\t';
    int MAX_FILE_SIZE_IN_KILOBYTES = 1;

    /* DO NOT create schema,csvMapper,objectReader as a bean, will interrupt with spring bean*/
    /* building schema for csv file with headers and with defined separator */
    CsvSchema schema = CsvSchema
            .builder()
            .setUseHeader(true)
            .setColumnSeparator(CSV_FILE_SEPARATOR)
            .build();
    CsvMapper csvMapper = new CsvMapper();
    ObjectReader objectReader = csvMapper.readerFor(Fuel.class).with(schema);
}
