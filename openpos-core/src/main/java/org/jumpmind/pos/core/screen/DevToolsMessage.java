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
    			//TODO add support
    		} else if (scopeType.equals("Flow")) {
    			sm.getApplicationState().getCurrentContext().removeFlowScope(scopeId);
    			scopes.get("FlowScope").removeIf(p -> p.getName().equals(scopeId));
    		}
		} else {
			// TODO 'add a scope' support?
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
		//TODO add way to populate save files on init or remove all on destruction
	}
	
	private void setScopes (IStateManager sm) {
		scopes.clear();
		scopes.put("ConversationScope", buildScope(sm.getApplicationState().getScope().getConversationScope()));
		scopes.put("NodeScope", buildScope(sm.getApplicationState().getScope().getNodeScope()));
		scopes.put("SessionScope", buildScope(sm.getApplicationState().getScope().getSessionScope()));
		scopes.put("FlowScope", buildScope(sm.getApplicationState().getCurrentContext().getFlowScope()));
	}
	
	
	private List<ScopeField> buildScope(Map<String, ScopeValue> map) {
		Set<String> keys = map.keySet();
		List<ScopeField> res = new ArrayList<>();
		for (String key : keys) {
			res.add(new ScopeField(key, map.get(key).getCreatedTime().toString(), map.get(key).getCreatedStackTrace().replaceAll("at ", "\nat_")));
		}
		return res;
	}
	
	public void saveState(IStateManager sm, String saveName) {
		String filename = "./" + saveName + ".json";
		applicationStateSerializer.serialize(sm, sm.getApplicationState(), filename);
	}
	
	public void loadState(IStateManager sm, String saveName) {
		String filename = "./" + saveName + ".json";
		
        boolean resumeState = false;
        
        ApplicationState applicationState = new ApplicationState();
        try {            
            applicationState = applicationStateSerializer.deserialize(sm, filename);
            sm.setApplicationState(applicationState);
            resumeState = true;
        } catch (FlowException ex) {
            logger.info(ex.getMessage());
        } catch (Exception ex) {
            logger.warn("Failed to load " + filename, ex);
        }
        sm.getApplicationState().getScope().setNodeScope("stateManager", sm);
        screenService.setApplicationState(applicationState);
        
        if (resumeState) {
            sm.refreshScreen();
        }
		
	}
	
	public void removeSave(String saveName) {
		String filename = "./" + saveName + ".json";
		File save = new File(filename);
		if(save.delete()) {
			logger.info("Successfully deleted save file " + filename);
		} else {
			logger.warn("Failed to delete save file " + filename);
		}
	}
	
	
	
	
}

