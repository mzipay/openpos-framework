package org.jumpmind.pos.devices.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component("deviceClientDisconnectedListener")
public class SessionDisconnectedListener implements ApplicationListener<SessionDisconnectEvent> {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {        
        Message<?> msg = event.getMessage();
        String sessionId = (String) msg.getHeaders().get("simpSessionId");
        logger.info("session disconnected: {}", sessionId);
    }


}