package org.jumpmind.pos.core.config;

import org.springframework.context.ApplicationEvent;
import org.springframework.messaging.Message;

public class SessionSubscribedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    Message<?> message;

    public SessionSubscribedEvent(Object source, Message<?> message) {
        super(source);
        this.message = message;
    }

    public Message<?> getMessage() {
        return message;
    }

}
