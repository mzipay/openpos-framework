package org.jumpmind.pos.app.startup;

import org.jumpmind.db.platform.IDatabasePlatform;
import org.jumpmind.pos.core.startup.AbstractStartupTask;
import org.jumpmind.pos.db.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(100)
public class DatabaseStartupTask extends AbstractStartupTask {
    
    final protected Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    IDatabasePlatform platform;

    @Autowired
    String tablePrefix;
    
    DatabaseManager dbMgr = null;

    @Override
    protected void doTask() throws Exception {

//        DatabaseScriptContainer dbUpgradeScripts = new DatabaseScriptContainer("/org/jumpmind/pos/app/upgrade", databasePlatform);
//        String fromVersion = infoService.getLastKnownVersion();
//        String toVersion = VersionUtils.getCurrentVersion();
//        if (fromVersion != null && !fromVersion.equals(toVersion)) {
//            dbUpgradeScripts.executePreInstallScripts(fromVersion, toVersion);
//        }
//        URL url = Thread.currentThread().getContextClassLoader().getResource("/org/jumpmind/pos/app/schema.xml");
//        try {
//            logger.info("Checking database schema per " + url);
//            new ConfigDatabaseUpgrader("/org/jumpmind/pos/app/schema.xml", databasePlatform, true, tablePrefix).upgrade();
//        } catch (Exception ex) {
//            throw new PosServerException("Failed to check schema per " + url, ex);
//        }
//   
        dbMgr = new DatabaseManager(platform);
        dbMgr.createAndUpgrade(tablePrefix);
        
    }
}
