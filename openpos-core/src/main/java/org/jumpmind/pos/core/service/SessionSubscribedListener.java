package org.jumpmind.pos.core.service;

import java.util.Arrays;

import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.IStateManagerFactory;
import org.jumpmind.pos.core.screen.DialogProperties;
import org.jumpmind.pos.core.screen.DialogScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class SessionSubscribedListener implements ApplicationListener<SessionSubscribeEvent> {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    IStateManagerFactory stateManagerFactory;

    @Autowired
    IMessageService messageService;
    
    @Autowired
    SessionAuthTracker sessionAuthTracker;

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        Message<?> msg = event.getMessage();
        String sessionId = (String) msg.getHeaders().get("simpSessionId");
        String topicName = (String) msg.getHeaders().get("simpDestination");
        String nodeId = topicName.substring(topicName.indexOf("/node/") + "/node/".length());
        String appId = topicName.substring(topicName.indexOf("/app/") + "/app/".length(), topicName.indexOf("/node/"));
        logger.info("session subscribing: {}", sessionId);
        try {
            logger.info("subscribing to {}", topicName);
            IStateManager stateManager = stateManagerFactory.retrieve(appId, nodeId);
            if (stateManager == null) {
                stateManager = stateManagerFactory.create(appId, nodeId);
            }

            stateManager.setSessionAuthenticated(sessionId, sessionAuthTracker.isSessionAuthenticated(sessionId));

            if (stateManager.isSessionAuthenticated(sessionId)) {
                stateManager.refreshScreen();
            } else {
                DialogScreen errorDialog = new DialogScreen();
                errorDialog.asDialog(new DialogProperties(false));
                errorDialog.setIcon("error");
                errorDialog.setTitle("Failed Authentication");
                errorDialog.setMessage(Arrays.asList("The client and server authentication tokens did not match"));
                messageService.sendMessage(appId, nodeId, errorDialog);
            }

        } catch (Exception ex) {
            logger.error("Failed to subscribe to " + topicName, ex);
        }
    }


}