package org.jumpmind.pos.core.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.FlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin
@Controller
public class MessageService implements IMessageService {

    Logger logger = LoggerFactory.getLogger(getClass());
    Logger loggerGraphical = LoggerFactory.getLogger(getClass().getName() + ".graphical");

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    SimpMessagingTemplate template;

    @Value("${org.jumpmind.pos.core.service.ScreenService.jsonIncludeNulls:true}")
    boolean jsonIncludeNulls = true;
    
    @Autowired
    List<IActionListener> actionListeners;


    @PostConstruct
    public void init() {
        if (!jsonIncludeNulls) {
            mapper.setSerializationInclusion(Include.NON_NULL);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "ping")
    @ResponseBody
    public String ping() {
        logger.info("Received a ping request");
        return "{ \"pong\": \"true\" }";
    }

    @RequestMapping(method = RequestMethod.GET, value = "app/{appId}/node/{nodeId}/{action}/{payload}")
    public void getAction(
            @PathVariable String appId,
            @PathVariable String nodeId,
            @PathVariable String action,
            @PathVariable String payload,
            HttpServletResponse resp) {
        logger.info("Received a request for {} {} {} {}", appId, nodeId, action, payload);
        for (IActionListener iActionListener : actionListeners) {
            // ???
        }
    }


    @MessageMapping("action/app/{appId}/node/{nodeId}")
    public void action(@DestinationVariable String appId, @DestinationVariable String nodeId, Action action) {
        for (IActionListener actionListener : actionListeners) {
            if (action.getType() != null && actionListener.getRegisteredTypes() != null &&
                    actionListener.getRegisteredTypes().contains(action.getType())) {
                actionListener.actionOccured(appId, nodeId, action);
            }
        }
    }

    @Override
    public void sendMessage(String appId, String nodeId, org.jumpmind.pos.core.model.Message message) {
        try {
            StringBuilder topic = new StringBuilder(128);
            topic.append("/topic/app/").append(appId).append("/node/").append(nodeId);
            byte[] json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message).getBytes("UTF-8");
            this.template.send(topic.toString(), MessageBuilder.withPayload(json).build());
        } catch (Exception ex) {
            throw new FlowException("Failed to serialize message for node: " + nodeId + " " + message, ex);
        }
    }

}
