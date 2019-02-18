package org.jumpmind.pos.core.screeninterceptor;

import org.jumpmind.pos.core.screen.ActionItem;
import org.jumpmind.pos.core.screen.Screen;
import org.jumpmind.pos.core.service.IKeyMappingService;
import org.springframework.beans.factory.annotation.Autowired;

public class KeyMappingScreenPropertyStrategy implements IScreenPropertyStrategy {

	@Autowired
	IKeyMappingService keyMappingService;
	
	@Override
	public Object doStrategy(String appId, String deviceId, Object property, Class<?> clazz, Screen screen) {
		if(property != null && ActionItem.class.equals(clazz)) {
			ActionItem item = (ActionItem)property;
			item.setKeybind(keyMappingService.getKeyMapping(screen.getId(), item.getAction()));
			return item;
		}
		return property;
	}

}
