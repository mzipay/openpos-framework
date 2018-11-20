package org.jumpmind.pos.persist.cars;

import java.util.Date;

import org.jumpmind.pos.persist.ModelId;

public class ServiceInvoiceId extends ModelId {

    public void setInvoiceDate(Date invoiceDate) {
        this.keys.put("invoiceDate", invoiceDate);
    }

    public void setInvoiceLocation(long invoiceLocation) {
        this.keys.put("invoiceLocation", invoiceLocation);
    }
    
    public void setInvoiceNumber(long invoiceNumber) {
        this.keys.put("invoiceNumber", invoiceNumber);
    }

}
