package org.jumpmind.pos.util.type;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@JsonDeserialize(using = DateYYYYMMDD.DateYYYYMMDDDeserializer.class)
@JsonSerialize(using = DateYYYYMMDD.DateYYYYMMDDSerializer.class)
public class DateYYYYMMDD {
    private final static Logger logger = LoggerFactory.getLogger(DateYYYYMMDD.class);
    private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final LocalDate theDate;

    public static DateYYYYMMDD fromString(String yyyymmdd) {
        if (null == yyyymmdd || yyyymmdd.trim().length() == 0) {
            return null;
        }

        return new DateYYYYMMDD(yyyymmdd);
    }

    public static String asString(DateYYYYMMDD dateYYYYMMDD) {
        if (dateYYYYMMDD == null) {
            return null;
        }

        return dateYYYYMMDD.toString();
    }

    public DateYYYYMMDD() {
        this(LocalDate.now());
    }

    public DateYYYYMMDD(String yyyymmdd) {
        String stripped = yyyymmdd.replaceAll("[-/]", "");

        this.theDate = LocalDate.parse(stripped, DATE_FORMATTER);
    }

    public DateYYYYMMDD(LocalDate date) {
        this.theDate = date;
    }

    public DateYYYYMMDD(Date date) {
        this.theDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public DateYYYYMMDD(int month, int dayOfMonth, int year) {
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

        DateYYYYMMDD otherDate = (DateYYYYMMDD) other;
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
    
    public static class DateYYYYMMDDDeserializer extends StdDeserializer<DateYYYYMMDD> {
        private static final long serialVersionUID = 1L;

        public DateYYYYMMDDDeserializer() {
            super(DateYYYYMMDD.class);
        }

        @Override
        public DateYYYYMMDD deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String strDateYYYYMMDD = p.readValueAs(String.class);
            return DateYYYYMMDD.fromString(strDateYYYYMMDD);
        }
    }
    
    public static class DateYYYYMMDDSerializer extends StdSerializer<DateYYYYMMDD> {
        private static final long serialVersionUID = 1L;

        public DateYYYYMMDDSerializer() {
            super(DateYYYYMMDD.class);
        }
        
        @Override
        public void serialize(DateYYYYMMDD value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeObject(value.toString());
        }

    }
        
}
