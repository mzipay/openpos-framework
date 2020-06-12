package org.jumpmind.pos.core.service;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;

import java.io.Serializable;
import java.io.StringWriter;

public class TestingBufferAppender extends AbstractAppender {

    StringWriter buffer = new StringWriter();

    protected TestingBufferAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
    }

    // TODO fix for log4j2
    
//    public TestingBufferAppender() {
//        this.setWriter(buffer);
//    }

    @Override
    public void append(LogEvent event) {
        buffer.append(event.getMessage().getFormattedMessage());
    }
}
