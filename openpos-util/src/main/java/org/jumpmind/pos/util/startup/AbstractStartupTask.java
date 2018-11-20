package org.jumpmind.pos.util.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

abstract public class AbstractStartupTask implements ApplicationListener<ApplicationReadyEvent> {

    final protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    final public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("{} is executing ...", getClass().getSimpleName());
        try {
            doTask();
        } catch (Exception e) {
            logger.error("Failed to execute " + getClass().getName(), e);
        } finally {
            logger.info("{} is complete", getClass().getSimpleName());
        }
    }

    protected abstract void doTask() throws Exception;
}