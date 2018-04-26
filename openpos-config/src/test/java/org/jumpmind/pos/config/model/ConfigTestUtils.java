package org.jumpmind.pos.config.model;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.h2.tools.RunScript;
import org.jumpmind.db.platform.IDatabasePlatform;
import org.jumpmind.db.platform.JdbcDatabasePlatformFactory;
import org.jumpmind.db.sql.SqlTemplateSettings;
import org.jumpmind.db.util.BasicDataSourceFactory;
import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.persist.DBSessionFactory;
import org.jumpmind.pos.persist.driver.Driver;
import org.jumpmind.pos.persist.impl.DatabaseSchema;
import org.jumpmind.properties.TypedProperties;
import org.jumpmind.security.ISecurityService;
import org.jumpmind.security.SecurityServiceFactory;

public class ConfigTestUtils {
    
    private static Logger log = Logger.getLogger(ConfigTestUtils.class);
    
    public static Map<String, String> getTestNodeTags() {
        Map<String, String> tags = new HashMap<>();
        tags.put(ConfigRepository.TAG_REGION, "NAM");
        tags.put(ConfigRepository.TAG_COUNTRY, "US");
        tags.put(ConfigRepository.TAG_STATE, "OH");
        tags.put(ConfigRepository.TAG_STORE, "101");
        tags.put(ConfigRepository.TAG_NODE_ID, "101-1");
        tags.put(ConfigRepository.TAG_DEVICE_TYPE, "MOBILE");
        tags.put(ConfigRepository.TAG_STORE_TYPE, "REGULAR");
        tags.put(ConfigRepository.TAG_DEPARTMENT_ID, "FOOTWEAR");
        tags.put(ConfigRepository.TAG_BRAND_ID, "JUSTICE");
        return tags;
    }

    public static void initTestDB(ConfigRepository configRepository, String dataFilePath) throws Exception {
        
        DBSessionFactory sessionFactory = new DBSessionFactory();

        sessionFactory.setDatabaseSchema(new DatabaseSchema());
        sessionFactory.init(
                testDbPlatform(), 
                getSessionContext(), 
                Arrays.asList(ConfigModel.class),
                DBSessionFactory.getQueryTempaltes("cfg"));
        DBSession session = sessionFactory.createDbSession();
        
        File script = new File(ConfigTestUtils.class.getResource(dataFilePath).getFile());
        RunScript.execute(session.getConnection(), new FileReader(script));

        Field dbSessionField = configRepository.getClass().getDeclaredField("dbSession");
        dbSessionField.setAccessible(true);
        dbSessionField.set(configRepository, session);
    }
    
    public static IDatabasePlatform testDbPlatform() {
        return JdbcDatabasePlatformFactory.createNewPlatformInstance(testDataSource(), new SqlTemplateSettings(), false, false);
    }
    
    static ISecurityService securityService() {
        return SecurityServiceFactory.create();
    }      
    
    
    public static BasicDataSource testDataSource() {
        return BasicDataSourceFactory.create(getH2TestProperties(), securityService());
    }    

    static TypedProperties getH2TestProperties() {
        Properties props = new Properties();
        String dbDir = System.getProperty("user.dir") + "/build/open-pos-test";
        
        try {
            Class.forName(Driver.class.getName());
        } catch (ClassNotFoundException ex) {
            log.error("Could not load Driver", ex);
        }
 
        props.setProperty("db.url", "jdbc:openpos:h2:mem:open-pos-config-test;LOCK_TIMEOUT=60000;DB_CLOSE_DELAY=-1");
        props.setProperty("db.user", "");
        props.setProperty("db.password", "");
        props.setProperty("db.driver", "org.h2.Driver");

        return new TypedProperties(props);
    }
    
    public static Map<String, String> getSessionContext() {
        Map<String, String> sessionContext = new HashMap<>();
        sessionContext.put("module.tablePrefix", "cfg");
        sessionContext.put("CREATE_BY", "unit-test");
        sessionContext.put("LAST_UPDATE_BY", "unit-test");
        return sessionContext;
    }
    
    
    
}
