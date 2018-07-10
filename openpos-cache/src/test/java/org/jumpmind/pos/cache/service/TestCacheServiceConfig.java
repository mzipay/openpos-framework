package org.jumpmind.pos.cache.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
        basePackages = { "org.jumpmind.pos.cache" })
public class TestCacheServiceConfig {

//    protected DBSessionFactory sessionFactory;
//    InputStream queryYamlStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("persist-test-query.yaml");
//    QueryTemplates queryTemplates = new Yaml(new Constructor(QueryTemplates.class)).load(queryYamlStream);    
//
//    @Bean
//    public DBSessionFactory sessionFactory() {
//        if (sessionFactory == null) {
//            sessionFactory = new DBSessionFactory();
//
//            Map<String, String> sessionContext = new HashMap<>();
//            sessionContext.put("module.tablePrefix", "tst");
//            sessionContext.put("CREATE_BY", "openpos-test");
//            sessionContext.put("LAST_UPDATE_BY", "openpos-test");
//            
//            sessionFactory.setDatabaseSchema(new DatabaseSchema());
//            sessionFactory.init(
//                    PersistTestUtil.testDbPlatform(), 
//                    PersistTestUtil.getSessionContext(), 
//                    Arrays.asList(CarEntity.class, CarStats.class, ServiceInvoice.class), 
//                    queryTemplates,
//                    DBSessionFactory.getDmlTemplates("persist-test"));
//            
//
//            DBSession session = sessionFactory.createDbSession();
//
//            updateDataModel(session);
//
//        }
//
//        return sessionFactory;
//    }
    
//    public void updateDataModel(DBSession session) {
//        String fromVersion = null;
//
//        DatabaseScriptContainer scripts = new DatabaseScriptContainer("persist-test/sql", PersistTestUtil.testDbPlatform());
//
//        scripts.executePreInstallScripts(fromVersion, "0.0.1");
//
//        sessionFactory.getDatabaseSchema().createAndUpgrade();
//
//        scripts.executePostInstallScripts(fromVersion, "0.0.1");
//    }


}
