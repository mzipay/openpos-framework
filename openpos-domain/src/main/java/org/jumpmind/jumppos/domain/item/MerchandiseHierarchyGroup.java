package org.jumpmind.jumppos.domain.item;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.jumpmind.jumppos.domain.BaseEntity;

/**
 * An aggregation level for rolling ITEM sales and units for reporting purposes
 * and for mapping item-level activity into the retailers merchandise structure.
 */
@Entity
public class MerchandiseHierarchyGroup extends BaseEntity {

    @Id
    private String id;
    private String name;
    private String description;

    @ManyToMany
    private Collection<SalesRestriction> salesRestrictions;

    public MerchandiseHierarchyGroup() {
    }

    public MerchandiseHierarchyGroup(String id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public MerchandiseHierarchyGroup(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * The business name for the merchandise structure.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * A brief text description of the merchandise structure.
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSalesRestrictions(Collection<SalesRestriction> salesRestrictions) {
        this.salesRestrictions = salesRestrictions;
    }

    public Collection<SalesRestriction> getSalesRestrictions() {
        return salesRestrictions;
    }

    public String toString() {
        return (this.name != null ? this.name + " " : "") + this.description;
    }

}
