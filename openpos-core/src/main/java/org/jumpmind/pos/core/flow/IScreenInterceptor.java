package org.jumpmind.pos.core.flow;

import org.jumpmind.pos.core.screen.DefaultScreen;

public interface IScreenInterceptor {

    DefaultScreen intercept(DefaultScreen screen);
    
}
