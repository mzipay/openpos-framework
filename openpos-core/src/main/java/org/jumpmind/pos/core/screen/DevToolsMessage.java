package org.jumpmind.pos.core.screen;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.Scope;
import org.jumpmind.pos.core.flow.ScopeValue;
import org.jumpmind.pos.core.service.ScreenService;

public class DevToolsMessage extends Screen {
	
    private static final long serialVersionUID = 1L;
	
	private Map<String, List<ScopeField>> scopes = new HashMap<>();
	
	public DevToolsMessage(IStateManager sm, ScreenService ss) {
		setType(ScreenType.DevTools);
		setName("DevTools");
		setRefreshAlways(true);
		setScopes(sm.getScope());
		this.put("Scopes", scopes);
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

