package org.jumpmind.pos.persist.impl;

import java.util.ArrayList;
import java.util.List;

public class QueryTemplate {

    private String name;
    private List<String> selects = new ArrayList<>();
    private List<String> optionalWhereClauses = new ArrayList<>();
    private String groupBy;
    private String where;
    private String orderBy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setSelect(String select) {
        this.selects.add(select);
    }
    
    public List<String> getSelects() {
        return selects;
    }

    public void setSelects(List<String> select) {
        this.selects = select;
    }

    public List<String> getOptionalWhereClauses() {
        return optionalWhereClauses;
    }

    public void setOptionalWhereClauses(List<String> optionalWhereClauses) {
        this.optionalWhereClauses = optionalWhereClauses;
    }

    public QueryTemplate optionalWhere(String optionalWhere) {
        this.optionalWhereClauses.add(optionalWhere);
        return this;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
