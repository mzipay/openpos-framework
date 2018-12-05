package org.jumpmind.pos.service;

import static org.jumpmind.db.util.BasicDataSourcePropertyConstants.DB_POOL_CONNECTION_PROPERTIES;
import static org.jumpmind.db.util.BasicDataSourcePropertyConstants.DB_POOL_DRIVER;
import static org.jumpmind.db.util.BasicDataSourcePropertyConstants.DB_POOL_INITIAL_SIZE;
import static org.jumpmind.db.util.BasicDataSourcePropertyConstants.DB_POOL_INIT_SQL;
import static org.jumpmind.db.util.BasicDataSourcePropertyConstants.DB_POOL_MAX_ACTIVE;
import static org.jumpmind.db.util.BasicDataSourcePropertyConstants.DB_POOL_MAX_IDLE;
import static org.jumpmind.db.util.BasicDataSourcePropertyConstants.DB_POOL_MAX_WAIT;
import static org.jumpmind.db.util.BasicDataSourcePropertyConstants.DB_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS;
import static org.jumpmind.db.util.BasicDataSourcePropertyConstants.DB_POOL_MIN_IDLE;
import static org.jumpmind.db.util.BasicDataSourcePropertyConstants.DB_POOL_PASSWORD;
import static org.jumpmind.db.util.BasicDataSourcePropertyConstants.DB_POOL_TEST_ON_BORROW;
import static org.jumpmind.db.util.BasicDataSourcePropertyConstants.DB_POOL_TEST_ON_RETURN;
import static org.jumpmind.db.util.BasicDataSourcePropertyConstants.DB_POOL_TEST_WHILE_IDLE;
import static org.jumpmind.db.util.BasicDataSourcePropertyConstants.DB_POOL_URL;
import static org.jumpmind.db.util.BasicDataSourcePropertyConstants.DB_POOL_USER;
import static org.jumpmind.db.util.BasicDataSourcePropertyConstants.DB_POOL_VALIDATION_QUERY;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.jumpmind.db.platform.IDatabasePlatform;
import org.jumpmind.db.platform.JdbcDatabasePlatformFactory;
import org.jumpmind.db.sql.SqlTemplateSettings;
import org.jumpmind.db.util.BasicDataSourceFactory;
import org.jumpmind.db.util.ConfigDatabaseUpgrader;
import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.persist.DBSessionFactory;
import org.jumpmind.pos.persist.DatabaseScriptContainer;
import org.jumpmind.pos.persist.PersistException;
import org.jumpmind.pos.persist.Table;
import org.jumpmind.pos.persist.driver.Driver;
import org.jumpmind.pos.persist.model.TagConfig;
import org.jumpmind.pos.service.model.ModuleModel;
import org.jumpmind.properties.TypedProperties;
import org.jumpmind.security.ISecurityService;
import org.jumpmind.security.SecurityServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
abstract public class AbstractModule extends AbstractServiceFactory implements Module {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected Environment env;

    @Value("${installation.id:undefined}")
    protected String installationId;

    @Autowired(required = false)
    protected TagConfig tagConfig;

    protected IDatabasePlatform databasePlatform;

    @Value("${openpos.module.datasource.bean.name:#{null}}")
    protected String dataSourceBeanName;
    
    protected BasicDataSource dataSource;

    protected ISecurityService securityService;

    protected PlatformTransactionManager txManager;

    protected DBSessionFactory sessionFactory;
    
    @Autowired
    private ApplicationContext applicationContext;

    protected List<Class<?>> getClassesForPackageAndAnnotation(String packageName, Class<? extends Annotation> annotation) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        classes.add(ModuleModel.class);
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);
        scanner.addIncludeFilter(new AnnotationTypeFilter(annotation));
        for (BeanDefinition bd : scanner.findCandidateComponents(packageName)) {
            try {
                final Class<?> clazz = Class.forName(bd.getBeanClassName());
                classes.add(clazz);
            } catch (ClassNotFoundException ex) {
                logger.error(ex.getMessage());
            }
        }
        return classes;
    }

    protected PlatformTransactionManager txManager() {
        if (txManager == null) {
            this.txManager = new DataSourceTransactionManager(dataSource());
        }
        return txManager;
    }

    protected IDatabasePlatform databasePlatform() {
        if (databasePlatform == null) {
            databasePlatform = JdbcDatabasePlatformFactory.createNewPlatformInstance(dataSource(), new SqlTemplateSettings(), false, false);
        }
        return databasePlatform;
    }

    protected ISecurityService securityService() {
        if (this.securityService == null) {
            this.securityService = SecurityServiceFactory.create();
        }
        return this.securityService;
    }

    @Override
    public String getDriver() {
        if (this.dataSourceBeanName != null) {
            BasicDataSource ds = this.dataSource();
            return ds.getDriverClassName();
        } else {
            return env.getProperty(DB_POOL_DRIVER, "org.h2.Driver");
        }
    }

    @Override
    public String getURL() {
        if (this.dataSourceBeanName != null) {
            BasicDataSource ds = this.dataSource();
            return ds.getUrl();
        } else {
            return env.getProperty(DB_POOL_URL, "jdbc:openpos:h2:mem:" + getName());
        }
    }

    protected BasicDataSource dataSource() {
        if (dataSource == null) {
            
            if (this.dataSourceBeanName != null) {
                try {
                    dataSource = this.applicationContext.getBean(this.dataSourceBeanName, BasicDataSource.class);
                    logger.info("Using dataSource bean '{}' for {} module dataSource", this.dataSourceBeanName, getName());
                } catch (Exception ex) {
                    logger.warn("Failed to load dataSource with name '{}', will load default dataSource instead. Reason: {}", 
                            this.dataSourceBeanName, ex.getMessage());
                }
            }
            
            if (dataSource == null ) {
                Driver.class.getName(); // Load openpos driver wrapper.
                TypedProperties properties = new TypedProperties();
                properties.put(DB_POOL_DRIVER, getDriver());
                properties.put(DB_POOL_URL, getURL());
                properties.put(DB_POOL_USER, env.getProperty(DB_POOL_USER));
                properties.put(DB_POOL_PASSWORD, env.getProperty(DB_POOL_PASSWORD));
                properties.put(DB_POOL_INITIAL_SIZE, env.getProperty(DB_POOL_INITIAL_SIZE, "20"));
                properties.put(DB_POOL_MAX_ACTIVE, env.getProperty(DB_POOL_MAX_ACTIVE, "20"));
                properties.put(DB_POOL_MAX_IDLE, env.getProperty(DB_POOL_MAX_IDLE, "20"));
                properties.put(DB_POOL_MIN_IDLE, env.getProperty(DB_POOL_MIN_IDLE, "20"));
                properties.put(DB_POOL_MAX_WAIT, env.getProperty(DB_POOL_MAX_WAIT, "30000"));
                properties.put(DB_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS, env.getProperty(DB_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS, "120000"));
                properties.put(DB_POOL_VALIDATION_QUERY, env.getProperty(DB_POOL_VALIDATION_QUERY));
                properties.put(DB_POOL_TEST_ON_BORROW, env.getProperty(DB_POOL_TEST_ON_BORROW, "false"));
                properties.put(DB_POOL_TEST_ON_RETURN, env.getProperty(DB_POOL_TEST_ON_RETURN, "false"));
                properties.put(DB_POOL_TEST_WHILE_IDLE, env.getProperty(DB_POOL_TEST_WHILE_IDLE, "true"));
                properties.put(DB_POOL_INIT_SQL, env.getProperty(DB_POOL_INIT_SQL));
                properties.put(DB_POOL_CONNECTION_PROPERTIES, env.getProperty(DB_POOL_CONNECTION_PROPERTIES));
                logger.info(String.format(
                        "About to initialize the '%s' module datasource using the following driver:"
                                + " '%s' and the following url: '%s' and the following user: '%s'",
                        getName(), properties.get(DB_POOL_DRIVER), properties.get(DB_POOL_URL), properties.get(DB_POOL_USER)));
    
                dataSource = BasicDataSourceFactory.create(properties, securityService());
            }
        }
        return dataSource;
    }

    protected DBSessionFactory sessionFactory() {
        if (sessionFactory == null) {
            Driver.register(null); // Load openpos driver wrapper.
            // Driver.class.getName();
            sessionFactory = new DBSessionFactory();

            String packageName = this.getClass().getPackage().getName();

            List<Class<?>> tableClasses = getClassesForPackageAndAnnotation(packageName, Table.class);

            Map<String, String> sessionContext = new HashMap<>();

            sessionContext.put("module.tablePrefix", getTablePrefix());
            sessionContext.put("CREATE_BY", "openpos-" + getName());
            sessionContext.put("LAST_UPDATE_BY", "openpos-" + getName());

            sessionFactory.init(databasePlatform(), sessionContext, tableClasses, tagConfig);

        }

        return sessionFactory;
    }

    @Override
    public void start() {
        updateDataModel(session());
    }

    public void updateDataModel(DBSession session) {
        String fromVersion = null;

        try {

            ModuleModel info = session.findByNaturalId(ModuleModel.class, installationId);
            if (info != null) {
                fromVersion = info.getCurrentVersion();
            }
        } catch (PersistException e) {
            logger.info("The module table is not available");
        }

        logger.info("The previous version of {} was {} and the current version is {}", getName(), fromVersion, getVersion());

        DatabaseScriptContainer scripts = new DatabaseScriptContainer(getName() + "/sql", databasePlatform());

        IDBSchemaListener schemaListener = getDbSchemaListener();

        scripts.executePreInstallScripts(fromVersion, getVersion());
        schemaListener.beforeSchemaCreate(sessionFactory);
        sessionFactory.createAndUpgrade();
        upgradeDbFromXml();
        schemaListener.afterSchemaCreate(sessionFactory);
        scripts.executePostInstallScripts(fromVersion, getVersion());

        session.save(new ModuleModel(installationId, getVersion()));
    }

    protected void upgradeDbFromXml() {
        String path = "/" + getName() + "-schema.xml";
        URL resource = getClass().getResource(path);
        if (resource != null) {
            ConfigDatabaseUpgrader databaseUpgrade = new ConfigDatabaseUpgrader(path, databasePlatform(), true, "");
            databaseUpgrade.upgrade();
        }
    }

    protected DBSession session() {
        return sessionFactory().createDbSession();
    }

    protected IDBSchemaListener getDbSchemaListener() {
        return new IDBSchemaListener() {
            @Override
            public void beforeSchemaCreate(DBSessionFactory sessionFactory) {
            }

            @Override
            public void afterSchemaCreate(DBSessionFactory sessionFactory) {
            }
        };
    }


}
