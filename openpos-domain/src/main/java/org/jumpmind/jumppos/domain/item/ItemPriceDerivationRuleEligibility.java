package org.jumpmind.jumppos.domain.item;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class ItemPriceDerivationRuleEligibility extends PriceDerivationRuleEligibility {

    public String itemPriceDerivationRuleEligiblityId;

    @OneToOne
    public Item item;

    public ItemPriceDerivationRuleEligibility() {
    }

    /**
     * @return Returns the item.
     */
    public Item getItem() {
        return item;
    }

    /**
     * @param item
     *            The item to set.
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * @return Returns the itemPriceDerivationRuleEligiblityId.
     */
    public String getItemPriceDerivationRuleEligiblityId() {
        return itemPriceDerivationRuleEligiblityId;
    }

    /**
     * @param itemPriceDerivationRuleEligiblityId
     *            The itemPriceDerivationRuleEligiblityId to set.
     */
    public void setItemPriceDerivationRuleEligiblityId(String itemPriceDerivationRuleEligiblityId) {
        this.itemPriceDerivationRuleEligiblityId = itemPriceDerivationRuleEligiblityId;
    }
}
