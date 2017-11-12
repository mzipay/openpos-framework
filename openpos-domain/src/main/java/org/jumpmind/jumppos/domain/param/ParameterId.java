package org.jumpmind.jumppos.domain.param;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Embeddable
public class ParameterId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String category;
    private String subcategory;
    private String name;
    private String level;
    private String levelValue;
    private String brand = Parameter.BRAND_ANY;
    private Date effectiveStartTime;


    public ParameterId()
    {
        super();
        this.effectiveStartTime = new Date();
    }
    
    public ParameterId(String category, String subcategory, String name, String level, String levelValue, String brand,
            Date effectiveStartTime) {
        super();
        this.category = category;
        this.subcategory = subcategory;
        this.name = name;
        this.level = level;
        this.levelValue = levelValue;
        this.brand = brand;
        this.effectiveStartTime = effectiveStartTime;
    }

    public ParameterId(ParameterDefinitionId id) {
        this.category = id.getCategory();
        this.subcategory = id.getSubcategory();
        this.name = id.getName();
        this.effectiveStartTime = new Date();
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevelValue() {
        return levelValue;
    }

    public void setLevelValue(String levelValue) {
        this.levelValue = levelValue;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Date getEffectiveStartTime() {
        return effectiveStartTime;
    }

    public void setEffectiveStartTime(Date effectiveStartTime) {
        this.effectiveStartTime = effectiveStartTime;
    }

    public String getFullName() {
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
        ParameterId rhs = (ParameterId) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj)).append(category, rhs.category).append(subcategory,
                rhs.subcategory).append(level, rhs.level).append(levelValue, rhs.levelValue).append(brand, rhs.brand)
                .append(effectiveStartTime, rhs.effectiveStartTime).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 41).append(category).append(subcategory).append(name).append(level).append(
                levelValue).append(brand).append(effectiveStartTime).toHashCode();
    }

}
