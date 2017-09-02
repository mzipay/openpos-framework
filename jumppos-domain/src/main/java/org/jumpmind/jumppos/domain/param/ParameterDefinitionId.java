package org.jumpmind.jumppos.domain.param;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Embeddable
public class ParameterDefinitionId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String category;
    private String subcategory;
    private String name;

    public ParameterDefinitionId(String category, String subcategory, String name) {
        super();
        this.category = category;
        this.subcategory = subcategory;
        this.name = name;
    }

    public ParameterDefinitionId() {
        super();

    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(category).append(",").append(subcategory).append(",").append(name);
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        ParameterDefinitionId rhs = (ParameterDefinitionId) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj)).append(category, rhs.category).append(subcategory,
                rhs.subcategory).append(name, rhs.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 41).append(category).append(subcategory).append(name).toHashCode();
    }
}
