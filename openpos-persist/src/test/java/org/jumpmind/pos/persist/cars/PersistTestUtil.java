package org.jumpmind.pos.persist.cars;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jumpmind.pos.persist.PersistException;
import org.jumpmind.pos.persist.driver.Driver;
import org.jumpmind.pos.persist.impl.QueryTemplates;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class PersistTestUtil {
    
    private static Logger log = Logger.getLogger(PersistTestUtil.class);

    public static Properties getH2TestProperties() {
        Properties props = new Properties();
        String dbDir = System.getProperty("user.dir") + "/build/open-pos-test";
        
        try {
            Class.forName(Driver.class.getName());
        } catch (ClassNotFoundException ex) {
            log.error("Could not load Driver", ex);
        }
 
        props.setProperty("db.url", "jdbc:openpos:h2:mem:open-pos-test;LOCK_TIMEOUT=60000;DB_CLOSE_DELAY=-1");
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
    
    public static QueryTemplates getQueryTempaltes(String tablePrefix) {
        try {            
            URL url = Thread.currentThread().getContextClassLoader().getResource(tablePrefix + "-query.yaml");
            if (url != null) {
                log.info(String.format("Loading %s...", url.toString()));
                InputStream queryYamlStream = url.openStream();
                QueryTemplates queryTemplates = new Yaml(new Constructor(QueryTemplates.class)).load(queryYamlStream);
                return queryTemplates;
            } else {
                log.info("Could not locate "  + tablePrefix + "-query.yaml on the classpath.");
                return new QueryTemplates();
            }
        } catch (Exception ex) {
            throw new PersistException("Failed to load query.yaml", ex);
        }
    }
}

    
