package org.jumpmind.pos.server.service;

import java.lang.management.ManagementFactory;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jumpmind.pos.server.config.PersonalizationParameters;
import org.jumpmind.pos.server.model.Action;
import org.jumpmind.pos.util.model.ProcessInfo;
import org.jumpmind.pos.util.web.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@Controller
public class MessageService implements IMessageService {

    Logger logger = LoggerFactory.getLogger(getClass());
    Logger loggerGraphical = LoggerFactory.getLogger(getClass().getName() + ".graphical");

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    SimpMessagingTemplate template;

    @Value("${openpos.screenService.jsonIncludeNulls:true}")
    boolean jsonIncludeNulls = true;
    
    @Autowired(required=false)
    List<IActionListener> actionListeners;

    @Autowired(required=false)
    PersonalizationParameters personalizationParameters;

    @Autowired
    private Environment env;
    
    private Integer port;
    private Integer pid;
    
    
    @PostConstruct
    public void init() {
        if (!jsonIncludeNulls) {
            mapper.setSerializationInclusion(Include.NON_NULL);
        }
        initProcessInfo();
    }
    
    private void initProcessInfo() {
        // TODO? May need to hook into spring's ApplicationPidFileWriter to get PID if this
        // doesn't work in all cases
        String procStr = ManagementFactory.getRuntimeMXBean().getName();
        pid = null;
        if (procStr != null) {
            String[] parts = procStr.split("@");
            if (parts != null && parts.length > 1) {
                try { pid = Integer.valueOf(parts[0]); } catch (Exception ex) {
                   logger.warn("Failed to parse pid from {}", procStr);
                }
            }
        }
        port = null;
        String portStr = env.getProperty("local.server.port");
        if (portStr == null || portStr.isEmpty()) {
            portStr = env.getProperty("server.port");
        }
        if (portStr != null) {
            try { port = Integer.valueOf(portStr); } catch (Exception ex) {
                logger.warn("Failed to parse port from {}", portStr);
             }
        }
        
        
    }

    @RequestMapping(method = RequestMethod.GET, value = "ping", produces="application/json")
    @ResponseBody
    public String ping() {
        logger.info("Received a ping request");
        return "{ \"pong\": \"true\" }";
    }

    @RequestMapping(method = RequestMethod.GET, value = "status", produces="application/json")
    @ResponseBody
    public ProcessInfo status() {
        logger.debug("Received a status request");
        return new ProcessInfo(ProcessInfo.ALIVE_STATUS, port, pid);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "personalize", produces = "application/json")
    @ResponseBody
    public String personalize() {
        logger.info("Received a personalization request");
        String response = "[ ]";
        try {
            if (personalizationParameters != null) {
                response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(personalizationParameters);
            }
        } catch (JsonProcessingException e) {
            logger.error("Could not parse personalization properties");
        }
        
        return response;
    }

    @MessageMapping("action/app/{appId}/node/{deviceId}")
    public void action(@DestinationVariable String appId, @DestinationVariable String deviceId, @Payload Action action, Message<?> message) {
        if (action.getType() == null) {
            throw new ServerException("Message/action must have a type. " + message);
        }        
        boolean handled = false;
        for (IActionListener actionListener : actionListeners) {
            if (action.getType() != null && actionListener.getRegisteredTypes() != null &&
                    actionListener.getRegisteredTypes().contains(action.getType())) {
                handled = true;
                actionListener.actionOccured(appId, deviceId, action);
            }
        }
        
        if (!handled) {
            throw new ServerException("Message/action was not handled by any action listeners. message=[" + 
                    message + "] actionListeners=[" + actionListeners + "]");
        }
    }

    @Override
    public void sendMessage(String appId, String deviceId, org.jumpmind.pos.util.model.Message message) {
        try {
            StringBuilder topic = new StringBuilder(128);
            topic.append("/topic/app/").append(appId).append("/node/").append(deviceId);
            byte[] json = messageToJson(message).getBytes("UTF-8");
            this.template.send(topic.toString(), MessageBuilder.withPayload(json).build());
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to publish message for deviceId: " + deviceId + " " + message, ex);
        }
    }
    
    protected String messageToJson(org.jumpmind.pos.util.model.Message message) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
    }

}
