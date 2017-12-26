package org.jumpmind.pos.core.screen;

import java.util.ArrayList;
import java.util.List;

public class SellItemScreen extends PromptScreen {

    private static final long serialVersionUID = 1L;

    private List<SellItem> items = new ArrayList<>();
    
    private String balanceDue;
    private String discountTotal;
    private String grandTotal;
    private String subTotal;
    private String taxTotal;
    private String itemActionName = "Item";
    private Transaction transaction = new Transaction();
    private Customer customer;
    
    private List<MenuItem> transactionMenuItems = new ArrayList<>();

    public SellItemScreen() {
        this.setType(ScreenType.Transaction);
        this.setTemplate(DefaultScreen.TEMPLATE_SELL);
        this.setShowScan(true);
        this.setScanType(ScanType.CAMERA_CORDOVA);
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

    public String getItemActionName() {
        return itemActionName;
    }

    public void setItemActionName(String itemActionName) {
        this.itemActionName = itemActionName;
    }
    
    public void addTransactionMenuItem(MenuItem menuItem) {
        this.transactionMenuItems.add(menuItem);
    }
    
    public void setTransactionMenuItems(List<MenuItem> transactionMenuItems) {
        this.transactionMenuItems = transactionMenuItems;
    }
    
    public List<MenuItem> getTransactionMenuItems() {
        return transactionMenuItems;
    }
}
