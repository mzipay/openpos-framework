package org.jumpmind.pos.core.screeninterceptor;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.core.screen.ActionItem;
import org.jumpmind.pos.core.service.IKeyMappingService;
import org.jumpmind.pos.core.ui.UIMessage;
import org.springframework.beans.factory.annotation.Autowired;

public class KeyMappingScreenPropertyStrategy implements IScreenPropertyStrategy {

	@Autowired
	IKeyMappingService keyMappingService;
	
	@In(scope=ScopeType.Device) 
	private IStateManager stateManager;
	
	@Override
	public Object doStrategy(String appId, String deviceId, Object property, Class<?> clazz, UIMessage screen, Map<String, Object> screenContext) {
		if (property != null 
		        && ActionItem.class.isAssignableFrom(clazz)) {
			ActionItem item = (ActionItem)property;
			String keyMapping = keyMappingService.getKeyMapping(screen, item.getAction(), screenContext);
			if (!StringUtils.isEmpty(keyMapping)) {			    
			    item.setKeybind(keyMapping);
			}
			return item;
		}
		return property;
	}

}
