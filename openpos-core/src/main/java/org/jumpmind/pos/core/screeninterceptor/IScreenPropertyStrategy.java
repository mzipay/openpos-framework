package org.jumpmind.pos.core.screeninterceptor;

import java.util.Map;

import org.jumpmind.pos.core.screen.Screen;

public interface IScreenPropertyStrategy {
	Object doStrategy( String appId, String deviceId, Object property, Class<?> clazz, Screen screen, Map<String, Object> screenContext);
}
