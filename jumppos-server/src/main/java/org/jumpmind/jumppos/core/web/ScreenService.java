package org.jumpmind.jumppos.core.web;

import java.util.HashMap;
import java.util.Map;

import org.jumpmind.jumppos.core.flow.Action;
import org.jumpmind.jumppos.core.flow.IScreenService;
import org.jumpmind.jumppos.core.flow.IStateManager;
import org.jumpmind.jumppos.core.flow.IStateManagerFactory;
import org.jumpmind.jumppos.core.model.Screen;
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
    IStateManagerFactory stateManagerFactory;

    private Map<String, Screen> lastScreenByClientId = new HashMap<String, Screen>();
    
    @MessageMapping("action/node/{nodeId}")
    public void action(@DestinationVariable String nodeId, Action action) {
        logger.info("Received action from {}", nodeId);
        IStateManager stateManager = stateManagerFactory.create(nodeId);
        stateManager.setNodeId(nodeId);
        if (stateManager != null) {
            logger.info("Posting action of {}", action);
            stateManager.doAction(action);
        }
    }

    @Override
    public void showScreen(String nodeId, Screen screen) {
        if (screen != null) {
            this.template.convertAndSend("/topic/node/" + nodeId, screen);
            lastScreenByClientId.put(nodeId, screen);
        }
    }

    @Override
    public void refresh(String clientId) {
        showScreen(clientId, lastScreenByClientId.get(clientId));
    }
}
