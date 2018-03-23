package org.jumpmind.pos.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.jumpmind.db.model.Column;
import org.jumpmind.db.model.Database;
import org.jumpmind.db.model.Table;
import org.jumpmind.db.platform.IDatabasePlatform;
import org.jumpmind.db.platform.IDdlBuilder;
import org.jumpmind.db.sql.SqlScript;
import org.jumpmind.pos.app.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class DatabaseManager {
    
    protected static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    IDatabasePlatform platform;
    final static String DB_MODEL_PACKAGE = "org.jumpmind.pos.db.model";
    final static String DB_MODEL_EXTENSION_PACKAGE = "org.jumpmind.pos.db.model.extension";
    
    public DatabaseManager(IDatabasePlatform platform) {
            this.platform = platform;
    }
    
    protected Database readDatabaseFromClasses(String basePackageName, String extensionPackageName) {
        Database db = new Database();
        addTablesToDatabase(db, basePackageName);
        updateDatabaseWithExtensions(db, extensionPackageName);        
        return db;
    }
    
    protected void updateDatabaseWithExtensions(Database db, String extensionPackageName) {
        addTablesToDatabase(db, extensionPackageName);
        updateTablesWithExtensions(db, extensionPackageName);
    }
    
    protected void updateTablesWithExtensions(Database db, String extensionPackageName) {
        for (Class<?> clazz : getClassesForPackageAndAnnotation(extensionPackageName, Extends.class)) {
            Extends extendsAnnotation = clazz.getAnnotation(Extends.class);
            Class<?> extendedClass = extendsAnnotation.clazz();
            //TODO: enhance for multiple tiers/levels of @Extends without parent being @Table
            org.jumpmind.pos.db.Table tblAnnotation = extendedClass.getAnnotation(org.jumpmind.pos.db.Table.class);
            extendTable(db.findTable(tblAnnotation.name()), clazz);
        }
    }
    
    protected void extendTable(Table dbTable, Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field:fields) {
            dbTable.addColumn(createColumn(field));
        }               
    }
    
    protected Set<Class<?>> getClassesForPackageAndAnnotation(String packageName, Class<? extends Annotation> annotation) {
        Set<Class<?>> classes = new HashSet<Class<?>>();
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
    
    protected Database addTablesToDatabase(Database db, String packageName) {
        for (Class<?> clazz : getClassesForPackageAndAnnotation(packageName, org.jumpmind.pos.db.Table.class)) {
            db.addTable(createTable(clazz));
        }
        return db;        
    }
    
    protected Table createTable(Class<?> clazz) {
        Table dbTable = new Table();
        org.jumpmind.pos.db.Table tblAnnotation = clazz.getAnnotation(org.jumpmind.pos.db.Table.class);
        dbTable.setName(tblAnnotation.name());
        dbTable.setDescription(tblAnnotation.description());        
        
        Field[] fields = clazz.getDeclaredFields();
        for (Field field:fields) {
            dbTable.addColumn(createColumn(field));
        }       
        return dbTable;
    }
    
    protected Column createColumn(Field field) {        
        Column dbCol = new Column();
        org.jumpmind.pos.db.Column colAnnotation = field.getAnnotation(org.jumpmind.pos.db.Column.class);
        if (colAnnotation != null) {
            dbCol.setName(colAnnotation.name());
            dbCol.setDescription(colAnnotation.description());
            dbCol.setJdbcTypeCode(colAnnotation.type().getVendorTypeNumber());
            dbCol.setJdbcTypeName(colAnnotation.type().getName());
            if (colAnnotation.size() != null & !colAnnotation.size().equalsIgnoreCase("")) {
                dbCol.setSize(colAnnotation.size());
            }
            dbCol.setPrimaryKey(colAnnotation.primaryKey());
            dbCol.setRequired(colAnnotation.required());
        }        
        return dbCol;
    }
    
    public boolean createAndUpgrade(String tablePrefix) {
        try {
            log.info("Checking if config tables need created or altered");
            Database modelFromSource = this.readDatabaseFromClasses(DB_MODEL_PACKAGE, DB_MODEL_EXTENSION_PACKAGE);
            
            platform.prefixDatabase(tablePrefix, modelFromSource);
            Database modelFromDatabase = platform.readFromDatabase(modelFromSource.getTables());

            IDdlBuilder builder = platform.getDdlBuilder();
            if (builder.isAlterDatabase(modelFromDatabase, modelFromSource)) {
                log.info("There are config tables that needed altered");
                String delimiter = platform.getDatabaseInfo().getSqlCommandDelimiter();
                String alterSql = builder.alterDatabase(modelFromDatabase, modelFromSource);
                log.debug("Alter SQL generated: {}", alterSql);

                SqlScript script = new SqlScript(alterSql, platform.getSqlTemplate(), true, false, false, delimiter, null);
                //TODO: add sql listener
                // if (logOutput) {
                // script.setListener(new LogSqlResultsListener(log));
                // }
                script.execute(platform.getDatabaseInfo().isRequiresAutoCommitForDdl());
                log.info("Done with auto update of config tables");
                return true;
            } else {
                return false;
            }
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }    
}
