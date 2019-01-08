package org.jumpmind.pos.persist;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.h2.tools.RunScript;
import org.jumpmind.db.model.Column;
import org.jumpmind.db.model.Table;
import org.jumpmind.db.platform.IDatabasePlatform;
import org.jumpmind.db.sql.DmlStatement;
import org.jumpmind.db.sql.DmlStatement.DmlType;
import org.jumpmind.db.sql.Row;
import org.jumpmind.pos.persist.impl.DatabaseSchema;
import org.jumpmind.pos.persist.impl.DefaultMapper;
import org.jumpmind.pos.persist.impl.DmlTemplate;
import org.jumpmind.pos.persist.impl.EntitySystemInfo;
import org.jumpmind.pos.persist.impl.QueryTemplate;
import org.jumpmind.pos.persist.impl.ReflectUtils;
import org.jumpmind.pos.persist.model.ITaggedModel;
import org.jumpmind.pos.persist.model.TagConfig;
import org.jumpmind.util.LinkedCaseInsensitiveMap;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

public class DBSession {

    private static final Logger log = Logger.getLogger(DBSession.class);

    private DatabaseSchema databaseSchema;
    private IDatabasePlatform databasePlatform;
    private JdbcTemplate jdbcTemplate;
    private Map<String, String> sessionContext;
    private Map<String, QueryTemplate> queryTemplates;
    private Map<String, DmlTemplate> dmlTemplates;

    public DBSession(String catalogName, String schemaName, DatabaseSchema databaseSchema, IDatabasePlatform databasePlatform,
            Map<String, String> sessionContext, Map<String, QueryTemplate> queryTemplates, Map<String, DmlTemplate> dmlTemplates,
            TagConfig tagConfig) {
        super();
        this.dmlTemplates = dmlTemplates;
        this.databaseSchema = databaseSchema;
        this.databasePlatform = databasePlatform;
        this.sessionContext = sessionContext;
        this.jdbcTemplate = new JdbcTemplate(databasePlatform.getDataSource());
        this.queryTemplates = queryTemplates;
    }

    public <T extends AbstractModel> List<T> findAll(Class<T> clazz) {
        QueryTemplate queryTemplate = new QueryTemplate();
        Query<T> query = new Query<T>().result(clazz);
        query.setQueryTemplate(queryTemplate);
        return query(query, new HashMap<>());
    }

    public <T extends AbstractModel> T findByNaturalId(Class<T> entityClass, String id) {
        Map<String, String> naturalColumnsToFields = databaseSchema.getEntityIdColumnsToFields(entityClass);

        if (naturalColumnsToFields.size() != 1) {
            throw new PersistException(String.format(
                    "findByNaturalId cannot be used with a single 'id' " + "param because the entity defines %s natural key fields.",
                    naturalColumnsToFields.size()));
        }

        ModelId entityId = new ModelId() {
            public Map<String, Object> getIdFields() {
                Map<String, Object> fields = new HashMap<>(1);
                String fieldName = naturalColumnsToFields.values().iterator().next();
                fields.put(fieldName, id);
                return fields;
            }
        };

        return findByNaturalId(entityClass, entityId);
    }

    public Connection getConnection() {
        DataSource datasource = databasePlatform.getDataSource();
        try {
            return datasource.getConnection();
        } catch (SQLException e) {
            throw new PersistException("Failed to get connection from databasePlatform " + databasePlatform);
        }
    }

    public <T extends AbstractModel> T findByNaturalId(Class<T> entityClass, ModelId id) {
        QueryTemplate queryTemplate = new QueryTemplate();
        queryTemplate.setSelect(getSelectSql(entityClass, id.getIdFields()));
        Query<T> query = new Query<T>().result(entityClass);
        query.setQueryTemplate(queryTemplate);
        List<T> results = query(query, id.getIdFields());

        if (results != null) {
            if (results.size() == 1) {
                return results.get(0);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * This method will use any field on the entity that is set for search
     * criteria in the database. It will ignore all audit fields. Be sure to
     * null out any fields that might be set by default.
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractModel> List<T> findByCriteria(T entity) {
        Map<String, Object> fieldValues = toParamMap(entity, false);
        List<T> list = (List<T>) findByFields(entity.getClass(), fieldValues);
        return list;
    }

    public Map<String, Object> toParamMap(AbstractModel entity, boolean includeNullValues) {
        Map<String, Object> params = new HashMap<>();
        Class<?> clazz = entity.getClass();
        while (!clazz.equals(AbstractModel.class)) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(ColumnDef.class)) {
                    try {
                        Object value = field.get(entity);
                        if (includeNullValues || value != null) {
                            params.put(field.getName(), value);
                        }
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            clazz = clazz.getSuperclass();
        }

        return params;
    }

    public <T extends AbstractModel> List<T> findByFields(Class<T> entityClass, Map<String, Object> fieldValues) {
        QueryTemplate queryTemplate = new QueryTemplate();
        queryTemplate.setSelect(getSelectSql(entityClass, fieldValues));
        Query<T> query = new Query<T>().result(entityClass);
        query.setQueryTemplate(queryTemplate);
        return query(query, fieldValues);
    }

    public void executeScript(File file) {
        try {
            executeScript(new FileReader(file));
        } catch (Exception ex) {
            if (ex instanceof PersistException) {
                throw (PersistException) ex;
            }
            throw new PersistException("Failed to execute script " + file, ex);
        }
    }

    public void executeScript(Reader reader) {
        try {
            RunScript.execute(getConnection(), reader);
        } catch (Exception ex) {
            throw new PersistException("Failed to execute script " + reader, ex);
        }
    }

    public int executeDml(String namedDml, Object... params) {
        DmlTemplate template = dmlTemplates.get(namedDml);
        if (template != null && isNotBlank(template.getDml())) {
            return jdbcTemplate.update(template.getDml(), params);
        } else {
            throw new PersistException("Could not find dml named: " + namedDml);
        }
    }

    public int executeSql(String sql, Object... params) {
        return jdbcTemplate.update(sql, params);
    }

    public <T extends AbstractModel> List<T> query(Query<T> query) {
        return query(query, (Map<String, Object>) null);
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractModel> List<T> query(Query<T> query, String singleParam) {
        populdateDefaultSelect(query);
        try {
            SqlStatement sqlStatement = query.generateSQL(singleParam);
            return (List<T>) queryInternal(query.getResultClass(), sqlStatement);
        } catch (Exception ex) {
            throw new PersistException("Failed to query target class " + query.getResultClass(), ex);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractModel> List<T> query(Query<T> query, Map<String, Object> params) {
        populdateDefaultSelect(query);
        try {
            SqlStatement sqlStatement = query.generateSQL(params);
            return (List<T>) queryInternal(query.getResultClass(), sqlStatement);
        } catch (Exception ex) {
            throw new PersistException("Failed to query result class " + query.getResultClass(), ex);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> void populdateDefaultSelect(Query<T> query) {
        boolean isEntityResult = AbstractModel.class.isAssignableFrom(query.getResultClass());
        if (queryTemplates.containsKey(query.getName())) {
            query.setQueryTemplate(queryTemplates.get(query.getName()).copy());
        }

        if (isEntityResult && query.getQueryTemplate().getSelect() == null) {
            Class<? extends AbstractModel> entityClass = (Class<? extends AbstractModel>) query.getResultClass();
            query.getQueryTemplate().setSelect(getSelectSql(entityClass, null));
        }
    }

    protected String getSelectSql(Class<?> entity, Map<String, Object> params) {
        List<Class<?>> toProcess = new ArrayList<>();
        Class<?> current = entity;
        /*
         * Add in reverse order because the superclass is going to have the
         * common denominator of primary keys
         */
        while (!current.equals(AbstractModel.class)) {
            if (current.isAnnotationPresent(TableDef.class)) {
                toProcess.add(0, current);
            }
            current = current.getSuperclass();
        }

        final String AND = " and ";
        final String JOIN = " join ";
        final String WHERE = " where ";
        final String COMMA = ", ";
        StringBuilder columns = new StringBuilder("select ");
        StringBuilder joins = new StringBuilder(" from ");
        StringBuilder where = new StringBuilder(WHERE);
        Set<String> columnsProcessed = new HashSet<>();
        int tableCount = 0;
        for (Class<?> processing : toProcess) {
            String tableAlias = "c" + tableCount;
            Table table = databaseSchema.getTable(entity, processing);
            joins.append(table.getName()).append(" ").append(tableAlias);
            if (tableCount > 0) {
                String previousTableAlias = "c" + (tableCount - 1);
                Column[] pks = table.getPrimaryKeyColumns();
                joins.append(" on ");
                for (Column pk : pks) {
                    joins.append(previousTableAlias).append(".").append(pk.getName().toLowerCase()).append("=").append(tableAlias)
                            .append(".").append(pk.getName().toLowerCase()).append(AND);
                }
                joins.replace(joins.length() - AND.length(), joins.length(), "");
            }
            joins.append(JOIN);

            Map<String, String> columnToFieldMapping = databaseSchema.getColumnsToEntityFields(processing);
            for (Column column : table.getColumns()) {
                if (!columnsProcessed.contains(column.getName())) {                   
                    columns.append(tableAlias).append(".").append(column.getName().toLowerCase()).append(COMMA);

                    String fieldName = columnToFieldMapping.get(column.getName());
                    if (params != null && params.containsKey(fieldName)) {
                        where.append(tableAlias).append(".").append(column.getName().toLowerCase()).append("=${").append(fieldName)
                                .append("}").append(AND);
                    }

                    columnsProcessed.add(column.getName());
                }
            }
            tableCount++;

        }

        if (where.length() > WHERE.length()) {
            where.replace(where.length() - AND.length(), where.length(), "");
        } else {
            where.setLength(0);
        }
        columns.replace(columns.length() - COMMA.length(), columns.length(), "");
        joins.replace(joins.length() - JOIN.length(), joins.length(), "");

        return columns.toString() + joins.toString() + where.toString();

    }

    protected LinkedHashMap<String, Column> mapObjectToTable(Class<?> resultClass, Table table) {
        LinkedHashMap<String, Column> columnNames = new LinkedHashMap<String, Column>();
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(resultClass);
        for (int i = 0; i < propertyDescriptors.length; i++) {
            String propName = propertyDescriptors[i].getName();
            Column column = table.getColumnWithName(DatabaseSchema.camelToSnakeCase(propName));
            if (column != null) {
                columnNames.put(propName, column);
            }
        }

        if (ITaggedModel.class.isAssignableFrom(resultClass)) {
            Column[] columns = table.getColumns();
            for (Column column : columns) {
                if (column.getName().toLowerCase().startsWith("tag_")) {
                    columnNames.put(column.getName(), column);
                }
            }
        }
        return columnNames;
    }

    public void save(AbstractModel entity) {
        List<Table> tables = getValidatedTables(entity);
        for (Table table : tables) {
            EntitySystemInfo entitySystemInfo = new EntitySystemInfo(entity);

            if (entitySystemInfo.isNew()) {
                try {
                    insert(entity, table);
                } catch (DuplicateKeyException ex) {
                    log.debug("Insert of entity failed, failing over to an update. " + entity, ex);
                    int updateCount = update(entity, table);
                    if (updateCount < 1) {
                        throw new PersistException("Failed to perform an insert or update on entity. Do the DB primary key and unique fields "
                                + "match what's understood by the code?  " + entity, ex);
                    }
                }
            } else {
                if (update(entity, table) == 0) {
                    insert(entity, table);
                }
            }
        }
    }

    protected void insert(AbstractModel entity, Table table) {
        if (StringUtils.isEmpty(entity.getCreateBy())) {
            entity.setCreateBy(sessionContext.get("CREATE_BY"));
        }
        if (StringUtils.isEmpty(entity.getLastUpdateBy())) {
            entity.setLastUpdateBy(sessionContext.get("LAST_UPDATE_BY"));
        }
        excecuteDml(DmlType.INSERT, entity, table);
    }

    protected int update(AbstractModel entity, Table table) {
        if (StringUtils.isEmpty(entity.getCreateBy())) {
            entity.setCreateBy(sessionContext.get("CREATE_BY"));
        }
        if (StringUtils.isEmpty(entity.getLastUpdateBy())) {
            entity.setCreateBy(sessionContext.get("LAST_UPDATE_BY"));
        }

        return excecuteDml(DmlType.UPDATE, entity, table);
    }

    protected int excecuteDml(DmlType type, Object object, org.jumpmind.db.model.Table table) {
        LinkedHashMap<String, Column> objectToTableMapping = mapObjectToTable(object.getClass(), table);
        LinkedHashMap<String, Object> objectValuesByColumnName = getObjectValuesByColumnName(object, objectToTableMapping);

        Column[] columns = objectToTableMapping.values().toArray(new Column[objectToTableMapping.size()]);
        List<Column> keys = new ArrayList<Column>(1);
        for (Column column : columns) {
            if (column.isPrimaryKey()) {
                keys.add(column);
            }
        }

        boolean[] nullKeyValues = new boolean[keys.size()];
        int i = 0;
        for (Column column : keys) {
            nullKeyValues[i++] = objectValuesByColumnName.get(column.getName()) == null;
        }

        DmlStatement statement = databasePlatform.createDmlStatement(type, table.getCatalog(), table.getSchema(), table.getName(),
                keys.toArray(new Column[keys.size()]), columns, nullKeyValues, null);
        String sql = statement.getSql();
        Object[] values = statement.getValueArray(objectValuesByColumnName);
        int[] types = statement.getTypes();

        return jdbcTemplate.update(sql, values, types);

    }

    protected LinkedHashMap<String, Object> getObjectValuesByColumnName(Object object, LinkedHashMap<String, Column> objectToTableMapping) {
        try {
            LinkedHashMap<String, Object> objectValuesByColumnName = new LinkedHashMap<String, Object>();
            Set<String> propertyNames = objectToTableMapping.keySet();
            for (String propertyName : propertyNames) {
                if (propertyName.toLowerCase().startsWith("tag_")) {
                    objectValuesByColumnName.put(propertyName, ((ITaggedModel) object).getTagValue(propertyName.substring("tag_".length())));
                } else {
                    objectValuesByColumnName.put(objectToTableMapping.get(propertyName).getName(),
                            PropertyUtils.getProperty(object, propertyName));
                }
            }
            return objectValuesByColumnName;
        } catch (Exception ex) {
            throw new PersistException(
                    "Failed to getObjectValuesByColumnName on object " + object + " objectToTableMapping: " + objectToTableMapping, ex);
        }
    }

    protected List<Table> getValidatedTables(AbstractModel entity) {
        if (entity == null) {
            throw new PersistException("Failed to locate a database table for null entity class");
        }
        return getValidatedTables(entity.getClass());
    }

    protected List<Table> getValidatedTables(Class<?> entityClass) {
        List<Table> tables = databaseSchema.getTables(entityClass);
        if (tables == null || tables.size() == 0) {
            throw new PersistException("Failed to locate a database table for entity class: '" + entityClass
                    + "'. Make sure the correct dbSession is used with the module by using the correct @Qualifier");
        }
        return tables;
    }

    protected <T extends AbstractModel> List<T> queryInternal(Class<T> resultClass, SqlStatement statement)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        List<T> objects = new ArrayList<T>();
        List<Row> rows = jdbcTemplate.query(statement.getSql(), new DefaultMapper(), statement.getValues().toArray());

        for (int j = 0; j < rows.size(); j++) {
            Row row = rows.get(j);
            T object = resultClass.newInstance();
            objects.add(object);

            PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(object);

            LinkedCaseInsensitiveMap<String> matchedColumns = new LinkedCaseInsensitiveMap<String>();

            for (int i = 0; i < propertyDescriptors.length; i++) {
                String propertyName = propertyDescriptors[i].getName();
                String columnName = DatabaseSchema.camelToSnakeCase(propertyName);

                if (row.containsKey(columnName)) {
                    Object value = row.get(columnName);
                    ReflectUtils.setProperty(object, propertyName, value);
                    matchedColumns.put(columnName, null);
                }
            }

            if (object instanceof AbstractModel) {
                addUnmatchedColumns(row, matchedColumns, (AbstractModel) object);
                decorateRetrievedEntity((AbstractModel) object);
            }

        }

        return objects;

    }

    private void addUnmatchedColumns(Row row, Map<String, String> matchedColumns, AbstractModel entity) {
        for (String rowColumn : row.keySet()) {
            if (!matchedColumns.containsKey(rowColumn)) {
                entity.setAdditionalField(rowColumn, row.get(rowColumn));
            }
        }
    }

    protected void decorateRetrievedEntity(AbstractModel entity) {
        EntitySystemInfo entitySystemInfo = new EntitySystemInfo(entity);
        entitySystemInfo.setRetrievalTime(new Date());
    }

    public void saveAll(List<? extends AbstractModel> entities) {
        for (AbstractModel entity : entities) {
            save(entity);
        }
    }

    public void close() {
        try {
            getConnection().close();
        } catch (Exception ex) {
            log.debug("Failed to close connection", ex);
        }
    }
}
