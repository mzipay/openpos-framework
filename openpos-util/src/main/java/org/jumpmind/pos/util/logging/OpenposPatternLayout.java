package org.jumpmind.pos.util.logging;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.status.StatusLogger;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.CRC32;

@Plugin(name = "OpenposPatternLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class OpenposPatternLayout extends AbstractStringLayout {

    private int historySize = 1024;

    private Map<String, String> loggedEventKeys;

    private PatternLayout patternLayout;

    protected OpenposPatternLayout(Charset charset, String pattern) {
        super(Charset.forName("UTF8"));
        patternLayout = PatternLayout.newBuilder().withPattern(pattern).build();
        this.loggedEventKeys = new LinkedHashMap<String, String>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return (size() >= historySize);
            }
        };
    }

    @PluginFactory
    public static OpenposPatternLayout createLayout(@PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset,
                                                    @PluginAttribute(value = "pattern") String pattern) {
        return new OpenposPatternLayout(charset, pattern);
    }

    public byte[] getHeader() {
        loggedEventKeys.clear();
        return null;
    }

    @Override
    public String toSerializable(LogEvent event) {
        String key = toKey(event);
        if (key != null) {
            if (loggedEventKeys.containsKey(key)) {
                event = supressStackTrace(event, key);
            } else {
                event = appendKey(event, key);
                loggedEventKeys.put(key, null);
            }
        }

        return patternLayout.toSerializable(event);
    }

    protected long getThrowableHash(StackTraceElement[] elements) throws UnsupportedEncodingException {
        CRC32 crc = new CRC32();
        for (StackTraceElement element : elements) {
            crc.update((element.getClassName() + element.getMethodName()).getBytes("UTF8"));
        }
        return crc.getValue();
    }

    protected LogEvent appendKey(LogEvent event, String key) {
        String message = getMessageWithKey(event, key, ".init");
        LogEvent eventClone = new Log4jLogEvent.Builder(event).setMessage(new SimpleMessage(message)).build();
        return eventClone;
    }

    protected LogEvent supressStackTrace(LogEvent event, String key) {
        String message = getMessageWithKey(event, key);
        LogEvent eventClone = new Log4jLogEvent.Builder(event).setMessage(new SimpleMessage(message)).setThrown(null).build();
        return eventClone;
    }

    protected String getMessageWithKey(LogEvent event, String key) {
        return getMessageWithKey(event, key, null);
    }

    protected String getMessageWithKey(LogEvent event, String key, String prefix) {
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

    protected String toKey(LogEvent event) {
        if (event.getThrown() == null || event.getThrown().getStackTrace() == null) {
            return null;
        }

        try {
            StringBuilder buff = new StringBuilder(128);
            Throwable throwable = event.getThrown();
            buff.append(throwable.getClass().getSimpleName());
            if (throwable.getStackTrace().length == 0) {
                buff.append("-jvm-optimized");
            }
            buff.append(":");
            buff.append(getThrowableHash(event.getThrown().getStackTrace()));
            return buff.toString();
        } catch (Exception ex) {
            StatusLogger.getLogger().error("Failed to hash stack trace.", ex);
            return null;
        }
    }


}
