package org.jumpmind.pos.item.model;

import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name = "business_unit_item")
public class BusinessUnitItemModel extends AbstractModel {

    private static final long serialVersionUID = 1L;

    @Column(primaryKey = true)
    String businessUnitId;

    @Column(primaryKey = true)
    String itemId;

    @Column
    String sellingRuleId;

    @Column
    String sellingPriceId;

    ItemModel item;

    ItemIdModel lookupId;

    SellingPriceModel sellingPrice;

    SellingRuleModel sellingRule;

    public String getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(String businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getSellingRuleId() {
        return sellingRuleId;
    }

    public void setSellingRuleId(String sellingRuleId) {
        this.sellingRuleId = sellingRuleId;
    }

    public String getSellingPriceId() {
        return sellingPriceId;
    }

    public void setSellingPriceId(String sellingPriceId) {
        this.sellingPriceId = sellingPriceId;
    }

    public SellingPriceModel getEffectiveSellingPrice() {
        return null;
    }

    public SellingRuleModel getEffectiveSellingRule() {
        return null;
    }

}
