package org.jumpmind.pos.app.startup;

import org.jumpmind.pos.core.startup.AbstractStartupTask;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(100)
public class DatabaseStartupTask extends AbstractStartupTask {

    @Override
    protected void doTask() throws Exception {
        
    }

}
