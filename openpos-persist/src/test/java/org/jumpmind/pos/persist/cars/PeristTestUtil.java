package org.jumpmind.pos.persist.cars;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PeristTestUtil {

    public static Properties getH2TestProperties() {
        Properties props = new Properties();
        String dbDir = System.getProperty("user.dir") + "/build/open-pos-test";
 
        props.setProperty("db.url", "jdbc:h2:mem:opeb-pos-test;LOCK_TIMEOUT=60000");
        props.setProperty("db.user", "");
        props.setProperty("db.password", "");
        props.setProperty("db.driver", "org.h2.Driver");

        return props;
    }
    
    public static Map<String, String> getSessionContext() {
        Map<String, String> sessionContext = new HashMap<>();
        sessionContext.put("CREATE_BY", "unit-test");
        sessionContext.put("LAST_UPDATE_BY", "unit-test");
        return sessionContext;
    }
}

    
