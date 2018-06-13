package org.jumpmind.pos.core.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.Scope;
import org.jumpmind.pos.core.flow.ScopeValue;
import org.jumpmind.pos.core.flow.StateManager;
import org.jumpmind.pos.core.service.ScreenService;

public class DevToolsMessage extends Screen {
	
    private static final long serialVersionUID = 1L;
	
	private Map<String, List<ScopeField>> scopes = new HashMap<>();
	
	public DevToolsMessage(IStateManager sm, ScreenService ss) {
		setType(ScreenType.DevTools);
		setName("DevTools");
		setRefreshAlways(true);
		setScopes(sm.getApplicationState().getScope());
		this.put("scopes", scopes);
		
		//sm.getApplicationState().getCurrentContext().getFlowConfig().getConfigScope();
		
		IState currentState = sm.getCurrentState();
		this.put("currentState", sm.getApplicationState().getCurrentContext().getFlowConfig().getStateConfig(currentState));
	}
	
	public DevToolsMessage(IStateManager sm, ScreenService ss, Map<String, String> element, String scopeType, String cmd) {
		setType(ScreenType.DevTools);
		setName("DevTools");
		setRefreshAlways(true);
		setScopes(sm.getApplicationState().getScope());
		
		
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
    		}
		} else {
			// TODO 'add a scope' support?
		}
		this.put("scopes", scopes);
	}


	private void setScopes (Scope scope) {
		scopes.put("ConversationScope", buildScope(scope.getConversationScope()));
		scopes.put("NodeScope", buildScope(scope.getNodeScope()));
		scopes.put("SessionScope", buildScope(scope.getSessionScope()));
		
	}
	
	private List<ScopeField> buildScope(Map<String, ScopeValue> map) {
		Set<String> keys = map.keySet();
		List<ScopeField> res = new ArrayList<>();
		for (String key : keys) {
			res.add(new ScopeField(key, map.get(key).getValue().toString(), map.get(key).getCreatedTime().toString(), map.get(key).getCreatedStackTrace()));
		}
		return res;
	}
	
	
	
	
}

