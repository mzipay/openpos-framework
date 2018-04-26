package org.jumpmind.pos.persist.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jumpmind.db.model.Column;
import org.jumpmind.db.model.Database;
import org.jumpmind.db.model.Table;
import org.jumpmind.db.platform.IDatabasePlatform;
import org.jumpmind.db.platform.IDdlBuilder;
import org.jumpmind.db.sql.SqlScript;
import org.jumpmind.pos.persist.Extends;
import org.jumpmind.pos.persist.PersistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DatabaseSchema {

    protected static final Logger log = LoggerFactory.getLogger(DatabaseSchema.class);

    private IDatabasePlatform platform;
    private List<Class<?>> entityClasses;
    private List<Class<?>> entityExtensionClasses;
    private Map<Class<?>, EntityMetaData> classMetadata = new HashMap<>();
    private Database db;
    private String tablePrefix;

    public void init(String tablePrefix, IDatabasePlatform platform, List<Class<?>> entityClasses, List<Class<?>> entityExtensionClasses) {
        this.tablePrefix = tablePrefix;
        this.platform = platform;
        this.entityClasses = entityClasses;
        this.entityExtensionClasses = entityExtensionClasses;
        db = new Database();
        loadTables(db);
        loadExtensions(db);
    }   
    
    public Table getTable(Class<?> entityClass) {
        if (entityClass == null) {
            throw new PersistException("Cannot lookup a table for a null entity class.");
        }
        
        EntityMetaData meta = classMetadata.get(entityClass);
        if (meta != null) {
            return meta.getTable();
        } else {
            return null;
        }
    }
    
    public boolean createAndUpgrade() {
        try {
            log.info("Checking if database tables need created or altered");
            Database desiredModel = db;

            platform.prefixDatabase(tablePrefix, desiredModel);
            Database actualModel = platform.readFromDatabase(desiredModel.getTables());

            IDdlBuilder builder = platform.getDdlBuilder();
            if (builder.isAlterDatabase(actualModel, desiredModel)) {
                log.info("There are database tables that needed altered");
                String delimiter = platform.getDatabaseInfo().getSqlCommandDelimiter();
                String alterSql = builder.alterDatabase(actualModel, desiredModel);
                log.info("SQL generated:\r\n{}", alterSql);

                SqlScript script = new SqlScript(alterSql, platform.getSqlTemplate(), true, false, false, delimiter, null);
                //TODO: add sql listener
                // if (logOutput) {
                // script.setListener(new LogSqlResultsListener(log));
                // }
                script.execute(platform.getDatabaseInfo().isRequiresAutoCommitForDdl());
                log.info("Finished updating tables.");
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

    protected void loadTables(Database db) {        
        for (Class<?> entityClass : entityClasses) {
            EntityMetaData meta = createMetaData(entityClass);
            validateTable(meta.getTable());
            classMetadata.put(entityClass, meta);
            db.addTable(meta.getTable());
        }
    }

    protected void loadExtensions(Database db) {
        for (Class<?> extensionClass : entityExtensionClasses) {
            Extends extendsAnnotation = extensionClass.getAnnotation(Extends.class);
            Class<?> baseClass = extendsAnnotation.entityClass();
            EntityMetaData meta = classMetadata.get(baseClass);
            if (meta == null) {
                throw new PersistException("Failed to process extension entity " + extensionClass + 
                        " Could not find table mapped for base entity class: " + baseClass);
            }
            Field[] fields = extensionClass.getDeclaredFields();
            for (Field field : fields) {
                meta.getTable().addColumn(createColumn(field));
            }                
        }
    }

    protected void validateTable(Table table) {
        String tableName = tablePrefix + "_" + table.getName();
        validateName(tableName, "table");
        boolean hasPk = false;
        for (Column column : table.getColumns()) {
            validateName(column.getName(), "column");
            if (column.isPrimaryKey()) {
                hasPk = true;
            }
        }
        if (!hasPk) {
            throw new PersistException(
                    String.format("Table '%s' must define at least 1 primary key field with @Column(primaryKey=true)", tableName));
        }
    }

    protected void validateName(String name, String type) {
        final int ORACLE_MAX_NAME_LENGTH = 30;
        if (name.length() == 0) {
            throw new PersistException(String.format("Invalid %s name \"%s\". The name cannot be blank.", type, name));
        }
        else if (name.length() > ORACLE_MAX_NAME_LENGTH) {
            throw new PersistException(String.format("Invalid %s name \"%s\". Must be 30 characeters or less.", type, name));
        } else if (ReservedWords.isReserved(name)) {
            throw new PersistException(String.format("Invalid %s name \"%s\". This is a reserved word. Try making the name more specific.", type, name));
        } else if (StringUtils.containsWhitespace(name)) {
            throw new PersistException(String.format("Invalid %s name \"%s\".  The name contains whitespace.", type, name));
        }        
    }

//    protected Database readDatabaseFromClasses(String basePackageName, String extensionPackageName) {
//        Database db = new Database();
//        addTablesToDatabase(db, basePackageName);
//        updateDatabaseWithExtensions(db, extensionPackageName);        
//        return db;
//    }

//    protected void updateDatabaseWithExtensions(Database db, String extensionPackageName) {
//        addTablesToDatabase(db, extensionPackageName);
//        updateTablesWithExtensions(db, extensionPackageName);
//    }

//    protected void updateTablesWithExtensions(Database db, String extensionPackageName) {
//        for (Class<?> clazz : getClassesForPackageAndAnnotation(extensionPackageName, Extends.class)) {
//            Extends extendsAnnotation = clazz.getAnnotation(Extends.class);
//            Class<?> extendedClass = extendsAnnotation.clazz();
//            //TODO: enhance for multiple tiers/levels of @Extends without parent being @Table
//            org.jumpmind.pos.persist.Table tblAnnotation = extendedClass.getAnnotation(org.jumpmind.pos.persist.Table.class);
//            extendTable(db.findTable(tblAnnotation.name()), clazz);
//        }
//    }

    protected void extendTable(Table dbTable, Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field:fields) {
            dbTable.addColumn(createColumn(field));
        }               
    }

//    protected Set<Class<?>> getClassesForPackageAndAnnotation(String packageName, Class<? extends Annotation> annotation) {
//        Set<Class<?>> classes = new HashSet<Class<?>>();
//        ClassPathScanningCandidateComponentProvider scanner =
//                new ClassPathScanningCandidateComponentProvider(true);
//        scanner.addIncludeFilter(new AnnotationTypeFilter(annotation));
//        for (BeanDefinition bd : scanner.findCandidateComponents(packageName)) {
//            try {
//                final Class<?> clazz = Class.forName(bd.getBeanClassName());
//                classes.add(clazz);
//            } catch (ClassNotFoundException ex) {
//                log.error(ex.getMessage());
//            }
//        }    
//        return classes;
//    }
//
//    protected Database addTablesToDatabase(Database db, String packageName) {
//        for (Class<?> clazz : getClassesForPackageAndAnnotation(packageName, org.jumpmind.pos.persist.Table.class)) {
//            db.addTable(createTable(clazz));
//        }
//        return db;        
//    }

    protected EntityMetaData createMetaData(Class<?> entityClass) {
        
        EntityMetaData meta = new EntityMetaData();
        Table dbTable = new Table();
        org.jumpmind.pos.persist.Table tblAnnotation = entityClass.getAnnotation(org.jumpmind.pos.persist.Table.class);
        if (!StringUtils.isEmpty(tblAnnotation.name())) {            
            dbTable.setName(tblAnnotation.name());
        } else {
            dbTable.setName(camelToSnakeCase(entityClass.getSimpleName()));
        }
        dbTable.setDescription(tblAnnotation.description());        

        Class<?> currentClass = entityClass; 
        while (currentClass != null && currentClass != Object.class) {            
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                Column column = createColumn(field);
                if (column != null) {
                    dbTable.addColumn(column);
                    if (isPrimaryKey(field)) {
                        meta.getEntityIdFields().add(field);
                    }
                }
            }       
            currentClass = currentClass.getSuperclass();
        }
        
        meta.setTable(dbTable);
        return meta;
    }
    
    private boolean isPrimaryKey(Field field) {
        if (field != null) {            
            org.jumpmind.pos.persist.Column colAnnotation = field.getAnnotation(org.jumpmind.pos.persist.Column.class);
            if (colAnnotation != null) {
                return colAnnotation.primaryKey();
            }
        }
        
        return false;
    }    

    protected Column createColumn(Field field) {        
        Column dbCol = null;
        org.jumpmind.pos.persist.Column colAnnotation = field.getAnnotation(org.jumpmind.pos.persist.Column.class);
        if (colAnnotation != null) {
            dbCol = new Column();
            
            if (!StringUtils.isEmpty(colAnnotation.name())) {                
                dbCol.setName(colAnnotation.name());
            } else {
                dbCol.setName(camelToSnakeCase(field.getName()));
            }
            
            dbCol.setDescription(colAnnotation.description());
            if (colAnnotation.type() == Types.OTHER) {
                dbCol.setTypeCode(getDefaultType(field));
            } else {
                dbCol.setTypeCode(colAnnotation.type());
            }
            
            if (colAnnotation.size() != null & !colAnnotation.size().equalsIgnoreCase("")) {
                dbCol.setSize(colAnnotation.size());
            } else {
                dbCol.setSize(getDefaultSize(field, dbCol));
            }
            dbCol.setPrimaryKey(colAnnotation.primaryKey());
            
            if (colAnnotation.primaryKey()) {
                dbCol.setRequired(true);
            } else {                
                dbCol.setRequired(colAnnotation.required());
            }
        }        
        return dbCol;
    }
    
    public Map<String, String> getEntityIdColumnsToFields(Class<?> entityClass) {
        Map<String, String> entityIdColumnsToFields = new LinkedHashMap<>();
        List<Field> fields = gettEntityIdFields(entityClass);
        for (Field field : fields) {
            org.jumpmind.pos.persist.Column colAnnotation = field.getAnnotation(org.jumpmind.pos.persist.Column.class);
            if (colAnnotation != null) {
                String columnName = null;
                if (!StringUtils.isEmpty(colAnnotation.name())) {                
                    columnName = colAnnotation.name();
                } else {
                    columnName = camelToSnakeCase(field.getName());
                } 
                entityIdColumnsToFields.put(columnName, field.getName());
            }
        }
        
        return entityIdColumnsToFields;
    }
    
    public List<Field> gettEntityIdFields(Class<?> entityClass) {
        EntityMetaData meta = classMetadata.get(entityClass);
        List<Field> fields = meta.getEntityIdFields();
        return fields;
    }

    protected String getDefaultSize(Field field, Column column) {
        if (column.getMappedTypeCode() == Types.VARCHAR) {
            return "128";
        } else if (column.getJdbcTypeCode() == Types.DECIMAL) {
            return "12";
        }
        return null;
    }
    
    private int getDefaultType(Field field) {
        if (field.getType().isAssignableFrom(String.class)) {
            return Types.VARCHAR;
        } else if (field.getType().isAssignableFrom(long.class)
                || field.getType().isAssignableFrom(Long.class)) {
            return Types.BIGINT;
        } else if (field.getType().isAssignableFrom(int.class)
                || field.getType().isAssignableFrom(Integer.class)) {
            return Types.INTEGER;
        } else if (field.getType().isAssignableFrom(boolean.class)
                || field.getType().isAssignableFrom(Boolean.class)) {
            return Types.BOOLEAN;
        } else if (field.getType().isAssignableFrom(Date.class)) {
            return Types.TIMESTAMP;
        } else if (field.getType().isAssignableFrom(BigDecimal.class)) {
            return Types.DECIMAL;
        } else {
            return Types.OTHER;
        }
    }
    
    public static String camelToSnakeCase(String camelCase) {
        StringBuilder buff = new StringBuilder();
        int index = 0;
        for (char c : camelCase.toCharArray()) {
            if (Character.isUpperCase(c) && index > 0) {
                buff.append('_');
                buff.append(Character.toLowerCase(c));
            } else {
                buff.append(c);
            }
            index++;
        }
        
        return buff.toString();
    }
    
    protected void validatePrimaryKey(Table table) {
        
    }    
}
