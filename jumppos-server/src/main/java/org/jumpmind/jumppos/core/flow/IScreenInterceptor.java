package org.jumpmind.jumppos.core.flow;

import org.jumpmind.jumppos.core.screen.DefaultScreen;

public interface IScreenInterceptor {

    DefaultScreen intercept(DefaultScreen screen);
    
}
