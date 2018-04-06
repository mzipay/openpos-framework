package org.jumpmind.pos.user;

import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.persist.DBSessionFactory;
import org.jumpmind.pos.service.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Configuration
//@Qualifier("userModule")
public class UserModule implements Module {

    @Autowired
    @Qualifier("userSessionFactory")
    private DBSessionFactory sessionFactory = new DBSessionFactory();

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
        return null;
    }

    //    public DBSessionFactory getSessionFactory() {
    //        return sessionFactory;
    //    }
    //    
    @Bean
    public DBSession getSession() {
        return sessionFactory.createDbSession();
    }

    @Override
    public String getTablePrefix() {
        return "usr";
    }
}
