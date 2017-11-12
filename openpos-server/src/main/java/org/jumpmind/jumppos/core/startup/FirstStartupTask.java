package org.jumpmind.jumppos.core.startup;

import static java.lang.Integer.MIN_VALUE;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(MIN_VALUE)
public class FirstStartupTask extends AbstractStartupTask {

    @Override
    protected void doTask() throws Exception {
    }
    
}
