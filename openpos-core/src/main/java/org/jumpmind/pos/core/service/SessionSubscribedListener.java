package org.jumpmind.pos.core.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.IStateManagerContainer;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.core.model.ClientConfiguration;
import org.jumpmind.pos.core.model.ConfigChangedMessage;
import org.jumpmind.pos.core.model.IConfigSelector;
import org.jumpmind.pos.core.screen.DialogProperties;
import org.jumpmind.pos.core.screen.DialogScreen;
import org.jumpmind.pos.core.screen.IconType;
import org.jumpmind.pos.server.config.MessageUtils;
import org.jumpmind.pos.server.config.SessionSubscribedEvent;
import org.jumpmind.pos.server.service.IMessageService;
import org.jumpmind.pos.server.service.SessionConnectListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class SessionSubscribedListener implements ApplicationListener<SessionSubscribedEvent>, MessageUtils {

    final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    IStateManagerContainer stateManagerContainer;

    @Autowired
    IMessageService messageService;

    @Autowired
    SessionConnectListener sessionAuthTracker;

    @Value("${openpos.incompatible.version.message:The compatibility version of the client does not match the server}")
    String incompatibleVersionMessage;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(SessionSubscribedEvent event) {
        Message<?> msg = event.getMessage();
        String sessionId = (String) msg.getHeaders().get("simpSessionId");
        Map<String, Object> queryParams = sessionAuthTracker.getQueryParams(sessionId);
        String topicName = (String) msg.getHeaders().get("simpDestination");
        String compatibilityVersion = this.getHeader(msg, MessageUtils.COMPATIBILITY_VERSION_HEADER);
        String nodeId = topicName.substring(topicName.indexOf("/node/") + "/node/".length());
        String appId = topicName.substring(topicName.indexOf("/app/") + "/app/".length(), topicName.indexOf("/node/"));
        try {
            log.info("session {} subscribed to {}", sessionId, topicName);
            IStateManager stateManager = stateManagerContainer.retrieve(appId, nodeId);
            boolean created = false;
            if (stateManager == null) {
                stateManager = stateManagerContainer.create(appId, nodeId, queryParams);
                created = true;
            } else {
                stateManager.registerQueryParams(queryParams);
            }

            stateManagerContainer.setCurrentStateManager(stateManager);

            Map<String, String> personizationProperties = new HashMap<>();
            personizationProperties.put("brandId", sessionAuthTracker.getBrandId(sessionId));
            personizationProperties.put("deviceType", sessionAuthTracker.getDeviceType(sessionId));
            stateManager.getApplicationState().getScope().setScopeValue(ScopeType.Device, "personalizationProperties",
                    personizationProperties);

            stateManager.setSessionAuthenticated(sessionId, sessionAuthTracker.isSessionAuthenticated(sessionId));
            stateManager.setSessionCompatible(sessionId, sessionAuthTracker.isSessionCompatible(sessionId));

            if (!stateManager.isSessionAuthenticated(sessionId)) {
                DialogScreen errorDialog = new DialogScreen();
                errorDialog.asDialog(new DialogProperties(false));
                errorDialog.setIcon(IconType.Error);
                errorDialog.setTitle("Failed Authentication");
                errorDialog.setMessage(Arrays.asList("The client and server authentication tokens did not match"));
                messageService.sendMessage(appId, nodeId, errorDialog);
            } else if (!stateManager.isSessionCompatible(sessionId)) {
                log.warn("Client compatiblity version of '{}' for nodeId '{}' is not compatible with the server", compatibilityVersion,
                        nodeId);
                DialogScreen errorDialog = new DialogScreen();
                // If there is no compatibility version, the client is an older
                // client that used the type attribute
                // instead of the screenType attribute for the screen type
                // value. In that case need to set the type attribute or
                // the dialog will not display on older clients
                if (compatibilityVersion == null) {
                    errorDialog.setType(errorDialog.getScreenType());
                }
                errorDialog.asDialog(new DialogProperties(false));
                errorDialog.setIcon(IconType.Error);
                errorDialog.setTitle("Incompatible Versions");
                errorDialog.setMessage(Arrays.asList(incompatibleVersionMessage.split("\n")));
                messageService.sendMessage(appId, nodeId, errorDialog);
            } else {
                sendClientConfiguration(appId, nodeId, sessionId);
                if (!created) {
                    stateManager.refreshScreen();
                }
            }

        } catch (Exception ex) {
            log.error("Failed to subscribe to " + topicName, ex);
            DialogScreen errorDialog = new DialogScreen();
            errorDialog.asDialog(new DialogProperties(false));
            errorDialog.setIcon(IconType.Error);
            errorDialog.setTitle("Failed To Subscribe");
            errorDialog.setMessage(Arrays.asList(ex.getMessage()));
            messageService.sendMessage(appId, nodeId, errorDialog);
        } finally {
            stateManagerContainer.setCurrentStateManager(null);
        }
    }

    private void sendClientConfiguration(String appId, String nodeId, String sessionId) {
        try {
            IConfigSelector configSelector = applicationContext.getBean(IConfigSelector.class);
            if (configSelector != null) {
                String theme = configSelector.getTheme();
                log.info("Config Selector Chose theme: {}", theme);
                ClientConfiguration config = configSelector.getClientConfig();
                ConfigChangedMessage configMessage = new ConfigChangedMessage(theme, config);
                messageService.sendMessage(appId, nodeId, configMessage);
            }
        } catch (BeansException e) {
            log.info("An {} is not configured.  WIll not be sending client configuration to the client",
                    IConfigSelector.class.getSimpleName());
        }
    }

}
