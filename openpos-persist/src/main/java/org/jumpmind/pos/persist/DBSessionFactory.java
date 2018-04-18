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
        
        databaseSchema.createAndUpgrade();
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
    
//    protected IDatabasePlatform createDatabasePlatform(Properties properties) {
//        IDatabasePlatform platform = createDatabasePlatform(null, properties, null);
//        return platform;
//    }    
//    
//    public static IDatabasePlatform createDatabasePlatform(ApplicationContext springContext, Properties dbProperties,
//            DataSource dataSource) {
//        TypedProperties properties = new TypedProperties(dbProperties);
//        
//        logger.info("Initializing connection to database");
//        if (dataSource == null) {
//            String jndiName = properties.getProperty("db.jndi.name");
//            if (StringUtils.isNotBlank(jndiName)) {
//                try {
//                    logger.info(String.format("Looking up datasource in jndi.  The jndi name is \"%s\"", jndiName));
//                    JndiObjectFactoryBean jndiFactory = new JndiObjectFactoryBean();
//                    jndiFactory.setJndiName(jndiName);
//                    jndiFactory.afterPropertiesSet();
//                    dataSource = (DataSource)jndiFactory.getObject();
//
//                    if (dataSource == null) {
//                        throw new PersistException("Could not locate the configured datasource in jndi.  The jndi name is \"" +  jndiName + "\n");
//                    }
//                } catch (Exception ex) {
//                    throw new PersistException("Could not locate the configured datasource in jndi.  The jndi name is \"" + jndiName, ex);
//                }
//            }
//            
//            String springBeanName = properties.getProperty("db.spring.bean.name");
//            if (StringUtils.isNotBlank(springBeanName) && springContext != null) {
//                logger.info(String.format("Using datasource from spring.  The spring bean name is %s", springBeanName));
//                dataSource = (DataSource)springContext.getBean(springBeanName);
//            }
//            
//            if (dataSource == null) {
//                dataSource = BasicDataSourceFactory.create(properties, SecurityServiceFactory.create(SecurityServiceType.CLIENT, properties));
//            }
//        }
////        if (waitOnAvailableDatabase) {
////            waitForAvailableDatabase(dataSource);
////        }
//        boolean delimitedIdentifierMode = 
//                StringUtils.equals(properties.getProperty("StringUtils.equals(properties.getProperty(\"db.metadata.ignore.case\", \"true\"), \"true\");", "true"), "true");
//        boolean caseSensitive = 
//                StringUtils.equals(properties.getProperty("db.metadata.ignore.case", "true"), "true");
//        return JdbcDatabasePlatformFactory.createNewPlatformInstance(dataSource,
//                createSqlTemplateSettings(properties), delimitedIdentifierMode, caseSensitive);
//    }   
//    
//    protected static SqlTemplateSettings createSqlTemplateSettings(TypedProperties properties) {
//        SqlTemplateSettings settings = new SqlTemplateSettings();
//        settings.setFetchSize(properties.getInt("db.jdbc.streaming.results.fetch.size", 1000));
//        settings.setQueryTimeout(properties.getInt("db.sql.query.timeout.seconds", 300));
//        settings.setBatchSize(properties.getInt("db.jdbc.execute.batch.size", 100));
//        settings.setOverrideIsolationLevel(properties.getInt("db.jdbc.isolation.level", -1));
////        settings.setReadStringsAsBytes(properties.is(ParameterConstants.JDBC_READ_STRINGS_AS_BYTES, false));
////        settings.setTreatBinaryAsLob(properties.is(ParameterConstants.TREAT_BINARY_AS_LOB_ENABLED, true));
//        LogSqlBuilder logSqlBuilder = new LogSqlBuilder();
//        logSqlBuilder.setLogSlowSqlThresholdMillis(properties.getInt("log.slow.sql.threshold.millis", 20000));
//        logSqlBuilder.setLogSqlParametersInline(properties.is("log.sql.parameters.inline", true));
//        settings.setLogSqlBuilder(logSqlBuilder);
//        
//        if (settings.getOverrideIsolationLevel() >=0 ) {
//            logger.info("Overriding isolation level to " + settings.getOverrideIsolationLevel());
//        }
//        return settings;
//    }

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
