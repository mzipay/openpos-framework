package org.jumpmind.pos.core.service;

import java.util.Map;

import org.jumpmind.pos.core.screen.Screen;

public interface IKeyMappingService {
	
    String getKeyMapping(Screen screen, String actionName, Map<String, Object> screenContext);

}
