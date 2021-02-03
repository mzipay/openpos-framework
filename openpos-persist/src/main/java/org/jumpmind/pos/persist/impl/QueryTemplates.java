package org.jumpmind.pos.persist.impl;

import org.apache.commons.lang3.StringUtils;
import org.jumpmind.db.model.Table;
import org.jumpmind.db.sql.InvalidSqlException;

import java.util.*;

public class QueryTemplates {
    Map<String, Map<String, QueryTemplate>> queriesByDeviceMode = new HashMap<>();

    public QueryTemplates() {
    }

    public List<QueryTemplate> getQueries(String deviceMode) {
        List<QueryTemplate> templateList = new ArrayList<>();
        templateList.addAll(getQueryTemplateMapForDeviceMode(deviceMode).values());
        return templateList;
    }

    public void setQueries(String deviceMode, List<QueryTemplate> queries) {
        getQueryTemplateMapForDeviceMode(deviceMode).clear();
        queriesByDeviceMode.put(deviceMode, buildQueryTemplatesByNameMap(queries));
    }

    public void addQueries(String deviceMode, List<QueryTemplate> queries) {
        queriesByDeviceMode.put(deviceMode, buildQueryTemplatesByNameMap(queries));
    }

    //  For backward compatibility...

    public List<QueryTemplate> getQueries() {
        return getQueries("default");
    }

    public void addQueries(List<QueryTemplate> queries) {
        addQueries("default", queries);
    }

    public void setQueries(List<QueryTemplate> queries) {
        setQueries("default", queries);
    }

    //  Lookups for queries by Device Mode and name.

    public boolean containsQueryTemplate(String deviceMode, String queryName) {
        return getQueryTemplateMapForDeviceMode(deviceMode).containsKey(queryName);
    }

    public QueryTemplate getQueryTemplate(String deviceMode, String queryName) {
        return getQueryTemplateMapForDeviceMode(deviceMode).get(queryName);
    }

    //  SQL parsing.

    public void replaceModelClassNamesWithTableNames(DatabaseSchema dbSchema, List<Class<?>> modelClassList, boolean validateTablesInQueries) {
        for (String deviceMode : queriesByDeviceMode.keySet()) {
            Map<String, QueryTemplate> queryMap = queriesByDeviceMode.get(deviceMode);
            for (QueryTemplate template : queryMap.values()) {
                for (Class<?> modelClass : modelClassList) {
                    String modelClassName = modelClass.getSimpleName();

                    Table regularTable = dbSchema.getTableForDeviceMode("default", modelClass, modelClass);
                    Table shadowTable = dbSchema.getTableForDeviceMode("training", modelClass, modelClass);

                    if (regularTable == null)  {
                        //  For whatever reason, we could not find a table associated with the given model class.
                        continue;

                    } else if (shadowTable == null)  {
                        shadowTable = regularTable;
                    }

                    String regularTableName = regularTable.getName();
                    String shadowTableName = shadowTable.getName();

                    if (validateTablesInQueries) {
                        scanSqlTextForLiteralTableName(template.getSelect(), "SELECT", template.getName(), regularTableName, shadowTableName, dbSchema.getTablePrefix(), modelClassName);
                        scanSqlTextForLiteralTableName(template.getWhere(), "WHERE", template.getName(), regularTableName, shadowTableName, dbSchema.getTablePrefix(), modelClassName);
                        scanSqlTextForLiteralTableName(template.getOrderBy(), "ORDER BY", template.getName(), regularTableName, shadowTableName, dbSchema.getTablePrefix(), modelClassName);
                        scanSqlTextForLiteralTableName(template.getGroupBy(), "GROUP BY", template.getName(), regularTableName, shadowTableName, dbSchema.getTablePrefix(), modelClassName);

                        for (String optionalWhereClause : template.getOptionalWhereClauses()) {
                            scanSqlTextForLiteralTableName(optionalWhereClause, "optional WHERE clause", template.getName(), regularTableName, shadowTableName, dbSchema.getTablePrefix(), modelClassName);
                        }
                    }

                    //  In the case below, we always want the model class, NOT its superclass.
                    String tableName = (deviceMode.equals("training") ? shadowTableName : regularTableName);

                    template.setSelect(replaceClassModelNameInSqlText(template.getSelect(), modelClassName, tableName));
                    template.setWhere(replaceClassModelNameInSqlText(template.getWhere(), modelClassName, tableName));
                    template.setOrderBy(replaceClassModelNameInSqlText(template.getOrderBy(), modelClassName, tableName));
                    template.setGroupBy(replaceClassModelNameInSqlText(template.getGroupBy(), modelClassName, tableName));

                    if (template.hasOptionalWhereClauses()) {
                        ArrayList<String> newOptionalWhereClauses = new ArrayList<>();
                        for (String optionalWhereClause : template.getOptionalWhereClauses()) {
                            newOptionalWhereClauses.add(replaceClassModelNameInSqlText(optionalWhereClause, modelClassName, tableName));
                        }
                        template.setOptionalWhereClauses(newOptionalWhereClauses);
                    }
                }
            }
        }
    }

    private String replaceClassModelNameInSqlText(String sqlText, String modelClassName, String tableName)  {
        if ((sqlText != null) && StringUtils.indexOf(sqlText, modelClassName) >= 0)  {
            return sqlText.replaceAll("\\b" + modelClassName + "\\b", tableName);
        }

        return sqlText;
    }

    private void scanSqlTextForLiteralTableName(String sqlText, String textType, String templateName, String regularTableName, String shadowTableName, String modulePrefix, String modelClassName)  {
        if (sqlText != null) {
            boolean textContainsRegularName = sqlText.matches(".*\\b" + regularTableName + "\\b.*");
            boolean textContainsShadowName = false;
            if (!regularTableName.equals(shadowTableName)) {
                textContainsShadowName = sqlText.matches(".*\\b" + shadowTableName + "\\b.*");
            }

            if (textContainsRegularName || textContainsShadowName) {
                throw new InvalidSqlException("SQL " + textType + " contains literal table name " +
                    (textContainsRegularName ? regularTableName : shadowTableName) + " in query " + templateName +
                    " in file " + modulePrefix.toLowerCase() + "-query.yml. Replace it with the corresponding model class name " +
                    modelClassName + " instead."
                );
            }
        }
    }

    //  Build our internal map of query names to the corresponding QueryTemplate.

    private Map<String, QueryTemplate> buildQueryTemplatesByNameMap(List<QueryTemplate> templateList) {
        Map<String, QueryTemplate> queryTemplatesMap = new HashMap<>();
        if (templateList != null) {
            for (QueryTemplate t : templateList)  {
                queryTemplatesMap.put(t.getName(), t.copy());
            }
        }
        return queryTemplatesMap;
    }

    private Map<String, QueryTemplate> getQueryTemplateMapForDeviceMode(String deviceMode)  {
        //  Normalize the Device Mode. Currently only "training" and "default" are supported.
        deviceMode = (deviceMode.equalsIgnoreCase("training") ? "training" : "default");
        Map<String, QueryTemplate> queryMap = queriesByDeviceMode.get(deviceMode);
        return (queryMap == null ? new HashMap<>() : queryMap);
    }
}