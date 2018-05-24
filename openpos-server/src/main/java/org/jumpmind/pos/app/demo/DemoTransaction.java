package org.jumpmind.pos.app.demo;

import java.util.ArrayList;
import java.util.List;

import org.jumpmind.pos.core.model.Total;
import org.jumpmind.pos.core.screen.SellItem;
import org.jumpmind.pos.core.screen.TenderItem;
import org.jumpmind.pos.core.screen.Transaction;
import org.jumpmind.pos.core.screen.Workstation;
import org.jumpmind.pos.user.model.User;

public class DemoTransaction {

    private Transaction transaction = new Transaction();
    private Workstation workstation = new Workstation();
    private String balanceDue;
    private String discountTotal;
    private String tenderTotal;
    private String grandTotal;
    private String subTotal;
    private String taxTotal;
    private String customerName;
    private List<Total> totals = new ArrayList<>();
    private List<SellItem> lineItems = new ArrayList<>();
    private List<TenderItem> tenderLineItems = new ArrayList<>();
    private User user;
    
    public Transaction getTransaction() {
        return transaction;
    }
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    public Workstation getWorkstation() {
        return workstation;
    }
    public void setWorkstation(Workstation workstation) {
        this.workstation = workstation;
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
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public List<Total> getTotals() {
        return totals;
    }
    public void setTotals(List<Total> totals) {
        this.totals = totals;
    }
    public List<SellItem> getLineItems() {
        return lineItems;
    }
    public void setLineItems(List<SellItem> lineItems) {
        this.lineItems = lineItems;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public List<TenderItem> getTenderLineItems() {
        return tenderLineItems;
    }
    public void setTenderLineItems(List<TenderItem> tenderLineItems) {
        this.tenderLineItems = tenderLineItems;
    }
    public String getTenderTotal() {
        return tenderTotal;
    }
    public void setTenderTotal(String tenderTotal) {
        this.tenderTotal = tenderTotal;
    }
}
