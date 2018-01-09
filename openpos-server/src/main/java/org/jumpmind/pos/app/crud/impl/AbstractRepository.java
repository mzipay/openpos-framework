package org.jumpmind.pos.app.crud.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.jumpmind.db.model.Column;
import org.jumpmind.db.model.Table;
import org.jumpmind.db.platform.IDatabasePlatform;
import org.jumpmind.db.sql.DmlStatement;
import org.jumpmind.db.sql.DmlStatement.DmlType;
import org.jumpmind.db.sql.JdbcSqlTemplate;
import org.jumpmind.db.sql.Row;
import org.jumpmind.db.sql.SqlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

abstract public class AbstractRepository {

    @Autowired
    protected IDatabasePlatform databasePlatform;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected <T> T map(Map<String, Object> row, Class<T> clazz, String catalogName, String schemaName, String tableName) {
        try {
            T object = clazz.newInstance();
            Table table = findTable(catalogName, schemaName, tableName);
            LinkedHashMap<String, Column> objectToTableMapping = mapObjectToTable(object, table);
            Set<String> propertyNames = objectToTableMapping.keySet();
            for (String propertyName : propertyNames) {
                Object value = row.get(objectToTableMapping.get(propertyName).getName());
                BeanUtils.copyProperty(object, propertyName, value);
            }
            return object;
        } catch (Exception e) {
            throw toRuntimeException(e);
        }
    }

    /**
     * @return true if the object was created, false if the object was updated
     */

    protected boolean save(Object object) {
        return save(object, null, null, camelCaseToUnderScores(object.getClass().getSimpleName()));
    }

    /**
     * @return true if the object was created, false if the object was updated
     */

    protected boolean save(Object object, String catalogName, String schemaName, String tableName) {
        if (update(object, catalogName, schemaName, tableName) == 0) {
            insert(object, catalogName, schemaName, tableName);
            return true;
        } else {
            return false;
        }
    }

    protected int update(Object object, String catalogName, String schemaName, String tableName) {
        return excecuteDml(DmlType.UPDATE, object, catalogName, schemaName, tableName);
    }

    protected void insert(Object object, String catalogName, String schemaName, String tableName) {
        excecuteDml(DmlType.INSERT, object, catalogName, schemaName, tableName);
    }

    protected boolean delete(Object object) {
        return delete(object, null, null, camelCaseToUnderScores(object.getClass().getSimpleName()));
    }

    protected boolean delete(Object object, String catalogName, String schemaName, String tableName) {
        return excecuteDml(DmlType.DELETE, object, catalogName, schemaName, tableName) > 0;
    }

    protected <T> List<T> find(Class<T> clazz) {
        return find(clazz, null, null, camelCaseToUnderScores(clazz.getSimpleName()));
    }

    protected <T> List<T> find(Class<T> clazz, Map<String, Object> conditions) {
        return find(clazz, conditions, null, null, camelCaseToUnderScores(clazz.getSimpleName()));
    }

    protected <T> int count(Class<T> clazz, Map<String, Object> conditions) {
        return count(clazz, null, null, camelCaseToUnderScores(clazz.getSimpleName()), conditions);
    }

    protected <T> int count(Class<T> clazz, String catalogName, String schemaName, String tableName, Map<String, Object> conditions) {
        if (conditions == null || conditions.size() == 0) {
            return count(catalogName, schemaName, tableName);
        } else {
            try {
                Table table = findTable(catalogName, schemaName, tableName);

                T object = clazz.newInstance();

                LinkedHashMap<String, Column> objectToTableMapping = mapObjectToTable(object, table);
                LinkedHashMap<String, Object> objectValuesByColumnName = new LinkedHashMap<String, Object>();

                Column[] keys = new Column[conditions.size()];

                Set<String> keyPropertyNames = conditions.keySet();
                boolean[] nullKeyValues = new boolean[conditions.size()];
                int index = 0;
                for (String propertyName : keyPropertyNames) {
                    Column column = objectToTableMapping.get(propertyName);
                    if (column != null) {
                        keys[index] = column;
                        nullKeyValues[index] = conditions.get(propertyName) == null;
                        objectValuesByColumnName.put(column.getName(), conditions.get(propertyName));
                        index++;
                    } else {
                        throw new IllegalStateException("Could not find a database column that maps to the " + propertyName + " property on "
                                + clazz.getName() + ".  Make sure the property is defined on the class and "
                                + "the matching column is defined in the database table");
                    }
                }

                DmlStatement statement = databasePlatform.createDmlStatement(DmlType.COUNT, table.getCatalog(), table.getSchema(),
                        table.getName(), keys, keys, nullKeyValues, null);
                String sql = statement.getSql();
                Object[] values = statement.getValueArray(objectValuesByColumnName);

                return jdbcTemplate.queryForObject(sql, values, Integer.class);
            } catch (Exception e) {
                throw toRuntimeException(e);
            }
        }

    }

    protected int count(String catalogName, String schemaName, String tableName) {
        try {
            Table table = findTable(catalogName, schemaName, tableName);
            DmlStatement statement = databasePlatform.createDmlStatement(DmlType.COUNT, table.getCatalog(), table.getSchema(),
                    table.getName(), null, null, null, null);
            String sql = statement.getSql();
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            throw toRuntimeException(e);
        }
    }

    protected <T> List<T> find(Class<T> clazz, Map<String, Object> conditions, String catalogName, String schemaName, String tableName) {
        if (conditions == null || conditions.size() == 0) {
            return find(clazz, catalogName, schemaName, tableName);
        } else {
            try {
                Table table = findTable(catalogName, schemaName, tableName);

                T object = clazz.newInstance();

                LinkedHashMap<String, Column> objectToTableMapping = mapObjectToTable(object, table);
                LinkedHashMap<String, Object> objectValuesByColumnName = new LinkedHashMap<String, Object>();

                Column[] keys = new Column[conditions.size()];

                Set<String> keyPropertyNames = conditions.keySet();
                boolean[] nullKeyValues = new boolean[conditions.size()];
                int index = 0;
                for (String propertyName : keyPropertyNames) {
                    Column column = objectToTableMapping.get(propertyName);
                    if (column != null) {
                        keys[index] = column;
                        nullKeyValues[index] = conditions.get(propertyName) == null;
                        objectValuesByColumnName.put(column.getName(), conditions.get(propertyName));
                        index++;
                    } else {
                        throw new IllegalStateException("Could not find a database column that maps to the " + propertyName + " property on "
                                + clazz.getName() + ".  Make sure the property is defined on the class and "
                                + "the matching column is defined in the database table");
                    }
                }

                Column[] columns = objectToTableMapping.values().toArray(new Column[objectToTableMapping.size()]);

                DmlStatement statement = databasePlatform.createDmlStatement(DmlType.SELECT, table.getCatalog(), table.getSchema(),
                        table.getName(), keys, columns, nullKeyValues, null);
                String sql = statement.getSql();
                Object[] values = statement.getValueArray(objectValuesByColumnName);
                int[] types = statement.getTypes();

                List<Row> rows = jdbcTemplate.query(sql, values, types, new Mapper());
                List<T> objects = new ArrayList<T>();

                for (Row row : rows) {
                    object = clazz.newInstance();
                    Set<String> propertyNames = objectToTableMapping.keySet();
                    for (String propertyName : propertyNames) {
                        Object value = row.get(objectToTableMapping.get(propertyName).getName());
                        BeanUtils.copyProperty(object, propertyName, value);
                    }
                    objects.add(object);
                }

                return objects;
            } catch (Exception e) {
                throw toRuntimeException(e);
            }
        }

    }

    protected <T> List<T> find(Class<T> clazz, String catalogName, String schemaName, String tableName) {
        try {
            Table table = findTable(catalogName, schemaName, tableName);

            T object = clazz.newInstance();

            LinkedHashMap<String, Column> objectToTableMapping = mapObjectToTable(object, table);

            Column[] columns = objectToTableMapping.values().toArray(new Column[objectToTableMapping.size()]);

            DmlStatement statement = databasePlatform.createDmlStatement(DmlType.SELECT_ALL, table.getCatalog(), table.getSchema(),
                    table.getName(), null, columns, null, null);
            String sql = statement.getSql();

            List<Row> rows = jdbcTemplate.query(sql, new Mapper());
            List<T> objects = new ArrayList<T>();

            for (Row row : rows) {
                object = clazz.newInstance();
                Set<String> propertyNames = objectToTableMapping.keySet();
                for (String propertyName : propertyNames) {
                    Object value = row.get(objectToTableMapping.get(propertyName).getName());
                    BeanUtils.copyProperty(object, propertyName, value);
                }
                objects.add(object);
            }

            return objects;
        } catch (Exception e) {
            throw toRuntimeException(e);
        }
    }

    protected void refresh(Object object, String catalogName, String schemaName, String tableName) {
        try {
            Table table = findTable(catalogName, schemaName, tableName);

            LinkedHashMap<String, Column> objectToTableMapping = mapObjectToTable(object, table);
            LinkedHashMap<String, Object> objectValuesByColumnName = getObjectValuesByColumnName(object, objectToTableMapping);

            Column[] columns = objectToTableMapping.values().toArray(new Column[objectToTableMapping.size()]);
            List<Column> keys = new ArrayList<Column>(1);
            for (Column column : columns) {
                if (column.isPrimaryKey()) {
                    keys.add(column);
                }
            }

            DmlStatement statement = databasePlatform.createDmlStatement(DmlType.SELECT, table.getCatalog(), table.getSchema(),
                    table.getName(), keys.toArray(new Column[keys.size()]), columns, null, null);
            String sql = statement.getSql();
            Object[] values = statement.getValueArray(objectValuesByColumnName);

            Row row = jdbcTemplate.queryForObject(sql, values, new Mapper());

            if (row != null) {
                Set<String> propertyNames = objectToTableMapping.keySet();
                for (String propertyName : propertyNames) {
                    Object value = row.get(objectToTableMapping.get(propertyName).getName());
                    BeanUtils.copyProperty(object, propertyName, value);
                }
            }
        } catch (Exception e) {
            throw toRuntimeException(e);
        }

    }

    protected int excecuteDml(DmlType type, Object object, String catalogName, String schemaName, String tableName) {
        Table table = findTable(catalogName, schemaName, tableName);

        LinkedHashMap<String, Column> objectToTableMapping = mapObjectToTable(object, table);
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

    private Table findTable(String catalogName, String schemaName, String tableName) {
        Table table = databasePlatform.getTableFromCache(catalogName, schemaName, tableName, false);
        if (table == null) {
            throw new SqlException("Could not find table " + Table.getFullyQualifiedTableName(catalogName, schemaName, tableName));
        } else {
            return table;
        }
    }

    protected LinkedHashMap<String, Object> getObjectValuesByColumnName(Object object, LinkedHashMap<String, Column> objectToTableMapping) {
        try {
            LinkedHashMap<String, Object> objectValuesByColumnName = new LinkedHashMap<String, Object>();
            Set<String> propertyNames = objectToTableMapping.keySet();
            for (String propertyName : propertyNames) {
                objectValuesByColumnName.put(objectToTableMapping.get(propertyName).getName(),
                        PropertyUtils.getProperty(object, propertyName));
            }
            return objectValuesByColumnName;
        } catch (IllegalAccessException e) {
            throw toRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw toRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw toRuntimeException(e);
        }
    }

    protected LinkedHashMap<String, Column> mapObjectToTable(Object object, Table table) {
        LinkedHashMap<String, Column> columnNames = new LinkedHashMap<String, Column>();
        PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(object);
        for (int i = 0; i < pds.length; i++) {
            String propName = pds[i].getName();
            Column column = table.getColumnWithName(camelCaseToUnderScores(propName));
            if (column != null) {
                columnNames.put(propName, column);
            }
        }
        return columnNames;
    }

    protected String camelCaseToUnderScores(String camelCaseName) {
        StringBuilder underscoredName = new StringBuilder();
        for (int p = 0; p < camelCaseName.length(); p++) {
            char c = camelCaseName.charAt(p);
            if (p > 0 && Character.isUpperCase(c)) {
                underscoredName.append("_");
            }
            underscoredName.append(Character.toLowerCase(c));

        }
        return underscoredName.toString();
    }

    protected RuntimeException toRuntimeException(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }

    class Mapper implements RowMapper<Row> {

        ResultSetMetaData rsMetaData = null;
        int rsColumnCount;

        @Override
        public Row mapRow(ResultSet rs, int rowNum) throws SQLException {

            if (rsMetaData == null) {
                rsMetaData = rs.getMetaData();
                rsColumnCount = rsMetaData.getColumnCount();
            }

            Row mapOfColValues = new Row(rsColumnCount);
            for (int i = 1; i <= rsColumnCount; i++) {
                String key = JdbcSqlTemplate.lookupColumnName(rsMetaData, i);
                Object obj = JdbcSqlTemplate.getResultSetValue(rs, rsMetaData, i, false);
                mapOfColValues.put(key, obj);
            }
            return null;
        }
    }

}
