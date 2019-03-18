package org.jumpmind.pos.util.type;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DateMMDDYYYYTest {

  @Test
  public void testDateMMDDYYYYNoArgs() {
    DateMMDDYYYY date = new DateMMDDYYYY();
    LocalDate now = LocalDate.now();
    try {
      assertEquals( now.getMonth().getValue(), date.getMonth() );
      assertEquals( now.getYear(), date.getYear() );
      assertEquals( now.getDayOfMonth(), date.getDayOfMonth() );
    } catch ( AssertionError e ) {
      // try twice, just in case date and now happened be created on two different
      // days
      now = LocalDate.now();
      assertEquals( now.getMonth().getValue(), date.getMonth() );
      assertEquals( now.getYear(), date.getYear() );
      assertEquals( now.getDayOfMonth(), date.getDayOfMonth() );
    }
  }
  
  @Test
  public void testDateMMDDYYYYFromString() {
    String mmddyyyyStr = "01012016";
    DateMMDDYYYY date = new DateMMDDYYYY( mmddyyyyStr );
    assertEquals( 1, date.getMonth() );
    assertEquals( 1, date.getDayOfMonth() );
    assertEquals( 2016, date.getYear() );
    assertEquals( mmddyyyyStr, date.toString() );
  }

  @Test
  public void testDateMMDDYYYYFromLocalDate() {
    LocalDate localdate = LocalDate.of( 2018, 10, 10 );
    DateMMDDYYYY date = new DateMMDDYYYY( localdate );
    assertEquals( 10, date.getMonth() );
    assertEquals( 10, date.getDayOfMonth() );
    assertEquals( 2018, date.getYear() );
    assertEquals( "10102018", date.toString() );
  }

  @Test
  public void testWithSeparators() throws ParseException {
      String dateStr = "02-15-2020";
      
      DateMMDDYYYY date = new DateMMDDYYYY(dateStr);
      assertEquals(2, date.getMonth());
      assertEquals(15, date.getDayOfMonth());
      assertEquals(2020, date.getYear());
      assertEquals("02152020", date.toString());
  }

  @Test
  public void testDateMMDDYYYYFromDate() throws ParseException {
    String mmddyyyyStr = "05052015";
    SimpleDateFormat sdf = new SimpleDateFormat( "MMddyyyy" );
    Date javaUtilDate = sdf.parse( mmddyyyyStr );
    DateMMDDYYYY date = new DateMMDDYYYY( javaUtilDate );
    
    assertEquals( 5, date.getMonth() );
    assertEquals( 5, date.getDayOfMonth() );
    assertEquals( 2015, date.getYear() );
    
    assertEquals( mmddyyyyStr, date.toString() );
  }

  @Test
  public void testDateMMDDYYYYFromMonthDayYear() {
    DateMMDDYYYY date = new DateMMDDYYYY( 6, 7, 2003 );
    
    assertEquals( 6, date.getMonth() );
    assertEquals( 7, date.getDayOfMonth() );
    assertEquals( 2003, date.getYear() );
    
    assertEquals( "06072003", date.toString() );
  }

  @Test
  public void testGetLocalDate() {
    DateMMDDYYYY date = new DateMMDDYYYY( 7, 31, 2019 );
    assertEquals( LocalDate.of( 2019, 7, 31 ), date.getLocalDate() );
  }

  @Test
  public void testGetDate() throws ParseException {
    DateMMDDYYYY date = new DateMMDDYYYY( 8, 31, 2020 );
    String mmddyyyyStr = "08312020";
    SimpleDateFormat sdf = new SimpleDateFormat( "MMddyyyy" );
    Date javaUtilDate = sdf.parse( mmddyyyyStr );
    
    assertEquals( javaUtilDate, date.getDate() );
  }
  
  @Test( expected=DateTimeParseException.class)
  public void testToStringBadDate() {
    // pick day of month greater than number of days in any month.  If I pick
    // 30 or 31 for February for example, LocalDate class just picks 28 as the
    // dayOfMonth for non leap year
    String mmddyyyyStr = "02322015";  
    new DateMMDDYYYY( mmddyyyyStr );
  }
  
  @Test
  public void testEquals() {
    DateMMDDYYYY date1 = new DateMMDDYYYY( 8, 31, 2020 );
    DateMMDDYYYY date2 = new DateMMDDYYYY( "08312020" );
    assertEquals( date1, date2 );
  }
  
  @Test
  public void testNotEquals() {
    DateMMDDYYYY date1 = new DateMMDDYYYY( 8, 30, 2020 );
    DateMMDDYYYY date2 = new DateMMDDYYYY( "08312020" );
    assertNotEquals( date1, date2 );
  }
  
  @Test
  public void testHashCode() {
    DateMMDDYYYY date1 = new DateMMDDYYYY( 8, 31, 2020 );
    DateMMDDYYYY date2 = new DateMMDDYYYY( "08312020" );
    assertEquals( date1.hashCode(), date2.hashCode() );
  }
  
  @Test
  public void testUnequalHashCode() {
    DateMMDDYYYY date1 = new DateMMDDYYYY( 8, 30, 2020 );
    DateMMDDYYYY date2 = new DateMMDDYYYY( "08312020" );
    assertNotEquals( date1.hashCode(), date2.hashCode() );
  }
  
  @Test 
  public void testJsonSerialize() throws JsonProcessingException {
      ObjectMapper mapper = new ObjectMapper();
      DateMMDDYYYYContainer c = new DateMMDDYYYYContainer(new DateMMDDYYYY(3, 15, 2020));
      
      String json = mapper.writeValueAsString(c);
      assertEquals("{\"theDate\":\"03152020\"}" , json);
  }

  @Test 
  public void testJsonDeserialize() throws IOException {
      ObjectMapper mapper = new ObjectMapper();
      DateMMDDYYYYContainer d = mapper.readValue("{\"theDate\":\"03152020\"}", DateMMDDYYYYContainer.class);
      assertEquals(new DateMMDDYYYY(3,15,2020), d.getTheDate());
  }
  
  
  public static class DateMMDDYYYYContainer {
      DateMMDDYYYY theDate;
      DateMMDDYYYYContainer() {
          
      }
      DateMMDDYYYYContainer(DateMMDDYYYY d) {
          this.theDate = d;
      }
      
      public DateMMDDYYYY getTheDate() {
          return this.theDate;
      }
      
      public void setTheDate(DateMMDDYYYY d) {
          this.theDate = d;
      }
  }

}
