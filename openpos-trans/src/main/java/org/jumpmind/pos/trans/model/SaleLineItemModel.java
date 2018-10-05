package org.jumpmind.pos.trans.model;

import java.math.BigDecimal;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name="sale_line_item")
public class SaleLineItemModel extends AbstractTransLineModel {

    private static final long serialVersionUID = 1L;
    
    @Column
    String posItemId;
    
    @Column
    String itemId;
    
    @Column
    String departmentId;
    
    @Column
    String itemType;
    
    @Column
    BigDecimal regularUnitPrice;
    
    @Column
    BigDecimal regularUnitPriceQuantity;
        
    @Column
    BigDecimal actualUnitPrice;
    
    @Column
    BigDecimal actualUnitPriceQuantity;
    
    @Column
    BigDecimal quantity;
    
    @Column
    BigDecimal bulkUnitCount;
    
    @Column
    BigDecimal extendedAmount;
    
    @Column
    SaleLineItemActionCode actionCode;
    
    @Column
    String reasonCode;
    
    @Column
    boolean giftReceipt;
    
    @Column
    boolean itemReturnable;
    
    @Column
    boolean itemTaxable;
    
    @Column
    boolean discountApplied;
    
    @Column
    boolean damageDiscountApplied;
    
    @Column
    boolean taxIncludedInPrice;  
    
    @Column
    String taxGroupId;
        
    @Column
    int origLineSequenceNumber;
    
    @Column
    Long origSequenceNumber;
    
    @Column
    String origBusinessDate;
    
    @Column
    String origDeviceId;

    public String getPosItemId() {
        return posItemId;
    }

    public void setPosItemId(String posItemId) {
        this.posItemId = posItemId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public BigDecimal getRegularUnitPrice() {
        return regularUnitPrice;
    }

    public void setRegularUnitPrice(BigDecimal regularUnitPrice) {
        this.regularUnitPrice = regularUnitPrice;
    }

    public BigDecimal getRegularUnitPriceQuantity() {
        return regularUnitPriceQuantity;
    }

    public void setRegularUnitPriceQuantity(BigDecimal regularUnitPriceQuantity) {
        this.regularUnitPriceQuantity = regularUnitPriceQuantity;
    }

    public BigDecimal getActualUnitPrice() {
        return actualUnitPrice;
    }

    public void setActualUnitPrice(BigDecimal actualUnitPrice) {
        this.actualUnitPrice = actualUnitPrice;
    }

    public BigDecimal getActualUnitPriceQuantity() {
        return actualUnitPriceQuantity;
    }

    public void setActualUnitPriceQuantity(BigDecimal actualUnitPriceQuantity) {
        this.actualUnitPriceQuantity = actualUnitPriceQuantity;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getBulkUnitCount() {
        return bulkUnitCount;
    }

    public void setBulkUnitCount(BigDecimal bulkUnitCount) {
        this.bulkUnitCount = bulkUnitCount;
    }

    public BigDecimal getExtendedAmount() {
        return extendedAmount;
    }

    public void setExtendedAmount(BigDecimal extendedAmount) {
        this.extendedAmount = extendedAmount;
    }

    public SaleLineItemActionCode getActionCode() {
        return actionCode;
    }

    public void setActionCode(SaleLineItemActionCode actionCode) {
        this.actionCode = actionCode;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public boolean isGiftReceipt() {
        return giftReceipt;
    }

    public void setGiftReceipt(boolean giftReceipt) {
        this.giftReceipt = giftReceipt;
    }

    public boolean isItemReturnable() {
        return itemReturnable;
    }

    public void setItemReturnable(boolean itemReturnable) {
        this.itemReturnable = itemReturnable;
    }

    public boolean isItemTaxable() {
        return itemTaxable;
    }

    public void setItemTaxable(boolean itemTaxable) {
        this.itemTaxable = itemTaxable;
    }

    public boolean isDiscountApplied() {
        return discountApplied;
    }

    public void setDiscountApplied(boolean discountApplied) {
        this.discountApplied = discountApplied;
    }

    public boolean isDamageDiscountApplied() {
        return damageDiscountApplied;
    }

    public void setDamageDiscountApplied(boolean damageDiscountApplied) {
        this.damageDiscountApplied = damageDiscountApplied;
    }

    public boolean isTaxIncludedInPrice() {
        return taxIncludedInPrice;
    }

    public void setTaxIncludedInPrice(boolean taxIncludedInPrice) {
        this.taxIncludedInPrice = taxIncludedInPrice;
    }

    public String getTaxGroupId() {
        return taxGroupId;
    }

    public void setTaxGroupId(String taxGroupId) {
        this.taxGroupId = taxGroupId;
    }

    public int getOrigLineSequenceNumber() {
        return origLineSequenceNumber;
    }

    public void setOrigLineSequenceNumber(int origLineSequenceNumber) {
        this.origLineSequenceNumber = origLineSequenceNumber;
    }

    public Long getOrigSequenceNumber() {
        return origSequenceNumber;
    }

    public void setOrigSequenceNumber(Long origSequenceNumber) {
        this.origSequenceNumber = origSequenceNumber;
    }

    public String getOrigBusinessDate() {
        return origBusinessDate;
    }

    public void setOrigBusinessDate(String origBusinessDate) {
        this.origBusinessDate = origBusinessDate;
    }

    public String getOrigDeviceId() {
        return origDeviceId;
    }

    public void setOrigDeviceId(String origDeviceId) {
        this.origDeviceId = origDeviceId;
    }
    
    
}
