package org.jumpmind.pos.context;

import org.jumpmind.pos.service.AbstractModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration("ContextModule")
@EnableTransactionManagement
public class ContextModule extends AbstractModule {

    @Override
    public String getName() {
        return "context";
    }

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public String getTablePrefix() {
        return "ctx";
    }
}
