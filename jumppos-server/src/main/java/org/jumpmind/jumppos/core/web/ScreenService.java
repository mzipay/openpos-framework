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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ScreenService implements IScreenService {

    Logger logger = LoggerFactory.getLogger(getClass());
    
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    SimpMessagingTemplate template;
    @Autowired
    IStateManagerFactory stateManagerFactory;

    private Map<String, Screen> lastScreenByNodeId = new HashMap<String, Screen>();
  
    @MessageMapping("action/node/{nodeId}")
    public void action(@DestinationVariable String nodeId, Action action) {
        try {
            logger.info("Received action from {}\n{}", nodeId, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(action));
        } catch (JsonProcessingException ex) {
            logger.error("Failed to write action to JSON", ex);
        }
        IStateManager stateManager = stateManagerFactory.create(nodeId);
        stateManager.setNodeId(nodeId);
        if (stateManager != null) {
            logger.info("Posting action of {}", action);
            stateManager.doAction(action);
        }
    }
    
    @Override
    public Screen getLastScreen(String nodeId) {
        return lastScreenByNodeId.get(nodeId);
    }

    @Override
    public void showScreen(String nodeId, Screen screen) {
        if (screen != null) {
            try {
                logger.info("Show screen on nodeId " + nodeId + "\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(screen));
            } catch (JsonProcessingException ex) {
                logger.error("Failed to write screen to JSON", ex);
            }
            this.template.convertAndSend("/topic/node/" + nodeId, screen);
            lastScreenByNodeId.put(nodeId, screen);
        }
    }

    @Override
    public void refresh(String clientId) {
        showScreen(clientId, lastScreenByNodeId.get(clientId));
    }
}
