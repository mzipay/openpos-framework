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
import org.jumpmind.pos.persist.impl.DmlTemplate;
import org.jumpmind.pos.persist.impl.DmlTemplates;
import org.jumpmind.pos.persist.impl.QueryTemplate;
import org.jumpmind.pos.persist.impl.QueryTemplates;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class DBSessionFactory {

    private static Logger log = Logger.getLogger(DBSessionFactory.class);
    
    private DatabaseSchema databaseSchema;
    private Map<String, QueryTemplate> queryTemplates;
    private Map<String, DmlTemplate> dmlTemplates;
    private IDatabasePlatform databasePlatform;
    private Map<String, String> sessionContext;
    private List<Class<?>> entities;

    public void init(IDatabasePlatform databasePlatform, Map<String, String> sessionContext, List<Class<?>> entities) {

        QueryTemplates queryTemplates = getQueryTemplates(sessionContext.get("module.tablePrefix"));
        DmlTemplates dmlTemplates = getDmlTemplates(sessionContext.get("module.tablePrefix"));

        init(databasePlatform, sessionContext, entities, queryTemplates, dmlTemplates);
    }

    public void init(IDatabasePlatform databasePlatform, Map<String, String> sessionContext, List<Class<?>> entities,
            QueryTemplates queryTemplatesObject, DmlTemplates dmlTemplates) {

        this.queryTemplates = buildQueryTemplatesMap(queryTemplatesObject);
        this.dmlTemplates = buildDmlTemplatesMap(dmlTemplates);
        this.sessionContext = sessionContext;

        this.databasePlatform = databasePlatform;
        this.entities = entities;

        this.initSchema();
    }
    
    
    protected void initSchema() {
        this.databaseSchema = new DatabaseSchema();
        databaseSchema.init(sessionContext.get("module.tablePrefix"), databasePlatform,
                this.entities.stream().filter(e -> e.getAnnotation(Table.class) != null).collect(Collectors.toList()),
                this.entities.stream().filter(e -> e.getAnnotation(Extends.class) != null).collect(Collectors.toList()));
    }

    public void createAndUpgrade() {
        databaseSchema.createAndUpgrade();
    }

    public DBSession createDbSession() {
        return new DBSession(null, null, databaseSchema, databasePlatform, sessionContext, queryTemplates, dmlTemplates);
    }
    
    public org.jumpmind.db.model.Table getTableForEnhancement(Class<?> entityClazz) {
        List<org.jumpmind.db.model.Table> tables = this.databaseSchema.getTables(entityClazz);
        return tables != null && tables.size() > 0 ? tables.get(0) : null;
    }

    public static QueryTemplates getQueryTemplates(String tablePrefix) {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(tablePrefix + "-query.yaml");
            if (url != null) {
                log.info(String.format("Loading %s...", url.toString()));
                InputStream queryYamlStream = url.openStream();
                QueryTemplates queryTemplates = new Yaml(new Constructor(QueryTemplates.class)).load(queryYamlStream);
                return queryTemplates;
            } else {
                log.info("Could not locate " + tablePrefix + "-query.yaml on the classpath.");
                return new QueryTemplates();
            }
        } catch (Exception ex) {
            throw new PersistException("Failed to load " + tablePrefix + "-query.yaml", ex);
        }
    }
    
    public static DmlTemplates getDmlTemplates(String tablePrefix) {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(tablePrefix + "-dml.yaml");
            if (url != null) {
                log.info(String.format("Loading %s...", url.toString()));
                InputStream queryYamlStream = url.openStream();
                DmlTemplates queryTemplates = new Yaml(new Constructor(DmlTemplates.class)).load(queryYamlStream);
                return queryTemplates;
            } else {
                log.info("Could not locate " + tablePrefix + "-query.yaml on the classpath.");
                return new DmlTemplates();
            }
        } catch (Exception ex) {
            throw new PersistException("Failed to load " + tablePrefix + "-query.yaml", ex);
        }
    }

    protected Map<String, QueryTemplate> buildQueryTemplatesMap(QueryTemplates queryTemplates) {
        Map<String, QueryTemplate> queryTemplatesMap = new HashMap<>();
        if (queryTemplates != null) {
            queryTemplates.getQueries().stream().forEach((q) -> queryTemplatesMap.put(q.getName(), q));
        }
        return queryTemplatesMap;
    }
    
    protected Map<String, DmlTemplate> buildDmlTemplatesMap(DmlTemplates dmlTemplates) {
        Map<String, DmlTemplate> dmlTemplatesMap = new HashMap<>();
        if (dmlTemplates != null && dmlTemplates.getDmls() != null) {
            dmlTemplates.getDmls().stream().forEach((q) -> dmlTemplatesMap.put(q.getName(), q));
        }
        return dmlTemplatesMap;
    }

}
