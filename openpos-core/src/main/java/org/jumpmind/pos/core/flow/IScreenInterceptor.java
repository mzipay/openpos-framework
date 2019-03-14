package org.jumpmind.pos.core.flow;

import org.jumpmind.pos.core.ui.UIMessage;

public interface IScreenInterceptor {

    void intercept(String appId, String nodeId, UIMessage screen);
}
