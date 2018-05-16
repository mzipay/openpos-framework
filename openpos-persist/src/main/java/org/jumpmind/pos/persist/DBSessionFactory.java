package org.jumpmind.pos.persist;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.jumpmind.db.platform.IDatabasePlatform;
import org.jumpmind.pos.persist.impl.DatabaseSchema;
import org.jumpmind.pos.persist.impl.QueryTemplate;
import org.jumpmind.pos.persist.impl.QueryTemplates;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

@Component
@Scope("prototype")
public class DBSessionFactory {
    
    private static Logger log = Logger.getLogger(DBSessionFactory.class);

    private DatabaseSchema databaseSchema;
    
    private Map<String, QueryTemplate> queryTemplates;
    
    private IDatabasePlatform databasePlatform;
    
    private Map<String, String> sessionContext;
    
    public void init(IDatabasePlatform databasePlatform, Map<String, String> sessionContext, 
            List<Class<?>> entities) {
        
        QueryTemplates queryTemplates = getQueryTempaltes(sessionContext.get("module.tablePrefix"));
        
        init(databasePlatform, sessionContext, entities, queryTemplates);
    }
    
    public void init(IDatabasePlatform databasePlatform, Map<String, String> sessionContext, 
            List<Class<?>> entities, QueryTemplates queryTemplatesObject) {
        
        this.databaseSchema = new DatabaseSchema();
        this.queryTemplates = buildQueryTemplatesMap(queryTemplatesObject);
        this.sessionContext = sessionContext;
        
        this.databasePlatform = databasePlatform;
        
        databaseSchema.init(sessionContext.get("module.tablePrefix"), 
                databasePlatform, 
                entities.stream().filter(e -> e.getAnnotation(Table.class) != null).collect(Collectors.toList()), 
                entities.stream().filter(e -> e.getAnnotation(Extends.class) != null).collect(Collectors.toList()));
        
    }

    public DBSession createDbSession() {
        return new DBSession(null, null, databaseSchema, databasePlatform, sessionContext, queryTemplates);
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

    public DatabaseSchema getDatabaseSchema() {
        return databaseSchema;
    }

    public void setDatabaseSchema(DatabaseSchema databaseSchema) {
        this.databaseSchema = databaseSchema;
    }
    
    protected Map<String, QueryTemplate> buildQueryTemplatesMap(QueryTemplates queryTemplates) {
        Map<String, QueryTemplate> queryTemplatesMap = new HashMap<>();
        if (queryTemplates != null) {            
            queryTemplates.getQueries().stream().forEach((q) -> queryTemplatesMap.put(q.getName(), q));
        }
        return queryTemplatesMap;
    }    
    
}
