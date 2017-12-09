package org.jumpmind.pos.translate.startup;

import org.jumpmind.pos.core.startup.AbstractStartupTask;
import org.jumpmind.pos.translate.IHeadlessStartupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(100)
public class HeadlessStartupTask extends AbstractStartupTask {

    @Autowired
    IHeadlessStartupService headlessStartupService;

    @Override
    protected void doTask() throws Exception {
        headlessStartupService.startPreviouslyStarted();
    }
}
