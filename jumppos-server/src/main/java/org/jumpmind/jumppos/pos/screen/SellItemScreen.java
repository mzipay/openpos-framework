package org.jumpmind.jumppos.pos.screen;

import java.util.ArrayList;
import java.util.List;

import org.jumpmind.jumppos.core.screen.Customer;
import org.jumpmind.jumppos.core.screen.PromptScreen;
import org.jumpmind.jumppos.core.screen.ScreenType;
import org.jumpmind.jumppos.core.screen.SellItem;
import org.jumpmind.jumppos.core.screen.Transaction;

public class SellItemScreen extends PromptScreen {

    private static final long serialVersionUID = 1L;

    private List<SellItem> items = new ArrayList<>();

    private String balanceDue;
    private String discountTotal;
    private String grandTotal;
    private String subTotal;
    private String taxTotal;
    private Transaction transaction = new Transaction();
    private Customer customer;

    public SellItemScreen() {
        this.setType(ScreenType.Sell);
    }

    public List<SellItem> getItems() {
        return items;
    }

    public void setItems(List<SellItem> items) {
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

    
    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Customer getCustomer() {
        return customer;
    }
}
