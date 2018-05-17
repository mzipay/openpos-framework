package org.jumpmind.pos.tax.model;

import java.util.ArrayList;
import java.util.List;

public class TaxCalculationResponse {

    private List<ItemTaxAmount> itemTaxAmounts = new ArrayList<ItemTaxAmount>();

    private List<TaxAmount> taxAmounts = new ArrayList<TaxAmount>();

    public void addItemTaxAmount(ItemTaxAmount amount) {
        itemTaxAmounts.add(amount);
    }

    public void addTaxAmount(TaxAmount amount) {
        taxAmounts.add(amount);
    }

    public List<ItemTaxAmount> getItemTaxAmounts() {
        return itemTaxAmounts;
    }

    public void setItemTaxAmounts(List<ItemTaxAmount> itemTaxAmounts) {
        this.itemTaxAmounts = itemTaxAmounts;
    }

    public List<TaxAmount> getTaxAmounts() {
        return taxAmounts;
    }

    public void setTaxAmounts(List<TaxAmount> taxAmounts) {
        this.taxAmounts = taxAmounts;
    }

    public TaxAmount getTaxAmount(String taxAuthorityId, String taxGroupId) {
        for (TaxAmount taxLineItem : taxAmounts) {
            if (taxLineItem.getTaxAuthorityId().equals(taxAuthorityId) && taxLineItem.getTaxGroupId().equals(taxGroupId)) {
                return taxLineItem;
            }
        }
        return null;
    }
}
