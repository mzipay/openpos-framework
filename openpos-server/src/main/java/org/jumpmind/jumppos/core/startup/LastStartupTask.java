package org.jumpmind.jumppos.core.startup;

import static java.lang.Integer.MAX_VALUE;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(MAX_VALUE)
public class LastStartupTask extends AbstractStartupTask {    
    
    @Autowired
    MutableBoolean initialized;

    @Override
    protected void doTask() throws Exception {
        initialized.setTrue();
    }
    
}
