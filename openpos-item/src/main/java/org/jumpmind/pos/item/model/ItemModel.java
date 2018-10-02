package org.jumpmind.pos.item.model;

import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name="item")
public class ItemModel extends AbstractModel {

    private static final long serialVersionUID = 1L;
    
    @Column(primaryKey=true)
    String itemId;
    
    @Column
    String brandId;
    
    @Column
    String departmentId;
    
    @Column
    String itemName;
    
    @Column
    String description;
    
    @Column
    String longDescription;
    
    @Column
    String sellingRuleId;
    
    @Column
    String sellingPriceId;
    
    @Column
    String taxGroupId;
    
    @Column 
    String taxExemptCode;
    
    @Column
    String usuageCode;
    
    @Column
    ItemTypeCode typeCode;
    
    @Column
    String hierarchyId;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String name) {
        this.itemName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getSellingRuleId() {
        return sellingRuleId;
    }

    public void setSellingRuleId(String defaultSellingRuleId) {
        this.sellingRuleId = defaultSellingRuleId;
    }

    public String getSellingPriceId() {
        return sellingPriceId;
    }

    public void setSellingPriceId(String defaultSellingPriceId) {
        this.sellingPriceId = defaultSellingPriceId;
    }

    public String getTaxGroupId() {
        return taxGroupId;
    }

    public void setTaxGroupId(String taxGroupId) {
        this.taxGroupId = taxGroupId;
    }

    public String getTaxExemptCode() {
        return taxExemptCode;
    }

    public void setTaxExemptCode(String taxExemptCode) {
        this.taxExemptCode = taxExemptCode;
    }

    public String getUsuageCode() {
        return usuageCode;
    }

    public void setUsuageCode(String usuageCode) {
        this.usuageCode = usuageCode;
    }

    public ItemTypeCode getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(ItemTypeCode typeCode) {
        this.typeCode = typeCode;
    }

    public String getHierarchyId() {
        return hierarchyId;
    }

    public void setHierarchyId(String hierarchyId) {
        this.hierarchyId = hierarchyId;
    }
    
    


}
