package org.jumpmind.pos.core.service;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.config.Property;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringWriter;

public class TestingBufferAppender extends AbstractOutputStreamAppender<OutputStreamManager> {

    private StringWriter buffer = new StringWriter();

    private static TestingBufferManagerFactory factory = new TestingBufferManagerFactory();


    protected TestingBufferAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, Property[] properties, OutputStreamManager manager) {
        super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
    }

    // TODO fix for log4j2
    
//    public TestingBufferAppender() {
//        this.setWriter(buffer);
//    }

    @Override
    public void append(LogEvent event) {
        buffer.append(event.getMessage().getFormattedMessage());
    }

    private static OutputStreamManager getManager(final ConsoleAppender.Target target, final boolean follow, final boolean direct,
                                                  final Layout<? extends Serializable> layout) {
        final OutputStream os = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                System.out.println(b);
            }
        };
        final String managerName = target.name() + '.' + follow + '.' + direct;



        return OutputStreamManager.getManager(managerName, new FactoryData(os, managerName, layout), factory);
    }

    private static class FactoryData {
        private final OutputStream os;
        private final String name;
        private final Layout<? extends Serializable> layout;

        /**
         * Constructor.
         *
         * @param os The OutputStream.
         * @param type The name of the target.
         * @param layout A Serializable layout
         */
        public FactoryData(final OutputStream os, final String type, final Layout<? extends Serializable> layout) {
            this.os = os;
            this.name = type;
            this.layout = layout;
        }
    }

    private static class TestingBufferManagerFactory implements ManagerFactory<OutputStreamManager, FactoryData> {

        /**
         * Create an OutputStreamManager.
         *
         * @param name The name of the entity to manage.
         * @param data The data required to create the entity.
         * @return The OutputStreamManager
         */
        @Override
        public OutputStreamManager createManager(final String name, final FactoryData data) {
            return new TestingBufferOutputStreamManager(data.os, name, data.layout, true);
        }
    }

    private static class TestingBufferOutputStreamManager extends OutputStreamManager {
        protected TestingBufferOutputStreamManager(final OutputStream os, final String streamName, final Layout<?> layout,
                                      final boolean writeHeader) {
            super(os, streamName, layout, writeHeader);
        }
    }

}
