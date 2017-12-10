package org.jumpmind.pos.translate.startup;

import org.jumpmind.pos.core.startup.AbstractStartupTask;
import org.jumpmind.pos.translate.ILegacyStartupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(100)
public class LegacyStartupTask extends AbstractStartupTask {

    @Autowired
    ILegacyStartupService headlessStartupService;

    @Override
    protected void doTask() throws Exception {
        headlessStartupService.startPreviouslyStarted();
    }
}
