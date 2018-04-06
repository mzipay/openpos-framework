package org.jumpmind.pos.service;

import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.persist.DBSessionFactory;

public interface Module {

    public String getName();
    public String getVersion();
    public default void start() {
    }
    
//    public DBSessionFactory getSessionFactory();
    public void setSessionFactory(DBSessionFactory sessionFactory);
//    
//    public DBSession getSession();
    
    public String getTablePrefix();
    
}
