package org.jumpmind.pos.core.flow;

import org.jumpmind.pos.core.screen.AbstractScreen;

public interface IScreenInterceptor {

    AbstractScreen intercept(AbstractScreen screen);
    
}
