package org.jumpmind.pos.core.screeninterceptor;

public interface IScreenPropertyStrategy {
	Object doStrategy( String appId, String deviceId, Object property, Class<?> clazz );
}
