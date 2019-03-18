package org.jumpmind.pos.util.type;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@JsonDeserialize(using = DateMMDDYYYY.DateMMDDYYYYDeserializer.class)
@JsonSerialize(using = DateMMDDYYYY.DateMMDDYYYYSerializer.class)
public final class DateMMDDYYYY {
    private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMddyyyy");

    private final LocalDate theDate;

    public static DateMMDDYYYY fromString(String mmddyyyy) {
        if (null == mmddyyyy || mmddyyyy.trim().length() == 0) {
            return null;
        }

        return new DateMMDDYYYY(mmddyyyy);
    }

    public static String asString(DateMMDDYYYY dateMMDDYYYY) {
        if (dateMMDDYYYY == null) {
            return null;
        }

        return dateMMDDYYYY.toString();
    }

    public DateMMDDYYYY() {
        this(LocalDate.now());
    }

    public DateMMDDYYYY(String mmddyyyy) {
        String stripped = mmddyyyy.replaceAll("[-/]", "");

        this.theDate = LocalDate.parse(stripped, DATE_FORMATTER);
    }

    public DateMMDDYYYY(LocalDate date) {
        this.theDate = date;
    }

    public DateMMDDYYYY(Date date) {
        this.theDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public DateMMDDYYYY(int month, int dayOfMonth, int year) {
        this.theDate = LocalDate.of(year, month, dayOfMonth);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }

        DateMMDDYYYY otherDate = (DateMMDDYYYY) other;
        return this.theDate != null && this.theDate.equals(otherDate.theDate);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.theDate.hashCode();
        return result;
    }

    public LocalDate getLocalDate() {
        return this.theDate;
    }

    public Date getDate() {
        return Date.from(this.theDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public int getMonth() {
        return this.theDate.getMonthValue();
    }

    public int getDayOfMonth() {
        return this.theDate.getDayOfMonth();
    }

    public int getYear() {
        return this.theDate.getYear();
    }

    @Override
    public String toString() {
        if (this.theDate == null) {
            return "undefined";
        }

        return this.theDate.format(DATE_FORMATTER);
    }

    public static class DateMMDDYYYYDeserializer extends StdDeserializer<DateMMDDYYYY> {
        private static final long serialVersionUID = 1L;

        public DateMMDDYYYYDeserializer() {
            super(DateMMDDYYYY.class);
        }

        @Override
        public DateMMDDYYYY deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String strDateMMDDYYYY = p.readValueAs(String.class);
            return DateMMDDYYYY.fromString(strDateMMDDYYYY);
        }
    }
    
    public static class DateMMDDYYYYSerializer extends StdSerializer<DateMMDDYYYY> {
        private static final long serialVersionUID = 1L;

        public DateMMDDYYYYSerializer() {
            super(DateMMDDYYYY.class);
        }
        
        @Override
        public void serialize(DateMMDDYYYY value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeObject(value.toString());
        }

    }
    
}
