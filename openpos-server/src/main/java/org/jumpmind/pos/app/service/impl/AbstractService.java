package org.jumpmind.pos.app.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.jumpmind.db.platform.IDatabasePlatform;
import org.jumpmind.persist.IPersistenceManager;
import org.jumpmind.security.ISecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractService {
    
    final protected Logger log = LoggerFactory.getLogger(getClass()); 

    @Autowired
    protected IPersistenceManager persistenceManager;
    
    @Autowired 
    protected BasicDataSource dataSource;
    
    @Autowired 
    protected IDatabasePlatform databasePlatform;
    
    @Autowired
    protected String tablePrefix;
    
    @Autowired
    protected ISecurityService securityService;

    AbstractService() {
    }
    
    protected String tableName(Class<?> clazz) {
        StringBuilder name = new StringBuilder(tablePrefix);
        int end = clazz.getSimpleName().indexOf("Name");
        if (end < 0) {
            end = clazz.getSimpleName().length();
        }
        String[] tokens = StringUtils.splitByCharacterTypeCamelCase(clazz.getSimpleName()
                .substring(0, end));
        for (String string : tokens) {
            name.append("_");
            name.append(string);
        }
        return name.toString();
    }
    
    protected <T> List<T> find(Class<T> clazzToMap, Map<String, Object> params, Class<?> tableClazz) {
        return persistenceManager.find(clazzToMap, params, null, null, tableName(tableClazz));
    }

    protected <T> List<T> find(Class<T> clazz, Map<String, Object> params) {
        return persistenceManager.find(clazz, params, null, null, tableName(clazz));
    }
    
    protected <T> int count(Class<T> clazz, Map<String, Object> params) {
        return persistenceManager.count(clazz, null, null, tableName(clazz), params);
    }

    protected <T> T findOne(Class<T> clazz, Map<String, Object> params) {
        List<T> all = persistenceManager.find(clazz, params, null, null, tableName(clazz));
        if (all.size() > 0) {
            return all.get(0);
        } else {
            return null;
        }
    }
    
    protected <T> T findOne(Class<T> clazzToMap, Map<String, Object> params, Class<?> tableNameClazz) {
        List<T> all = persistenceManager.find(clazzToMap, params, null, null, tableName(tableNameClazz));
        if (all.size() > 0) {
            return all.get(0);
        } else {
            return null;
        }
    }    

    public void delete(Object data) {
        persistenceManager.delete(data, null, null, tableName(data.getClass()));
    }

    protected void refresh(Object object) {
        persistenceManager.refresh(object, null, null, tableName(object
                .getClass()));
    }
    
    public void save(Object data) {
        //data.setLastUpdateTime(new Date());
        persistenceManager.save(data, null, null, tableName(data.getClass()));
    }
    
    protected void rethrow(Throwable error) {
        if (error instanceof RuntimeException) {
            throw (RuntimeException)error;
        } else if (error instanceof Error) {
            throw (Error)error;
        } else {
            throw new RuntimeException(error);
        }
    }   

}
