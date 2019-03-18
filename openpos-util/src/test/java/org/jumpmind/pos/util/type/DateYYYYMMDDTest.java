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

public class DateYYYYMMDDTest {

  @Test
  public void testDateYYYYMMDDNoArgs() {
    DateYYYYMMDD date = new DateYYYYMMDD();
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
  public void testDateYYYYMMDDFromString() {
    String mmddyyyyStr = "20160101";
    DateYYYYMMDD date = new DateYYYYMMDD( mmddyyyyStr );
    assertEquals( 1, date.getMonth() );
    assertEquals( 1, date.getDayOfMonth() );
    assertEquals( 2016, date.getYear() );
    assertEquals( mmddyyyyStr, date.toString() );
  }

  @Test
  public void testWithSeparators() throws ParseException {
      String dateStr = "2020/02/15";
      
      DateYYYYMMDD date = new DateYYYYMMDD(dateStr);
      assertEquals(2, date.getMonth());
      assertEquals(15, date.getDayOfMonth());
      assertEquals(2020, date.getYear());
      assertEquals("20200215", date.toString());
  }
  
  @Test
  public void testDateYYYYMMDDFromLocalDate() {
    LocalDate localdate = LocalDate.of( 2018, 10, 10 );
    DateYYYYMMDD date = new DateYYYYMMDD( localdate );
    assertEquals( 10, date.getMonth() );
    assertEquals( 10, date.getDayOfMonth() );
    assertEquals( 2018, date.getYear() );
    assertEquals( "20181010", date.toString() );
  }

  @Test
  public void testDateYYYYMMDDFromDate() throws ParseException {
    String mmddyyyyStr = "20150505";
    SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd" );
    Date javaUtilDate = sdf.parse( mmddyyyyStr );
    DateYYYYMMDD date = new DateYYYYMMDD( javaUtilDate );
    
    assertEquals( 5, date.getMonth() );
    assertEquals( 5, date.getDayOfMonth() );
    assertEquals( 2015, date.getYear() );
    
    assertEquals( mmddyyyyStr, date.toString() );
  }

  @Test
  public void testDateYYYYMMDDFromMonthDayYear() {
    DateYYYYMMDD date = new DateYYYYMMDD( 6, 7, 2003 );
    
    assertEquals( 6, date.getMonth() );
    assertEquals( 7, date.getDayOfMonth() );
    assertEquals( 2003, date.getYear() );
    
    assertEquals( "20030607", date.toString() );
  }

  @Test
  public void testGetLocalDate() {
    DateYYYYMMDD date = new DateYYYYMMDD( 7, 31, 2019 );
    assertEquals( LocalDate.of( 2019, 7, 31 ), date.getLocalDate() );
  }

  @Test
  public void testGetDate() throws ParseException {
    DateYYYYMMDD date = new DateYYYYMMDD( 8, 31, 2020 );
    String mmddyyyyStr = "20200831";
    SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd" );
    Date javaUtilDate = sdf.parse( mmddyyyyStr );
    
    assertEquals( javaUtilDate, date.getDate() );
  }
  
  @Test( expected=DateTimeParseException.class)
  public void testToStringBadDate() {
    // pick day of month greater than number of days in any month.  If I pick
    // 30 or 31 for February for example, LocalDate class just picks 28 as the
    // dayOfMonth for non leap year
    String mmddyyyyStr = "20150232";  
    new DateYYYYMMDD( mmddyyyyStr );
  }
  
  @Test
  public void testEquals() {
    DateYYYYMMDD date1 = new DateYYYYMMDD( 8, 31, 2020 );
    DateYYYYMMDD date2 = new DateYYYYMMDD( "20200831" );
    assertEquals( date1, date2 );
  }
  
  @Test
  public void testNotEquals() {
    DateYYYYMMDD date1 = new DateYYYYMMDD( 8, 30, 2020 );
    DateYYYYMMDD date2 = new DateYYYYMMDD( "20200831" );
    assertNotEquals( date1, date2 );
  }
  
  @Test
  public void testHashCode() {
    DateYYYYMMDD date1 = new DateYYYYMMDD( 8, 31, 2020 );
    DateYYYYMMDD date2 = new DateYYYYMMDD( "20200831" );
    assertEquals( date1.hashCode(), date2.hashCode() );
  }
  
  @Test
  public void testUnequalHashCode() {
    DateYYYYMMDD date1 = new DateYYYYMMDD( 8, 30, 2020 );
    DateYYYYMMDD date2 = new DateYYYYMMDD( "20200831" );
    assertNotEquals( date1.hashCode(), date2.hashCode() );
  }

  @Test 
  public void testJsonSerialize() throws JsonProcessingException {
      ObjectMapper mapper = new ObjectMapper();
      DateYYYYMMDDContainer c = new DateYYYYMMDDContainer(new DateYYYYMMDD(3, 15, 2020));
      
      String json = mapper.writeValueAsString(c);
      assertEquals("{\"theDate\":\"20200315\"}" , json);
  }

  @Test 
  public void testJsonDeserialize() throws IOException {
      ObjectMapper mapper = new ObjectMapper();
      DateYYYYMMDDContainer d = mapper.readValue("{\"theDate\":\"20200315\"}", DateYYYYMMDDContainer.class);
      assertEquals(new DateYYYYMMDD(3,15,2020), d.getTheDate());
  }
  
  
  public static class DateYYYYMMDDContainer {
      DateYYYYMMDD theDate;
      DateYYYYMMDDContainer() {
          
      }
      DateYYYYMMDDContainer(DateYYYYMMDD d) {
          this.theDate = d;
      }
      
      public DateYYYYMMDD getTheDate() {
          return this.theDate;
      }
      
      public void setTheDate(DateYYYYMMDD d) {
          this.theDate = d;
      }
  }
  
}
