package org.jumpmind.pos.util.logging;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.CRC32;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import org.slf4j.LoggerFactory;

public class OpenposPatternLayoutEncoder extends PatternLayoutEncoder {

    private int historySize = 1024;

    private Map<String, String> loggedEventKeys;

    public OpenposPatternLayoutEncoder() {
        this.loggedEventKeys = new LinkedHashMap<String, String>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return (size() >= historySize);
            }
        };
    }

    @Override
    public byte[] headerBytes() {
        loggedEventKeys.clear();
        return super.headerBytes();
    }

    public byte[] encode(ILoggingEvent event) {
        if (event.getThrowableProxy() == null) {
            String key = toKey(event);
            if (loggedEventKeys.containsKey(key)) {
                event = supressStackTrace(event, key);
            } else {
                event = appendKey(event, key);
                loggedEventKeys.put(key, null);
            }
        }

        return super.encode(event);
    }

    protected long getThrowableHash(StackTraceElementProxy[] elements) throws UnsupportedEncodingException {
        CRC32 crc = new CRC32();
        for (StackTraceElementProxy element : elements) {
            StackTraceElement stackTraceElement = element.getStackTraceElement();
            crc.update((stackTraceElement.getClassName() + stackTraceElement.getMethodName()).getBytes("UTF8"));
        }
        return crc.getValue();
    }

    protected ILoggingEvent appendKey(ILoggingEvent event, String key) {
        String message = getMessageWithKey(event, key, ".init");
        LogEvent eventClone = new Log4jLogEvent.Builder(event).setMessage(new SimpleMessage(message)).build();
        return eventClone;
    }

    protected ILoggingEvent supressStackTrace(ILoggingEvent event, String key) {
        String message = getMessageWithKey(event, key);
        LogEvent eventClone = new Log4jLogEvent.Builder(event).setMessage(new SimpleMessage(message)).setThrown(null).build();
        return eventClone;
    }

    protected String getMessageWithKey(ILoggingEvent event, String key) {
        return getMessageWithKey(event, key, null);
    }

    protected String getMessageWithKey(ILoggingEvent event, String key, String prefix) {
        StringBuilder buff = new StringBuilder(128);
        if (event.getMessage() != null) {
            buff.append(event.getMessage()).append(" ");
        }
        buff.append("StackTraceKey");
        if (prefix != null) {
            buff.append(prefix);
        }
        buff.append(" [").append(key).append("]");
        return buff.toString();
    }

    protected String toKey(ILoggingEvent event) {
        try {
            StringBuilder buff = new StringBuilder(128);

            IThrowableProxy throwable = event.getThrowableProxy();
            buff.append(throwable.getClassName());

            StackTraceElementProxy[] stackTraceElements = throwable.getStackTraceElementProxyArray();

            if (stackTraceElements == null || stackTraceElements.length == 0) {
                buff.append("-jvm-optimized");
            } else {
                buff.append(":");
                buff.append(getThrowableHash(stackTraceElements));
                return buff.toString();
            }


        } catch (Exception ex) {
            System.err.println("Failed to hash stack trace for " + ex.toString());
            ex.printStackTrace(System.err);
            return null;
        }
    }


}
