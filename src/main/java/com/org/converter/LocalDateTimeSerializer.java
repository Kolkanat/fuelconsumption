package com.org.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.org.constants.AppConstants.DATE_TIME_FORMAT;

public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
    protected LocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.format(DATE_TIME_FORMAT));
    }
}
