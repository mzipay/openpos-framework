package org.jumpmind.jumppos.domain.item;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.jumpmind.jumppos.domain.BaseEntity;


@Entity
public class Promotion extends BaseEntity {

    @Id
    String id;    
    public String operatorText;
    public String customerText;
    public String receiptText;

    public Promotion() {

    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }

    /**
     * @return Returns the customerText.
     */
    public String getCustomerText() {
        return customerText;
    }

    /**
     * @param customerText
     *            The customerText to set.
     */
    public void setCustomerText(String customerText) {
        this.customerText = customerText;
    }

    /**
     * @return Returns the operatorText.
     */
    public String getOperatorText() {
        return operatorText;
    }

    /**
     * @param operatorText
     *            The operatorText to set.
     */
    public void setOperatorText(String operatorText) {
        this.operatorText = operatorText;
    }

    /**
     * @return Returns the receiptText.
     */
    public String getReceiptText() {
        return receiptText;
    }

    /**
     * @param receiptText
     *            The receiptText to set.
     */
    public void setReceiptText(String receiptText) {
        this.receiptText = receiptText;
    }
}
