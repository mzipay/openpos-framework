package org.jumpmind.pos.util.type;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
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

@JsonDeserialize(using = DateMMYY.DateMMYYDeserializer.class)
@JsonSerialize(using = DateMMYY.DateMMYYSerializer.class)
public class DateMMYY {
    private final static Logger logger = LoggerFactory.getLogger(DateMMYY.class);
    protected final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMyy");
    /**
     * Certain cards have this value for the expiration date.
     */
    public final static String NON_DATE = "0000";

    private final int year;
    private final int month;

    public static DateMMYY fromString(String mmyy) {
        if (isInvalidDateString(mmyy)) {
            logger.trace("Automatically converted invalid DateMMYY date string  value of '{}' to null", mmyy);
            return null;
        }

        return new DateMMYY(mmyy);
    }

    public static String asString(DateMMYY dateMMYY) {
        if (dateMMYY == null) {
            return null;
        }

        return dateMMYY.toString();
    }

    public static boolean isInvalidDateString(String mmyy) {
        return null == mmyy || (mmyy.trim().length() != 4 && mmyy.trim().length() != 5) || NON_DATE.equals(mmyy);
    }

    public DateMMYY() {
        this(LocalDate.now());
    }

    public DateMMYY(String mmyy) {
        if (isInvalidDateString(mmyy)) {
            throw new DateTimeParseException("mmyy date is null or empty or a non-date", mmyy, 0);
        }

        String strippedMmyy = mmyy.replaceAll("[-/]", "");
        
        try {
            int mo = Integer.parseInt(strippedMmyy.substring(0, 2));
            if (mo < 1 || mo > 12) {
                throw new DateTimeParseException("Month must be between 1 and 12. Month given: " + mo, mmyy, 0);
            }
            this.month = mo;
        } catch (DateTimeParseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DateTimeParseException("Failed to parse month in date", mmyy, 0, ex);
        }

        try {
            this.year = Integer.parseInt(strippedMmyy.substring(2, 4));
        } catch (Exception ex) {
            throw new DateTimeParseException("Failed to parse year in date", mmyy, 2, ex);
        }
    }

    public DateMMYY(LocalDate date) {
        this.month = date.getMonthValue();
        int localYear = date.getYear();
        this.year = ((((int) localYear / 10) % 10) * 10) + (localYear % 10);
    }

    public DateMMYY(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        this.month = cal.get(Calendar.MONTH) + 1;
        int localYear = cal.get(Calendar.YEAR);
        this.year = ((((int) localYear / 10) % 10) * 10) + (localYear % 10);
    }

    public DateMMYY(int month, int year) {
        this.month = month;
        int yearLen = (year + "").length();
        if (yearLen == 2) {
            this.year = year;
        } else if (yearLen == 4) {
            this.year = ((((int) year / 10) % 10) * 10) + (year % 10);
        } else {
            throw new RuntimeException("Given year must be either 2 or 4 digits in size. Year provided: " + year);
        }
    }

    public LocalDate getLocalDate() {
        return LocalDate.of(this.year, this.month, 1);
    }

    public Date getDate() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(this.year, this.month - 1, 1);
        return cal.getTime();
    }

    public int getMonth() {
        return this.month;
    }

    public int getYear() {
        return this.year;
    }

    @Override
    public String toString() {
        return this.getLocalDate().format(DATE_FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }

        DateMMYY otherDate = (DateMMYY) other;
        return this.year == otherDate.year && this.month == otherDate.month;
    }

    @Override
    public int hashCode() {
        int result = 13;
        result = 29 * result + this.year;
        result = 29 * result + this.month;
        return result;
    }

    public static class DateMMYYDeserializer extends StdDeserializer<DateMMYY> {
        private static final long serialVersionUID = 1L;

        public DateMMYYDeserializer() {
            super(DateMMYY.class);
        }

        @Override
        public DateMMYY deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String strDateMMYY = p.readValueAs(String.class);
            return DateMMYY.fromString(strDateMMYY);
        }
    }
    
    public static class DateMMYYSerializer extends StdSerializer<DateMMYY> {
        private static final long serialVersionUID = 1L;

        public DateMMYYSerializer() {
            super(DateMMYY.class);
        }
        
        @Override
        public void serialize(DateMMYY value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeObject(value.toString());
        }

    }

}
