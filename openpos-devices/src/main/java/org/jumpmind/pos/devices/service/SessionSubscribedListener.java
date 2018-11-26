package org.jumpmind.pos.devices.service;

import org.jumpmind.pos.server.config.MessageUtils;
import org.jumpmind.pos.server.config.SessionSubscribedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class SessionSubscribedListener implements ApplicationListener<SessionSubscribedEvent>, MessageUtils {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${openpos.incompatible.version.message:The compatibility version of the client does not match the server}")
    String incompatibleVersionMessage; 

    @Override
    public void onApplicationEvent(SessionSubscribedEvent event) {
        Message<?> msg = event.getMessage();
        String sessionId = (String) msg.getHeaders().get("simpSessionId");
        String topicName = (String) msg.getHeaders().get("simpDestination");
        String compatibilityVersion = this.getHeader(msg, MessageUtils.COMPATIBILITY_VERSION_HEADER);
        String nodeId = topicName.substring(topicName.indexOf("/node/") + "/node/".length());
        String appId = topicName.substring(topicName.indexOf("/app/") + "/app/".length(), topicName.indexOf("/node/"));
        try {
            logger.info("session {} subscribed to {}", sessionId, topicName);
        } catch (Exception ex) {
            logger.error("Failed to subscribe to " + topicName, ex);
        }
    }

}