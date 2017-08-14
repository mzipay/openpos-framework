package org.jumpmind.jumppos.pos.screen;

import java.util.ArrayList;
import java.util.List;

import org.jumpmind.jumppos.core.screen.PromptScreen;

public class SellItemScreen extends PromptScreen {

    private static final long serialVersionUID = 1L;

    private List<LineItem> items = new ArrayList<>();

    private String balanceDue;
    private String discountTotal;
    private String grandTotal;
    private String subTotal;
    private String taxTotal;
    private boolean transactionActive = false;
    private String transactionNumber;

    public SellItemScreen() {
        this.setType(SELL_SCREEN_TYPE);
    }

    public List<LineItem> getItems() {
        return items;
    }

    public void setItems(List<LineItem> items) {
        this.items = items;
    }

    public String getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(String balanceDue) {
        this.balanceDue = balanceDue;
    }

    public String getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(String discountTotal) {
        this.discountTotal = discountTotal;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(String taxTotal) {
        this.taxTotal = taxTotal;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }
    
    public void setTransactionActive(boolean transactionActive) {
        this.transactionActive = transactionActive;
    }
    
    public boolean isTransactionActive() {
        return transactionActive;
    }

}
