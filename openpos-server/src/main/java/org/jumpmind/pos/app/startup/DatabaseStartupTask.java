package org.jumpmind.pos.app.startup;

import org.jumpmind.db.platform.IDatabasePlatform;
import org.jumpmind.db.util.ConfigDatabaseUpgrader;
import org.jumpmind.pos.core.startup.AbstractStartupTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(100)
public class DatabaseStartupTask extends AbstractStartupTask {

    @Autowired
    IDatabasePlatform databasePlatform;
    
    @Autowired
    String tablePrefix;
    
    @Override
    protected void doTask() throws Exception {
          new ConfigDatabaseUpgrader("/org/jumpmind/pos/app/schema.xml", databasePlatform, true, tablePrefix).upgrade();
    }

}
