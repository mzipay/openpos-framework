package org.jumpmind.pos.core.screeninterceptor;

import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.service.IDisableActionItemService;
import org.springframework.beans.factory.annotation.Autowired;

public class DisableActionItemScreenPropertyStrategy implements IScreenPropertyStrategy {

	@Autowired( required = false )
	IDisableActionItemService disableActionItemService;
	
	@Override
	public Object doStrategy(String appId, String deviceId, Object property, Class<?> clazz) {
		if(property != null && MenuItem.class.equals(clazz)) {
			MenuItem item = (MenuItem)property;
			item.setEnabled(!disableActionItemService.isActionDisabled(appId, deviceId, item.getAction()));
			return item;
		}
		
		return property;
	}
}
