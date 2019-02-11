package org.jumpmind.pos.core.screen;

import java.util.ArrayList;
import java.util.List;

import org.jumpmind.pos.core.model.Total;
import org.jumpmind.pos.core.model.Total.TotalType;
import org.jumpmind.pos.core.screenpart.BaconStripPart;
import org.jumpmind.pos.core.screenpart.ScanPart;
import org.jumpmind.pos.core.screenpart.StatusStripPart;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SaleScreen extends Screen {
    private static final long serialVersionUID = 1L;
    public static final String ITEM_TOTAL_NAME = "itemTotal";

    private BaconStripPart baconStrip = new BaconStripPart();
    private ScanPart scan = new ScanPart();
    private StatusStripPart statusStrip = new StatusStripPart();

    private List<SellItem> items = new ArrayList<>();
    private int[] selectedItemIndexes = new int[0];
    private List<MenuItem> sausageLinks = new ArrayList<>();

    private String transactionMenuPrompt;
    private List<MenuItem> transactionMenuItems = new ArrayList<>();

    private String balanceDue;
    private String discountTotal;
    private String grandTotal;
    private String subTotal;
    private String taxTotal;
    private String itemActionName = "Item";
    private boolean transactionActive = false;
    private String customerName;
    private String noCustomerText;
    private String checkoutButtonText = "";
    private List<Total> totals = new ArrayList<>();
    private MenuItem loyaltyButton;
    private MenuItem promoButton;
    private List<MenuItem> multiSelectedMenuItems;

    public SaleScreen() {
        this.setScreenType(ScreenType.Sale);
        this.setId("sale");
        this.setTemplate(null);
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

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getItemActionName() {
        return itemActionName;
    }

    public void setItemActionName(String itemActionName) {
        this.itemActionName = itemActionName;
    }

    public List<Total> getTotals() {
        return totals;
    }

    public void setTotals(List<Total> totals) {
        this.totals = totals;
    }

    public void addTotal(String name, String amount) {
        this.totals.add(new Total(name, amount));
    }

    public void addTotal(Total total) {
        this.totals.add(total);
    }

    public void setItemTotal(String total) {
        Total itemTotal = this.getItemTotal();
        if (itemTotal == null) {
            this.totals.add(new Total(ITEM_TOTAL_NAME, total, TotalType.Quantity));
        } else {
            itemTotal.setAmount(total);
        }
    }

    @JsonIgnore
    public Total getItemTotal() {
        return this.totals.stream().filter(t -> t.getType() == TotalType.Quantity && ITEM_TOTAL_NAME.equalsIgnoreCase(t.getName()))
                .findFirst().orElse(null);
    }

    public String getNoCustomerText() {
        return noCustomerText;
    }

    public void setNoCustomerText(String noCustomerText) {
        this.noCustomerText = noCustomerText;
    }

    public MenuItem getLoyaltyButton() {
        return loyaltyButton;
    }

    public void setLoyaltyButton(MenuItem loyaltyButton) {
        this.loyaltyButton = loyaltyButton;
    }

    public MenuItem getPromoButton() {
        return promoButton;
    }

    public void setPromoButton(MenuItem promoButton) {
        this.promoButton = promoButton;
    }

    public List<MenuItem> getMultiSelectedMenuItems() {
        return multiSelectedMenuItems;
    }

    public void setMultiSelectedMenuItems(List<MenuItem> multiSelectedMenuItems) {
        this.multiSelectedMenuItems = multiSelectedMenuItems;
    }

    public MenuItem getLocalSausageLinkByAction(String action) {
        return this.sausageLinks.stream().filter(mi -> action.equalsIgnoreCase(mi.getAction())).findFirst().orElse(null);
    }

    public MenuItem getSausageLinkByTitle(String title) {
        return this.sausageLinks.stream().filter(mi -> title.equalsIgnoreCase(mi.getTitle())).findFirst().orElse(null);
    }

    public void addSausageLink(MenuItem menuItem) {
        this.sausageLinks.add(menuItem);
    }

    public void setSausageLinks(List<MenuItem> localMenuItems) {
        this.sausageLinks = localMenuItems;
    }

    public List<MenuItem> getSausageLinks() {
        return sausageLinks;
    }

    public String getTransactionMenuPrompt() {
        return transactionMenuPrompt;
    }

    public void setTransactionMenuPrompt(String transactionMenuPrompt) {
        this.transactionMenuPrompt = transactionMenuPrompt;
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

    public ScanPart getScan() {
        return scan;
    }

    public void setScan(ScanPart scan) {
        this.scan = scan;
    }

    public BaconStripPart getBaconStrip() {
        return baconStrip;
    }

    public void setBaconStrip(BaconStripPart baconStrip) {
        this.baconStrip = baconStrip;
    }

    public int[] getSelectedItemIndexes() {
        return selectedItemIndexes;
    }

    public void setSelectedItemIndexes(int[] selectedItemIndexes) {
        this.selectedItemIndexes = selectedItemIndexes;
    }

    public StatusStripPart getStatusStrip() {
        return statusStrip;
    }

    public void setStatusStrip(StatusStripPart statusStrip) {
        this.statusStrip = statusStrip;
    }
    
    public String getCheckoutButtonText() {
        return checkoutButtonText;
    }
    
    public void setCheckoutButtonText(String checkoutButtonText) {
        this.checkoutButtonText = checkoutButtonText;
    }
    
    public void setTransactionActive(boolean isTransactionActive) {
        this.transactionActive = isTransactionActive;
    }
    
    public boolean isTransactionActive() {
        return transactionActive;
    }

}
