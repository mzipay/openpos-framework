package org.jumpmind.jumppos.domain.item;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.jumpmind.jumppos.domain.BaseEntity;
import org.jumpmind.jumppos.domain.business.BusinessUnitGroup;
import org.jumpmind.jumppos.domain.tax.TaxableGroup;

@Entity
public class BusinessUnitGroupItem extends BaseEntity {

    @Id
    BusinessUnitGroupItemId id;

    @OneToOne
    private TaxableGroup taxableGroup;

    @OneToOne
    private ItemSellingRule itemSellingRule;

    @OneToMany(mappedBy = "businessUnitGroupItemId")
    private List<ItemSellingPrice> itemSellingPrices;

    public BusinessUnitGroupItem() {
    }

    public BusinessUnitGroupItem(BusinessUnitGroup group, Item item) {
        this.id = new BusinessUnitGroupItemId(item, group);
    }

    public void setId(BusinessUnitGroupItemId id) {
        this.id = id;
    }

    public BusinessUnitGroupItemId getId() {
        return id;
    }

    public Item getItem() {
        return id != null ? id.getItem() : null;
    }

    public BusinessUnitGroup getBusinessUnitGroup() {
        return id != null ? id.getBusinessUnitGroup() : null;
    }

    public void setTaxableGroup(TaxableGroup taxableGroup) {
        this.taxableGroup = taxableGroup;
    }

    public TaxableGroup getTaxableGroup() {
        return taxableGroup;
    }

    public List<ItemSellingPrice> getItemSellingPrices() {
        return itemSellingPrices;
    }

    public void setItemSellingPrices(List<ItemSellingPrice> itemSellingPrices) {
        this.itemSellingPrices = itemSellingPrices;
    }

    public void setItemSellingRule(ItemSellingRule itemSellingRule) {
        this.itemSellingRule = itemSellingRule;
    }

    public ItemSellingRule getItemSellingRule() {
        return itemSellingRule;
    }

    @Transient
    public ItemSellingRule findItemSellingRule() {
        ItemSellingRule toUse = this.itemSellingRule;
        if (toUse == null) {
            toUse = id.getItem().getItemSellingRule();
        }
        return toUse;
    }
    
    @Transient
    public TaxableGroup findTaxableGroup() {
        TaxableGroup toUse = this.taxableGroup;
        if (toUse == null) {
            toUse = id.getItem().getTaxableGroup();
        }
        return toUse;
    }

    @Transient
    public String findTaxableGroupId() {
        String taxableGroupId = null;
        TaxableGroup group = findTaxableGroup();
        if (group != null) {
            taxableGroupId = group.getId();
        }
        return taxableGroupId;
    }
    
    @Transient
    public ItemSellingPrice findItemSellingPriceForDate(Date forTime) {
        if (itemSellingPrices != null) {
            for (ItemSellingPrice price : itemSellingPrices) {
                Date start = price.getEffectiveStartTime();
                Date end = price.getEffectiveEndTime();
                if (start != null && start.before(forTime)) {
                    if (end == null || end.after(forTime)) {
                        return price;
                    }
                }
            }
        }
        return null;
    }

}
