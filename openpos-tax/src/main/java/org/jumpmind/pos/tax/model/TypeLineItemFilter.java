package org.jumpmind.pos.tax.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Filter a specific type of line item from a collection of retail transaction
 * line items.
 * 
 * @author chenson
 * 
 * @param <T>
 */
public class TypeLineItemFilter<T extends RetailTransactionLineItem> {

    public List<T> filter(Collection<RetailTransactionLineItem> lineItems, Class<T> filterClass) {
        List<T> list = new ArrayList<T>();

        if (lineItems != null) {
            for (RetailTransactionLineItem lineItem : lineItems) {
                if (lineItem != null && filterClass.isInstance(lineItem) && shouldInclude(filterClass.cast(lineItem))) {
                    list.add(filterClass.cast(lineItem));
                }
            }
        }
        return list;
    }

    public boolean shouldInclude(T lineItem) {
        return true;
    }

}