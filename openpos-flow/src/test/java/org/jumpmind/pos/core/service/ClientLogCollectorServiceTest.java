package org.jumpmind.pos.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.core.Appender;
//import org.apache.logging.log4j.core.Filter;
//import org.apache.logging.log4j.core.LogEvent;
//import org.apache.logging.log4j.core.LoggerContext;
//import org.apache.logging.log4j.core.config.Configuration;
//import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.junit.LoggerContextRule;
import org.apache.logging.log4j.test.appender.ListAppender;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import org.apache.log4j.Level;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ClientLogCollectorService.class )
@WebMvcTest(value = ClientLogCollectorService.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {
    "openpos.clientLogCollector.timestampFormat=yyyy_MM_dd HH:mm:ss,SSS",
})
public class ClientLogCollectorServiceTest {

    private static ListAppender appender;

    @ClassRule
    public static LoggerContextRule init = new LoggerContextRule("log4j2-test.yml");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ClientLogCollectorService clientLogCollector;
    
    ObjectMapper mapper = new ObjectMapper();

    @Before
    public void beforeTest() {
        appender.clear();
    }

    @BeforeClass
    public static void setupLogging() {
        appender = init.getListAppender("List");
    }
    
    @Test
    public void whenPostclientLogs_returnOk() throws Exception {
        ClientLogEntry[] entries = {
                new ClientLogEntry(ClientLogType.log, new Date(), "Some log message"),
                new ClientLogEntry(ClientLogType.debug, new Date(), "Some debug message")

        };

        mockMvc.perform(
                post("/api/appId/pos/deviceId/111-11111/clientlogs")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(entries))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
    
    /**
     * Ensure that openpos.clientLogCollector.timestampFormat is used when present
     * for formatting log timestamps.
     * @throws Exception
     */
    @Test
    public void dateFormatTest() throws Exception {
        Logger oldLogger = clientLogCollector.logger;

//        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
//        final Configuration config = ctx.getConfiguration();

        // TODO fix this..

//        TestingBufferAppender appender = new TestingBufferAppender();

//        appender.setLayout(new PatternLayout("%X{timestamp} [%X{deviceId}] %p %m%n"));
//        appender.setThreshold(Level.ALL);
//        appender.setImmediateFlush(true);

//        final PatternLayout layout = PatternLayout.createDefaultLayout(config);
//        final Appender appender = WriterAppender.createAppender(layout, null, writer, writerName, false, true);
//        appender.start();
//        config.addAppender(appender);
//        updateLoggers(appender, config);
//
//        LogManager.getLogger(ClientLogCollectorService.class).addAppender(appender);
//        LogManager.getLogger(ClientLogCollectorService.class).setLevel(Level.ALL);

        try {
            Date testDate = new Date();
            Calendar testCal = Calendar.getInstance();
            testCal.setTime(testDate);
            SimpleDateFormat expectedDateFormat = new SimpleDateFormat(clientLogCollector.timestampFormat);
            String expectedDateStr = expectedDateFormat.format(testDate);
            
            ClientLogEntry[] entries = {
                    new ClientLogEntry(ClientLogType.log, testDate, "Message 1~"),
                    new ClientLogEntry(ClientLogType.debug, testDate, "Message 2")
            };
            
            clientLogCollector.clientLogs("foo", "111-22222", Arrays.asList(entries));
            String expectedLogOutput = String.format("%s [111-22222] INFO Message 1~%s%s [111-22222] DEBUG Message 2%s", 
                    expectedDateStr, System.getProperty("line.separator"), 
                    expectedDateStr, System.getProperty("line.separator")
            );
//            ("this needs updated for log4j2")
//            assertThat(appender.buffer.toString()).isEqualTo(expectedLogOutput);

//            List<LogEvent> logEvents = appender.getEvents();
//            List<String> logMessages = logEvents.stream()
////                    .filter(event -> event.getLevel().equals(Level.ERROR))
//                    .map(event -> event.getMessage().getFormattedMessage())
//                    .collect(Collectors.toList());

//            System.out.println("Messagees: " + logMessages);

            assertThat("YES").isEqualTo("Fail");
        } finally {
            clientLogCollector.logger = oldLogger;
//            org.apache.log4j.Logger.getLogger(ClientLogCollectorService.class).removeAppender(appender);
        }
        
        
    }

//    private void updateLoggers(final Appender appender, final Configuration config) {
//        final Level level = null;
//        final Filter filter = null;
//        for (final LoggerConfig loggerConfig : config.getLoggers().values()) {
//            loggerConfig.addAppender(appender, level, filter);
//        }
//        config.getRootLogger().addAppender(appender, level, filter);
//    }
    

}
