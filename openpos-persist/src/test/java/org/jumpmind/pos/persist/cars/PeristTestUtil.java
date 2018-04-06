package org.jumpmind.pos.persist.cars;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jumpmind.pos.persist.PersistException;
import org.jumpmind.pos.persist.impl.QueryTemplates;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

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
        sessionContext.put("module.tablePrefix", "car");
        sessionContext.put("CREATE_BY", "unit-test");
        sessionContext.put("LAST_UPDATE_BY", "unit-test");
        return sessionContext;
    }
    
    public static QueryTemplates getQueryTempaltes() {
        try {            
            InputStream queryYamlStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("query.yaml");
            QueryTemplates queryTemplates = new Yaml(new Constructor(QueryTemplates.class)).load(queryYamlStream);
            return queryTemplates;
        } catch (Exception ex) {
            throw new PersistException("Failed to load query.yaml", ex);
        }
    }
}

    
