package org.jumpmind.pos.service;

import org.jumpmind.pos.persist.DBSessionFactory;

public interface Module {

    public String getName();
    public String getVersion();
    
    public default void start() {
    }
    
    public void setSessionFactory(DBSessionFactory sessionFactory);
    
    public String getTablePrefix();
    
}
