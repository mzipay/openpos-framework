package org.jumpmind.jumppos.domain;

import org.hibernate.cfg.ImprovedNamingStrategy;

public class NamingStrategy extends ImprovedNamingStrategy {

    private static final long serialVersionUID = 1L;

    @Override
    public String foreignKeyColumnName(String propertyName, String propertyEntityName,
            String propertyTableName, String referencedColumnName) {
        return addUnderscoreId(super.foreignKeyColumnName(propertyName, propertyEntityName,
                propertyTableName, referencedColumnName));
    }

    @Override
    public String joinKeyColumnName(String joinedColumn, String joinedTable) {
        if (joinedColumn.equalsIgnoreCase("ID")) {
            return addUnderscoreId(tableName(joinedTable));
        } else {
            return addUnderscoreId(super.joinKeyColumnName(joinedColumn, joinedTable));
        }
    }

    protected String addUnderscoreId(String colName) {
        if (!colName.toUpperCase().endsWith("_ID")) {
            colName = colName + "_ID";
        }
        return colName;
    }
}
