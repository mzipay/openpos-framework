package org.jumpmind.pos.app.demo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jumpmind.pos.core.model.FormField;
import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.screen.Screen;
import org.jumpmind.pos.core.screen.SelectionMode;
import org.jumpmind.pos.core.screen.SellItem;
import org.jumpmind.pos.core.screen.SellItemScreen;
import org.jumpmind.pos.core.screen.TenderItem;
import org.jumpmind.pos.core.screen.TenderingScreen;
import org.jumpmind.pos.core.template.Scan;
import org.jumpmind.pos.core.template.SellTemplate;
import org.jumpmind.pos.user.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DemoTransactionService {

    private static Logger log = Logger.getLogger(DemoTransactionService.class); 

    public static AtomicInteger transactionSequence = new AtomicInteger(24343);
    DecimalFormat FORMATTER = new DecimalFormat();

    @Autowired
    private DemoItemService itemService;

    private int counter = 1;

    @PostConstruct
    public void init() {
        FORMATTER.setMaximumFractionDigits(2);
        FORMATTER.setMinimumFractionDigits(2);
        FORMATTER.setGroupingUsed(true);
    }

    public DemoTransaction startTransaction(UserModel user, String nodeId) {
        DemoTransaction transaction = new DemoTransaction();
        transaction.getTransaction().setTransactionNumber(String.valueOf(transactionSequence.getAndIncrement()));
        transaction.getWorkstation().setWorkstationId(nodeId);
        transaction.getWorkstation().setStoreId(nodeId);
        transaction.getTransaction().setActive(true);
        transaction.setUser(user);
        return transaction;
    }

    public DemoTransaction ringItem(DemoTransaction transaction, String scanData) {
        DemoItem item = itemService.lookupItem(scanData);
        if (item == null ) {
            throw new DemoException("Item not found.");
        }

        SellItem lineItem = new SellItem();
        lineItem.setSellingPrice(FORMATTER.format(item.getSellingPrice()));
        lineItem.setAmount(FORMATTER.format(item.getSellingPrice()));
        lineItem.setDescription("WholeHearted Grain Free All Life Stages Salmon and Pea Recipe Dry Dog Food, 25 lbs.");
        lineItem.setPosItemId(scanData);
        lineItem.setQuantity("1");

        transaction.getLineItems().add(lineItem);
        calculate(transaction);
        return transaction;
    }

    public DemoTransaction applyTender(DemoTransaction transaction, String tenderId, String amountString) {
        BigDecimal tenderAmount = null;

        if (!StringUtils.isEmpty(amountString)) {
            tenderAmount = parse(amountString);
        } else {
            tenderAmount = parse(transaction.getBalanceDue());
        }

        TenderItem tenderItem = new TenderItem();
        tenderItem.setAmount(FORMATTER.format(tenderAmount));
        tenderItem.setType(formatDemoTenderId(tenderId));
        tenderItem.setIndex(transaction.getTenderLineItems().size()+1);
        transaction.getTenderLineItems().add(tenderItem);
        calculate(transaction);
        return transaction;
    }

    private String formatDemoTenderId(String tenderId) {
        String[] demoCreditTenders = {
                "VISA XXXXXXXXXXXX2545",      
                "MC XXXXXXXXXXXX5439",      
                "DISCOVER XXXXXXXXXXXX0011",      
                "AMEX XXXXXXXXXXXX2545",
                "MC XXXXXXXXXXXX9931",
        };
        String[] demoGiftCardTenders = {
                "GC XXXXXXXXXX6239",
                "STORE CREDIT XXXXXXXXXX2510",
                "GC XXXXXXXXXX23412"
        };

        if (tenderId.equals("Credit")) {
            if (counter++ % 3 == 0) {
                throw new DemoException(demoCreditTenders[counter % demoCreditTenders.length] + " DECLINED");
            }
            return demoCreditTenders[counter++ % demoCreditTenders.length];
        } else if (tenderId.equals("GiftCard")) {
            if (counter++ % 3 == 0) {
                throw new DemoException(demoGiftCardTenders[counter % demoGiftCardTenders.length] + " NOT ACTIVATED");
            }
            return demoGiftCardTenders[counter++ % demoGiftCardTenders.length];
        } 
        else {
            return tenderId;
        }
    }

    public SellItemScreen buildScreen(DemoTransaction transaction) {
        SellItemScreen screen = new SellItemScreen();
        screen.setTransaction(transaction.getTransaction());
        screen.setLogoutButton(new MenuItem("Back", "Logout", "exit_to_app"));
        if (transaction.getLineItems().isEmpty()) {            
            screen.setPrompt("Ready to begin");
        } else {
            screen.setPrompt(null);
        }
        screen.setName("Sell");
        screen.setReadOnly(false);

        SellTemplate template = new SellTemplate();
        template.setWorkstation(transaction.getWorkstation());
        template.setOperatorText(transaction.getUser().getFirstName() + " " + transaction.getUser().getLastName());

        Scan scan = new Scan();
        scan.setScanSomethingText("Scan Something");
        template.setScan(scan);

        template.addLocalMenuItem(new MenuItem("CustomerSearch", "Customer", "person"));
        template.addLocalMenuItem(new MenuItem("ItemLookup", "Item Lookup", "wallpaper"));
        template.addLocalMenuItem(new MenuItem("Returns", "Returns", "receipt"));
        screen.setTemplate(template);        

        for (SellItem lineItem : transaction.getLineItems()) {            
            screen.getItems().add(lineItem);
        }

        screen.setSubTotal(transaction.getSubTotal());
        screen.setTaxTotal(transaction.getTaxTotal());
        screen.setGrandTotal(transaction.getGrandTotal());
        screen.setBalanceDue(transaction.getBalanceDue());

        return screen;        
    }

    protected void calculate(DemoTransaction transaction) {

        BigDecimal subtotal = BigDecimal.ZERO;

        for (SellItem lineItem : transaction.getLineItems()) {
            subtotal = subtotal.add(parse(lineItem.getAmount()));
        }

        BigDecimal taxTotal = subtotal.multiply(parse("0.07"));

        BigDecimal grandTotal = subtotal.add(taxTotal);

        transaction.setSubTotal(FORMATTER.format(subtotal));
        transaction.setTaxTotal(FORMATTER.format(taxTotal));
        transaction.setGrandTotal(FORMATTER.format(grandTotal));

        BigDecimal tenderTotal = BigDecimal.ZERO;
        for (TenderItem tenderItem : transaction.getTenderLineItems()) {
            tenderTotal = tenderTotal.add(parse(tenderItem.getAmount()));
        }

        transaction.setTenderTotal(FORMATTER.format(tenderTotal));

        BigDecimal balanceDue = grandTotal.subtract(tenderTotal);

        transaction.setBalanceDue(FORMATTER.format(balanceDue));

    }


    public Screen buildTenderMenuScreen(DemoTransaction transaction) {

        TenderingScreen tenderingScreen = new TenderingScreen();
        if (transaction.getTenderLineItems().isEmpty()) {            
            tenderingScreen.setBackButton(new MenuItem("Back"));
        }
        tenderingScreen.setBalanceDue(transaction.getBalanceDue());
        tenderingScreen.setPrompt("Choose a tender");
        tenderingScreen.setSelectionMode(SelectionMode.Single);
        tenderingScreen.addItemAction(new MenuItem("Cash", "Cash"));
        tenderingScreen.setText("Choose a tender text.");
        tenderingScreen.setTenderAmount(new FormField("tenderAmount", transaction.getGrandTotal()));

        SellTemplate template = new SellTemplate();
        template.setWorkstation(transaction.getWorkstation());
        template.setOperatorText(transaction.getUser().getFirstName() + " " + transaction.getUser().getLastName());

        template.addLocalMenuItem(new MenuItem("TenderCash", "Cash", "attach_money"));
        template.addLocalMenuItem(new MenuItem("TenderCreditDebit", "Credit/Debit", "credit_card"));
        template.addLocalMenuItem(new MenuItem("TenderGiftCard", "Gift Card", "card_giftcard"));

        tenderingScreen.setTemplate(template);
        for (TenderItem tenderItem : transaction.getTenderLineItems()) {
            tenderingScreen.addTenderItem(tenderItem);
        }

        return tenderingScreen;
    }

    public BigDecimal parse(String numberAsString) {
        String numberAsStringSansSeperator = numberAsString.replaceAll(",", "");
        try {
            return new BigDecimal(numberAsString);        
        } catch (Exception ex) {
            log.warn("Failed to parse number: " + numberAsString);
            return BigDecimal.ZERO;
        }
    }
}
