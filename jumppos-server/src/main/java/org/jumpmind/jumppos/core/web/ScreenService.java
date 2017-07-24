package org.jumpmind.jumppos.core.web;

import java.util.HashMap;

import java.util.Map;

import org.jumpmind.jumppos.core.flow.Action;
import org.jumpmind.jumppos.core.flow.IStateManager;
import org.jumpmind.jumppos.core.flow.IStateManagerRepository;
import org.jumpmind.jumppos.core.flow.StateManagerRepository;
import org.jumpmind.jumppos.core.model.Screen;
import org.jumpmind.jumppos.service.IScreenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ScreenService implements IScreenService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    IStateManagerRepository repository;

    private Map<String, Screen> lastScreenByClientId = new HashMap<String, Screen>();

    @MessageMapping("action/store/{storeId}/device/{deviceId}")
    public void action(@DestinationVariable String storeId, @DestinationVariable String deviceId, Action action) {
        String clientId = String.format("/store/%s/device/%s", storeId, deviceId);
        //clientId = clientId.substring(clientId.indexOf("/store"));
        logger.info("Received action from {}", clientId);
        IStateManager manager = repository.createOrLookup(clientId);
        if (manager != null) {
            logger.info("Posting action of {}", action);
            manager.doAction(action);
        }
    }

    @Override
    public void showScreen(String clientId, Screen screen) {
        if (screen != null) {
            this.template.convertAndSend("/topic" + clientId, screen);
            lastScreenByClientId.put(clientId, screen);
        }
    }

    @Override
    public void refresh(String clientId) {
        showScreen(clientId, lastScreenByClientId.get(clientId));
    }
}
