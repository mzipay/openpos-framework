package org.jumpmind.pos.util.type;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DateMMYYTest {

    @Test
    public void testDateMMYYNoArgs() {
        DateMMYY date = new DateMMYY();
        LocalDate now = LocalDate.now();
        try {
            assertEquals(now.getMonth().getValue(), date.getMonth());
            // Convert to two digit year
            assertEquals(Integer.parseInt((now.getYear() + "").substring(2, 4)), date.getYear());
        } catch (AssertionError e) {
            // try twice, just in case date and now happened be created on two
            // different
            // days
            now = LocalDate.now();
            assertEquals(now.getMonth().getValue(), date.getMonth());
            assertEquals(Integer.parseInt((now.getYear() + "").substring(2, 4)), date.getYear());
        }
    }

    @Test
    public void testDateMMYYFromString() {
        String mmyyStr = "0116";
        DateMMYY date = new DateMMYY(mmyyStr);
        assertEquals(1, date.getMonth());
        assertEquals(16, date.getYear());
        assertEquals(mmyyStr, date.toString());
    }

    @Test
    public void testDateMMYYFromLocalDate() {
        LocalDate localdate = LocalDate.of(2018, 10, 10);
        DateMMYY date = new DateMMYY(localdate);
        assertEquals(10, date.getMonth());
        assertEquals(18, date.getYear());
        assertEquals("1018", date.toString());
    }

    @Test
    public void testDateMMYYFromDate() throws ParseException {
        String mmyyStr = "0515";
        SimpleDateFormat sdf = new SimpleDateFormat("MMyy");
        Date javaUtilDate = sdf.parse(mmyyStr);
        DateMMYY date = new DateMMYY(javaUtilDate);

        assertEquals(5, date.getMonth());
        assertEquals(15, date.getYear());

        assertEquals(mmyyStr, date.toString());
    }

    @Test
    public void testWithSeparators() throws ParseException {
        String mmyyStr = "02/15";
        
        DateMMYY date = new DateMMYY(mmyyStr);
        assertEquals(2, date.getMonth());
        assertEquals(15, date.getYear());
        assertEquals("0215", date.toString());
    }

    @Test
    public void testDateMMYYFromMonthDayYear() {
        DateMMYY date = new DateMMYY(6, 2003);

        assertEquals(6, date.getMonth());
        assertEquals(3, date.getYear());

        assertEquals("0603", date.toString());
    }

    @Test
    public void testGetLocalDate() {
        DateMMYY date = new DateMMYY(7, 19);
        // Local date doesn't make assumptions about the century, see
        // testGetLocalDateWithCentury
        assertEquals(LocalDate.of(19, 7, 1), date.getLocalDate());
    }

    @Test
    public void testGetLocalDateWithCentury() {
        DateMMYY date = new DateMMYY(7, 2019);
        // LocalDate doesn't make assumptions about the century, so it leaves
        // year
        // without century
        assertEquals("19", date.getLocalDate().getYear() + "");
    }

    @Test
    public void testGetDate() throws ParseException {
        DateMMYY date = new DateMMYY(8, 20);
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, 20);
        cal.set(Calendar.MONTH, 7);
        Date javaUtilDate = cal.getTime();

        assertEquals(javaUtilDate, date.getDate());
    }

    @Test(expected = DateTimeParseException.class)
    public void testToStringBadDate() {
        // Send in month greater than 12
        String mmyyStr = "1315";
        new DateMMYY(mmyyStr);
    }

    @Test
    public void testEquals() {
        DateMMYY date1 = new DateMMYY(8, 20);
        DateMMYY date2 = new DateMMYY("0820");
        assertEquals(date1, date2);
    }

    @Test
    public void testNotEquals() {
        DateMMYY date1 = new DateMMYY(9, 2020);
        DateMMYY date2 = new DateMMYY("0820");
        assertNotEquals(date1, date2);
    }

    @Test
    public void testHashCode() {
        DateMMYY date1 = new DateMMYY(8, 2020);
        DateMMYY date2 = new DateMMYY("0820");
        assertEquals(date1.hashCode(), date2.hashCode());
    }

    @Test
    public void testUnequalHashCode() {
        DateMMYY date1 = new DateMMYY(8, 20);
        DateMMYY date2 = new DateMMYY("0720");
        assertNotEquals(date1.hashCode(), date2.hashCode());
    }

    @Test
    public void testFromString_0000ExpDate() {
        String expDate = "0000";

        assertNull(DateMMYY.fromString(expDate));
    }

    @Test(expected = DateTimeParseException.class)
    public void testConstructor_0000ExpDate() {
        String expDate = "0000";
        DateMMYY date0000 = new DateMMYY(expDate);
    }

    @Test
    public void testJsonSerialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DateMMYYContainer c = new DateMMYYContainer(new DateMMYY(3, 20));

        String json = mapper.writeValueAsString(c);
        assertEquals("{\"theDate\":\"0320\"}", json);
    }

    @Test
    public void testJsonDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateMMYYContainer d = mapper.readValue("{\"theDate\":\"0320\"}", DateMMYYContainer.class);
        assertEquals(new DateMMYY(3, 20), d.getTheDate());
    }

    public static class DateMMYYContainer {
        DateMMYY theDate;

        DateMMYYContainer() {

        }

        DateMMYYContainer(DateMMYY d) {
            this.theDate = d;
        }

        public DateMMYY getTheDate() {
            return this.theDate;
        }

        public void setTheDate(DateMMYY d) {
            this.theDate = d;
        }
    }
}
