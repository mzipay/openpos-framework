package org.jumpmind.jumppos.domain.item;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.BaseEntity;

/**
 * A grouping of items with similar poInteger of sale control and processing
 * attributes. This entity type may also be used to control sales that are not
 * properly identified at the item-level.
 */
@Entity
public class PosDepartment extends BaseEntity {

    @Id
    private String id;

    @OneToOne
    private ItemSellingRule itemSellingRule;

    @OneToOne
    private PosDepartment parentPosDepartment;

    /**
     * The human readable name for the POSDepartment
     */
    private String name;

    public PosDepartment() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ItemSellingRule getItemSellingRule() {
        return itemSellingRule;
    }

    public void setItemSellingRule(ItemSellingRule itemSellingRule) {
        this.itemSellingRule = itemSellingRule;
    }

    public PosDepartment getParentPosDepartment() {
        return parentPosDepartment;
    }

    public void setParentPosDepartment(PosDepartment parentPosDepartment) {
        this.parentPosDepartment = parentPosDepartment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
