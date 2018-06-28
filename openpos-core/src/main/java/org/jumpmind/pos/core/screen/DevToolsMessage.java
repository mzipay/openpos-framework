package org.jumpmind.pos.core.screen;

import java.io.File;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.ApplicationState;
import org.jumpmind.pos.core.flow.ApplicationStateSerializer;
import org.jumpmind.pos.core.flow.FlowException;
import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.Scope;
import org.jumpmind.pos.core.flow.ScopeValue;
import org.jumpmind.pos.core.flow.StateContext;
import org.jumpmind.pos.core.flow.config.FlowConfig;
import org.jumpmind.pos.core.flow.config.StateConfig;
import org.jumpmind.pos.core.service.IScreenService;
import org.jumpmind.pos.core.service.ScreenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class DevToolsMessage extends Screen {
	
	@Autowired
	private ApplicationStateSerializer applicationStateSerializer;
	
    @Autowired
    private IScreenService screenService;
	
    private static final long serialVersionUID = 1L;
    
    private static final String SAVE_PATH = "./src/main/resources/saveStates/";

	private Map<String, List<ScopeField>> scopes = new HashMap<>();
	
	private List<String> actions = new ArrayList<>();
	
	private List<String> saveFiles = new ArrayList<>();
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	DevToolsMessage() {
		setType(ScreenType.DevTools);
		setRefreshAlways(true);
	}
	
	public void createMessage(IStateManager sm, ScreenService ss) {
		this.clearAdditionalProperties();
		setScopes(sm);
		setCurrentStateAndActions(sm);
		
		this.put("scopes", scopes);
		
//		Map<String, Object> map = sm.getApplicationState().getCurrentContext().getFlowConfig().getConfigScope();
//		Set<String> keys = map.keySet();
//		for (String key : keys) {
//			Object obj = map.get(key);
//		}
	}
	
	public void createMessage(IStateManager sm, ScreenService ss, Map<String, String> element, String scopeType, String cmd) {
		this.clearAdditionalProperties();
		setScopes(sm);
		setCurrentStateAndActions(sm);
		
		String scopeId = element.get("ID");
		if (cmd.equals("remove")) {
			if (scopeType.equals("Node")) {
    			sm.getApplicationState().getScope().removeNodeScope(scopeId);
    			scopes.get("NodeScope").removeIf(p -> p.getName().equals(scopeId));
    		} else if (scopeType.equals("Session")) {
    			sm.getApplicationState().getScope().removeSessionScope(scopeId);
    			scopes.get("SessionScope").removeIf(p -> p.getName().equals(scopeId));
    		} else if (scopeType.equals("Conversation")) {
    			sm.getApplicationState().getScope().removeConversationScope(scopeId);
    			scopes.get("ConversationScope").removeIf(p -> p.getName().equals(scopeId));
    		} else if (scopeType.equals("Config")) {
    			sm.getApplicationState().getCurrentContext().getFlowConfig().getConfigScope().remove(scopeId);
    			scopes.get("ConfigScope").removeIf(p -> p.getName().equals(scopeId));
    		} else if (scopeType.equals("Flow")) {
    			sm.getApplicationState().getCurrentContext().removeFlowScope(scopeId);
    			scopes.get("FlowScope").removeIf(p -> p.getName().equals(scopeId));
    		}
		} 
		this.put("scopes", scopes);
	}

	private void setCurrentStateAndActions (IStateManager sm) {
		loadSaveFiles();
		
		IState currentState = sm.getCurrentState();
		FlowConfig fc = sm.getApplicationState().getCurrentContext().getFlowConfig();
		if (fc != null) {
			StateConfig sc = fc.getStateConfig(currentState);
			Set<String> keySet = sc.getActionToStateMapping().keySet();
			actions.clear();
			for (String key : keySet) {
				actions.add(key);
				actions.add(sc.getActionToStateMapping().get(key).toString().replace("class ", ""));
			}
			this.put("actions", actions);
			this.put("actionsSize", actions.size());
			this.put("currentState", sm.getApplicationState().getCurrentContext().getFlowConfig().getStateConfig(currentState));
		}
	}
	
	public void loadSaveFiles () {
		saveFiles.clear();
		File dir = null;
		try {
			dir = new File(SAVE_PATH);
			File[] directoryListing = dir.listFiles();
			if (directoryListing != null) {
				int i = 0;
				for (File child : directoryListing) {
					this.saveFiles.add(child.getName().replaceAll(".json", ""));
					i++;
			    }
			   	logger.info("Loaded " + i + " save files from " + dir);
			} else {
			    logger.warn("No save files found in save directory.");
			}
		} catch (Exception ex) {
			logger.warn("Cannot load save files from " + SAVE_PATH);
		}
		 
		 this.put("saveFiles", saveFiles);
	}
	
	private void setScopes (IStateManager sm) {
		try {
			scopes.clear();
			scopes.put("ConversationScope", buildScope(sm.getApplicationState().getScope().getConversationScope()));
			scopes.put("NodeScope", buildScope(sm.getApplicationState().getScope().getNodeScope()));
			scopes.put("SessionScope", buildScope(sm.getApplicationState().getScope().getSessionScope()));
			scopes.put("FlowScope", buildScope(sm.getApplicationState().getCurrentContext().getFlowScope()));
			scopes.put("ConfigScope", buildConfigScope(sm.getApplicationState().getCurrentContext().getFlowConfig().getConfigScope()));
		} catch (Exception ex) {
			logger.warn("Error loading in Developer Tool application scopes. Deleting local storage may fix this.", ex);
		}
	}
	
	
	private List<ScopeField> buildScope(Map<String, ScopeValue> map) {
		Set<String> keys = map.keySet();
		List<ScopeField> res = new ArrayList<>();
		for (String key : keys) {
			res.add(new ScopeField(key, map.get(key).getCreatedTime().toString(), map.get(key).getCreatedStackTrace().replaceAll("at ", "\nat_")));
		}
		return res;
	}
	
	private List<ScopeField> buildConfigScope(Map<String, Object> map) {
		Set<String> keys = map.keySet();
		List<ScopeField> res = new ArrayList<>();
		for (String key : keys) {
			res.add(new ScopeField(key, map.get(key).toString(), map.get(key).toString().replaceAll("at ", "\nat_")));
		}
		return res;
	}
	
	public void saveState(IStateManager sm, String saveName) {
		String filename = SAVE_PATH + saveName + ".json";
		try {
			applicationStateSerializer.serialize(sm, sm.getApplicationState(), filename);
			logger.info("Saved state " + filename);
		} catch (FlowException ex) {
			logger.info(ex.getMessage());
		}
		catch (Exception ex) {
			logger.warn("Unable to save state at " + filename, ex);
		}
	}
	
	public void loadState(IStateManager sm, String saveName) {
		String filename = SAVE_PATH + saveName + ".json";
		
        boolean resumeState = false;
       
        try {     
            ApplicationState applicationState = new ApplicationState();
            applicationState = applicationStateSerializer.deserialize(sm, filename);
            sm.setApplicationState(applicationState);
            screenService.setApplicationState(applicationState);
            sm.getApplicationState().getScope().setNodeScope("stateManager", sm);
            resumeState = true;
            logger.info("Loaded save state " + filename);
        } catch (FlowException ex) {
            logger.info(ex.getMessage());
        } catch (Exception ex) {
            logger.warn("Failed to load save state " + filename, ex);
        }

        if (resumeState) {
            sm.refreshScreen();
        }
		
	}
	
	public void removeSave(String saveName) {
		String filename = SAVE_PATH + saveName + ".json";
		File save = null;
		try {
			save = new File(filename);
		} catch (Exception ex) {
			logger.warn("Can't find save state " + filename, ex);
		}
		if(save != null && save.delete()) {
			logger.info("Successfully deleted save state " + filename);
		} else {
			logger.warn("Failed to delete save state " + filename);
		}
	}
	
	
	
	
}

