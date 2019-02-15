package org.jumpmind.pos.core.flow;

import org.jumpmind.pos.core.screen.Screen;

public interface IScreenInterceptor {

    public void intercept(String appId, String nodeId, Screen screen);
}
