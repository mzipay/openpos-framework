package org.jumpmind.pos.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component("serverCoreSessionDisconnectedListener")
public class SessionDisconnectedListener implements ApplicationListener<SessionDisconnectEvent> {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SessionConnectListener sessionConnectListener;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {        
        Message<?> msg = event.getMessage();
        String sessionId = (String) msg.getHeaders().get("simpSessionId");
        logger.info("session disconnected: {}", sessionId);
        sessionConnectListener.removeSession(sessionId);       
    }


}