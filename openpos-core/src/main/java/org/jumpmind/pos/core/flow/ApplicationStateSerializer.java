package org.jumpmind.pos.core.flow;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.jumpmind.pos.cache.service.impl.ICache;
import org.jumpmind.pos.core.flow.config.IFlowConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
public class ApplicationStateSerializer {
    
    final Logger log = LoggerFactory.getLogger(getClass());

    ObjectMapper mapper = new ObjectMapper();
    
    @Autowired
    private IFlowConfigProvider flowConfigProvider;

    @PostConstruct
    public void init() {        
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.enableDefaultTyping(DefaultTyping.NON_FINAL);
    }

    /**
     * Warning: this method has side-effects of mutating the applicationState.
     */
    public void serialize(StateManager stateManager, ApplicationState applicationState, String fileName) {
        try {
            if (applicationState.getCurrentContext().getState() != null) {
                try {                    
                    // flush pending outjection state so it's available upon rehydration.
                    stateManager.performOutjections(applicationState.getCurrentContext().getState());
                } catch (Exception ex) {
                   log.debug("Failed to perform outjections before serialization.", ex);
                }
            }
            
            filterApplicationStateForSerialization(applicationState);
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(applicationState);
            rehydrateApplicationState(stateManager, applicationState);
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            writer.println(json);
            writer.close();                

        } catch (Exception ex) {
            throw new FlowException("Failed to desererialize file " + fileName, ex);
        }
    }

    public ApplicationState deserialize(StateManager stateManager, String fileName) {
        try {            
            File f = new File(fileName);
            if (f.exists()) {                
                String json = new String(Files.readAllBytes(Paths.get(fileName)));
                ApplicationState applicationState = mapper.readValue(json, ApplicationState.class);
                rehydrateApplicationState(stateManager, applicationState);
                return applicationState;
            } else {
                throw new FlowException("Could not find file to desererialize " + fileName);    
            }
        } catch (Exception ex) {
            if (ex instanceof FlowException) {
                throw (FlowException)ex;
            } else {                
                throw new FlowException("Failed to desererialize file " + fileName, ex);
            }
        }   
    }

    public void filterApplicationStateForSerialization(ApplicationState appStateForSerialization) {
        appStateForSerialization.getScope().getNodeScope().remove("stateManager");
        Set<String> keys = new HashSet<>(appStateForSerialization.getScope().getNodeScope().keySet());

        for (String key : keys) {
            Object value = appStateForSerialization.getScope().getNodeScope().get(key).getValue();
            if (ICache.class.isAssignableFrom(value.getClass())
                    || value.getClass().toString().contains("ContextServiceClient")) {
                appStateForSerialization.getScope().getNodeScope().remove(key);
            }
        }

        appStateForSerialization.setCurrentTransition(null);
    }
    
    public void rehydrateApplicationState(StateManager stateManager, ApplicationState applicationState) {
        applicationState.getScope().setNodeScope("stateManager", stateManager);
        
        stateManager.setApplicationState(applicationState);
        if (applicationState.getCurrentContext().getState() != null) {                            
            stateManager.performInjections(applicationState.getCurrentContext().getState());
        }
        
        refreshFlowConfig(stateManager, applicationState.getCurrentContext());
        
        for (StateContext context : applicationState.getStateStack()) {
            refreshFlowConfig(stateManager, context);    
        }
    }
    
    protected void refreshFlowConfig(StateManager stateManager, StateContext stateContext) {
        String flowConfigName = stateContext.getFlowConfig().getName(); // Only the name will be available after deserialize
        stateContext.setFlowConfig(
                flowConfigProvider.getConfigByName(stateManager.getAppId(), stateManager.getNodeId(), flowConfigName));        
    }

}
