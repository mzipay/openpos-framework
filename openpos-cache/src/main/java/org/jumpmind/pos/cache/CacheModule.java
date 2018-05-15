package org.jumpmind.pos.cache;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.h2.tools.Server;
import org.jumpmind.db.platform.IDatabasePlatform;
import org.jumpmind.persist.IPersistenceManager;
import org.jumpmind.pos.persist.DBSessionFactory;
import org.jumpmind.pos.service.Module;
import org.jumpmind.properties.TypedProperties;
import org.jumpmind.security.ISecurityService;
import org.jumpmind.security.SecurityServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration("CacheModule")
@EnableTransactionManagement
public class CacheModule implements Module {
    
    @Autowired
    Environment env;

    TypedProperties environmentProperties;

    IPersistenceManager persistenceManager;
    
    IDatabasePlatform databasePlatform;

    BasicDataSource dataSource;

    Server h2Server;
    
    ISecurityService securityService;     
    
    PlatformTransactionManager txManager;    
    
    private static Logger log = Logger.getLogger(CacheModule.class);

    private DBSessionFactory sessionFactory;

    public String getName() {
        return "cache";
    }

    public String getVersion() {
        return "0.0.1";
    }

    public void setSessionFactory(DBSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public String getTablePrefix() {
        return "cfg";
    }
    
    protected List<Class<?>> getClassesForPackageAndAnnotation(String packageName, Class<? extends Annotation> annotation) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(true);
        scanner.addIncludeFilter(new AnnotationTypeFilter(annotation));
        for (BeanDefinition bd : scanner.findCandidateComponents(packageName)) {
            try {
                final Class<?> clazz = Class.forName(bd.getBeanClassName());
                classes.add(clazz);
            } catch (ClassNotFoundException ex) {
                log.error(ex.getMessage());
            }
        }    
        return classes;
    }    
    
    // @Bean
    // @Scope(value = "singleton")
    // Server h2Server() {
    //     String configDbUrl = env.getProperty(DB_POOL_URL, "jdbc:h2:mem:cache");
    //     if (h2Server == null && configDbUrl.contains("h2:tcp")) {
    //         try {
    //             h2Server = Server.createTcpServer("-tcpPort", env.getProperty("h2.port", "9092"));
    //             h2Server.start();
    //         } catch (SQLException e) {
    //             throw new SqlException(e);
    //         }
    //     }
    //     return h2Server;
    // }
//
//    @Bean
//    @Scope(value = "singleton")
//    TypedProperties environmentProperties() {
//        if (environmentProperties == null) {
//            environmentProperties = new TypedProperties();
//            MutablePropertySources propSrcs = ((AbstractEnvironment) env).getPropertySources();
//            StreamSupport.stream(propSrcs.spliterator(), false).filter(ps -> ps instanceof EnumerablePropertySource)
//                    .map(ps -> ((EnumerablePropertySource<?>) ps).getPropertyNames()).flatMap(Arrays::<String> stream)
//                    .forEach(propName -> environmentProperties.setProperty(propName, env.getProperty(propName)));
//        }
//        return environmentProperties;
//    }
//    
//    @Bean
//    @Scope(value = "singleton")
//    public PlatformTransactionManager txManager() {
//        if (txManager == null) {
//            this.txManager = new DataSourceTransactionManager(dataSource());
//        } 
//        return txManager;
//    }
//    
//    @Bean
//    @Scope(value = "singleton", proxyMode = ScopedProxyMode.INTERFACES)
//    public IDatabasePlatform databasePlatform() {
//        if (databasePlatform == null) {
//            databasePlatform = JdbcDatabasePlatformFactory.createNewPlatformInstance(dataSource(), new SqlTemplateSettings(), false, false);
//        }
//        return databasePlatform;
//    }
//        
//    @Bean
//    @Scope(value = "singleton")
//    public IPersistenceManager persistenceManager() {
//        if (persistenceManager == null) {
//            persistenceManager = new SqlPersistenceManager(databasePlatform());
//        }
//        return persistenceManager;
//    }    
    
    @Bean
    @Scope(value = "singleton")
    ISecurityService securityService() {
        if (this.securityService == null) {
            this.securityService = SecurityServiceFactory.create();
        }
        return this.securityService;
    }    

//    @Bean
//    @Scope(value = "singleton")
//    @Qualifier("cacheDataSource")
//    BasicDataSource dataSource() {
//        if (dataSource == null) {
//            h2Server();
//            TypedProperties properties = new TypedProperties();
//            properties.put(DB_POOL_DRIVER, env.getProperty(DB_POOL_DRIVER, Driver.class.getName()));
//            properties.put(DB_POOL_URL, env.getProperty(DB_POOL_URL, "jdbc:h2:mem:cache"));
//            properties.put(DB_POOL_USER, env.getProperty(DB_POOL_USER));
//            properties.put(DB_POOL_PASSWORD, env.getProperty(DB_POOL_PASSWORD));
//            properties.put(DB_POOL_INITIAL_SIZE, env.getProperty(DB_POOL_INITIAL_SIZE, "20"));
//            properties.put(DB_POOL_MAX_ACTIVE, env.getProperty(DB_POOL_MAX_ACTIVE, "20"));
//            properties.put(DB_POOL_MAX_IDLE, env.getProperty(DB_POOL_MAX_IDLE, "20"));
//            properties.put(DB_POOL_MIN_IDLE, env.getProperty(DB_POOL_MIN_IDLE, "20"));
//            properties.put(DB_POOL_MAX_WAIT, env.getProperty(DB_POOL_MAX_WAIT, "30000"));
//            properties.put(DB_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS, env.getProperty(DB_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS, "120000"));
//            properties.put(DB_POOL_VALIDATION_QUERY, env.getProperty(DB_POOL_VALIDATION_QUERY));
//            properties.put(DB_POOL_TEST_ON_BORROW, env.getProperty(DB_POOL_TEST_ON_BORROW, "false"));
//            properties.put(DB_POOL_TEST_ON_RETURN, env.getProperty(DB_POOL_TEST_ON_RETURN, "false"));
//            properties.put(DB_POOL_TEST_WHILE_IDLE, env.getProperty(DB_POOL_TEST_WHILE_IDLE, "true"));
//            properties.put(DB_POOL_INIT_SQL, env.getProperty(DB_POOL_INIT_SQL));
//            properties.put(DB_POOL_CONNECTION_PROPERTIES, env.getProperty(DB_POOL_CONNECTION_PROPERTIES));
//            log.info(String.format(
//                    "About to initialize the 'cache' module datasource using the following driver:"
//                            + " '%s' and the following url: '%s' and the following user: '%s'",
//                    properties.get(DB_POOL_DRIVER), properties.get(DB_POOL_URL), properties.get(DB_POOL_USER)));
//
//            dataSource = BasicDataSourceFactory.create(properties, securityService());
//        }
//        return dataSource;
//    }
    
//    @Bean(name="cacheSessionFactory")
//    public DBSessionFactory getSessionFactory() {
//        
//        sessionFactory = new DBSessionFactory();
//        
//        String packageName = this.getClass().getPackage().getName();
//        
//        List<Class<?>> tableClasses = 
//                getClassesForPackageAndAnnotation(packageName, Table.class);
//
//        Map<String, String> sessionContext = new HashMap<>();
//        
//        sessionContext.put("module.tablePrefix", getTablePrefix());
//        sessionContext.put("CREATE_BY", "openpos-cache");
//        sessionContext.put("LAST_UPDATE_BY", "openpos-cache");        
//        
//        // init sessionFactory per this module. 
//        sessionFactory.init(
//                databasePlatform(), 
//                sessionContext, 
//                tableClasses);
//        
//        return sessionFactory;
//    }
//
//    @Bean("cacheDbSession")
//    public DBSession getSession() {
//        return getSessionFactory().createDbSession();
//    }    
}
