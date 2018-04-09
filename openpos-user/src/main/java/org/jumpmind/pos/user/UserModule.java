package org.jumpmind.pos.user;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.persist.DBSessionFactory;
import org.jumpmind.pos.persist.Table;
import org.jumpmind.pos.persist.cars.PersistTestUtil;
import org.jumpmind.pos.service.Module;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

@Configuration
public class UserModule implements Module {
    
    private static Logger log = Logger.getLogger(UserModule.class);

    private DBSessionFactory sessionFactory;

    public String getName() {
        return "user";
    }

    public String getVersion() {
        return "0.0.1";
    }

    public void setSessionFactory(DBSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Bean(name="userSessionFactory")
    public DBSessionFactory getSessionFactory() {
        
        sessionFactory = new DBSessionFactory();
        
        String packageName = this.getClass().getPackage().getName();
        
        List<Class<?>> tableClasses = 
                getClassesForPackageAndAnnotation(packageName, Table.class);

        Map<String, String> sessionContext = PersistTestUtil.getSessionContext();
        
        sessionContext.put("module.tablePrefix", getTablePrefix());
        
        // init sessionFactory per this module. 
        sessionFactory.init(
                PersistTestUtil.getH2TestProperties(), 
                sessionContext, 
                tableClasses,
                PersistTestUtil.getQueryTempaltes(getTablePrefix())); // TODO
        
        return sessionFactory;
    }

    @Bean("userDbSession")
    public DBSession getSession() {
        return sessionFactory.createDbSession();
    }

    @Override
    public String getTablePrefix() {
        return "usr";
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
}
