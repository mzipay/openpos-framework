package org.jumpmind.jumppos.domain.param;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import org.jumpmind.jumppos.domain.BaseEntity;

@Entity
public class Parameter extends BaseEntity {

    public static final String BOOLEAN = "BOOLEAN";
    public static final String INTEGER = "INTEGER";
    public static final String LONG = "LONG";
    public static final String FLOAT = "FLOAT";
    public static final String DOUBLE = "DOUBLE";
    public static final String STRING = "STRING";
    public static final String LIST = "LIST";
    public static final String MAP = "MAP";

    public static final String GLOBAL = "GLOBAL";
    public static final String COUNTRY = "COUNTRY";
    public static final String COUNTRY_STATE = "COUNTRY_STATE";
    public static final String STORE = "STORE";
    public static final String STORE_WORKSTATION = "STORE_WORKSTATION";
    public static final String BRAND_ANY = "ANY";

    @Id
    private ParameterId id = null;

    @ManyToOne
    @JoinColumns( {
            @JoinColumn(referencedColumnName = "category", name = "category", insertable = false, updatable = false),
            @JoinColumn(referencedColumnName = "subcategory", name = "subcategory", insertable = false, updatable = false),
            @JoinColumn(referencedColumnName = "name", name = "name", insertable = false, updatable = false) })
    private ParameterDefinition definition;

    private Date effectiveEndTime;

    private String value;

    public Parameter(ParameterId id) {
        super();
        this.id = id;
        this.effectiveEndTime = null;
    }

    public Parameter(ParameterDefinition parameterDefinition) {
        super();
        this.id = new ParameterId(parameterDefinition.getId());
        this.definition = parameterDefinition;
    }

    public Parameter(ParameterId id, Date effectiveEndTime, String value) {
        super();
        this.id = id;
        this.effectiveEndTime = null;
        this.value = value;
    }

    public Parameter() {
        super();

    }

    public Date getEffectiveEndTime() {
        return effectiveEndTime;
    }

    public void setEffectiveEndTime(Date effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
    }

    public ParameterId getId() {
        return id;
    }

    public void setId(ParameterId id) {
        this.id = id;
    }

    public ParameterDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(ParameterDefinition definition) {
        this.definition = definition;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id.toString());
        sb.append(": ");
        sb.append(value.toString());

        return sb.toString();
    }

    public String getFullName() {
        return id.getFullName();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCategory() {
        return id == null ? null : id.getCategory();
    }

    public String getSubcategory() {
        return id == null ? null : id.getSubcategory();
    }

    public String getName() {
        return id == null ? null : id.getName();
    }

}
