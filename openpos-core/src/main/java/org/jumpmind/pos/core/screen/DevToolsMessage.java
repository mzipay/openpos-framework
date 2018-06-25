package org.jumpmind.pos.core.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jumpmind.pos.core.flow.ApplicationStateSerializer;
import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.Scope;
import org.jumpmind.pos.core.flow.ScopeValue;
import org.jumpmind.pos.core.flow.config.StateConfig;
import org.jumpmind.pos.core.service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DevToolsMessage extends Screen {
	
	@Autowired
	private ApplicationStateSerializer applicationStateSerializer;
	
    private static final long serialVersionUID = 1L;

	private Map<String, List<ScopeField>> scopes = new HashMap<>();
	
	private List<String> actions = new ArrayList<>();
	
	DevToolsMessage() {
		setType(ScreenType.DevTools);
		setName("DevTools::Base");
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
		IState currentState = sm.getCurrentState();
		StateConfig sc = sm.getApplicationState().getCurrentContext().getFlowConfig().getStateConfig(currentState);
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
	
	public void saveState(IStateManager sm) {
		String filename = "./openpos-save.json";
		this.applicationStateSerializer.serialize(sm, sm.getApplicationState(), filename);
		this.put("saveFile", filename);
	}
	
	
	
	
}

