package org.jumpmind.pos.core.screeninterceptor;

import java.util.Map;

import org.jumpmind.pos.core.screen.Screen;
import org.jumpmind.pos.core.ui.UIMessage;

public interface IScreenPropertyStrategy {
	Object doStrategy(String appId, String deviceId, Object property, Class<?> clazz, UIMessage screen, Map<String, Object> screenContext);
}
