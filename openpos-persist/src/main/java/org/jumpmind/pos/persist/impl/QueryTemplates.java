package org.jumpmind.pos.persist.impl;

import org.apache.commons.lang3.StringUtils;
import org.jumpmind.db.sql.InvalidSqlException;
import org.jumpmind.pos.persist.PersistException;
import org.springframework.data.mapping.PersistentPropertyAccessor;

import java.text.ParseException;
import java.util.*;

public class QueryTemplates {
    Map<String, Map<String, QueryTemplate>> queriesByDeviceMode = new HashMap<>();

    public QueryTemplates()  {}

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

    public void setQueries(List<QueryTemplate> queries)  {
        setQueries("default", queries);
    }

    //  Lookups for queries by Device Mode and name.

    public boolean containsQueryTemplate(String deviceMode, String queryName)  {
        return getQueryTemplateMapForDeviceMode(deviceMode).containsKey(queryName);
    }

    public QueryTemplate getQueryTemplate(String deviceMode, String queryName)  {
        return getQueryTemplateMapForDeviceMode(deviceMode).get(queryName);
    }

    //  SQL parsing.

    public void replaceModelClassNamesWithTableNames(DatabaseSchema dbSchema, List<Class<?>> modelClassList, boolean validateTablesInQueries)  {
        for (String deviceMode : queriesByDeviceMode.keySet())  {
            Map<String, QueryTemplate> queryMap = queriesByDeviceMode.get(deviceMode);
            for (QueryTemplate template : queryMap.values())  {
                String select = template.getSelect();
                if (select != null)  {
                    for (Class<?> modelClass : modelClassList)  {
                        String modelClassName = modelClass.getSimpleName();

                        if (validateTablesInQueries)  {
                            String regularTableName = dbSchema.getTableForDeviceMode("default", modelClass, modelClass).getName();
                            String shadowTableName  = dbSchema.getTableForDeviceMode("training", modelClass, modelClass).getName();

                            boolean selectContainsRegularName = select.matches(".*\\b" + regularTableName + "\\b.*");
                            boolean selectContainsShadowName = false;
                            if (!regularTableName.equals(shadowTableName))  {
                                selectContainsShadowName = select.matches(".*\\b" + shadowTableName + "\\b.*");
                            }

                            if (selectContainsRegularName || selectContainsShadowName)  {
                                throw new InvalidSqlException(
                                    "SQL SELECT statement contains literal table name " + (selectContainsRegularName ? regularTableName : shadowTableName) +
                                    " in query " + template.getName() + " in file " + dbSchema.getTablePrefix().toLowerCase() +
                                    "-query.yml. Replace it with the corresponding model class name " + modelClassName + " instead."
                                );
                            }
                        }

                        if (StringUtils.indexOf(select, modelClassName) >= 0)  {
                            //  In the case below, we always want the model class, not its superclass.
                            select = select.replaceAll("\\b" + modelClassName + "\\b", dbSchema.getTableForDeviceMode(deviceMode, modelClass, modelClass).getName());
                            template.setSelect(select);
                        }
                    }
                }
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
        Map<String, QueryTemplate> queryMap = queriesByDeviceMode.get(deviceMode);
        return (queryMap == null ? new HashMap<>() : queryMap);
    }
}
