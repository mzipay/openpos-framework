package org.jumpmind.jumppos.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


/**
 * A fee paid by the CUSTOMER for a service, normally associated with the
 * purchase of a STOCK ITEM. An example would be delivery charge, gift wrapping
 * service, etc.
 */
@Entity
@DiscriminatorValue("3")
public class MiscellaneousFee extends ServiceItem {

    private String description;
    private Boolean customerPaysForDisposition;

    public String getDescription() {
        return description;
    }

    /**
     * Describes this miscellaneous fee
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCustomerPaysForDisposition() {
        return customerPaysForDisposition;
    }

    /**
     * Flag that determines whether the customer is charged for this misc. fee
     * or the store absorbs it as an expense.
     */
    public void setCustomerPaysForDisposition(Boolean customerPaysForDisposition) {
        this.customerPaysForDisposition = customerPaysForDisposition;
    }
}
