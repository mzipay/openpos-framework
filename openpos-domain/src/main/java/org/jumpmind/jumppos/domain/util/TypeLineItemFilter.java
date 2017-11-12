package org.jumpmind.jumppos.domain.util;

import org.jumpmind.jumppos.domain.transaction.RetailTransactionLineItem;

/**
 * Filter a specific type of line item from a collection of retail transaction
 * line items.
 * 
 * @author chenson
 * 
 * @param <T>
 */
public class TypeLineItemFilter<T extends RetailTransactionLineItem> extends
        AbstractLineItemFilter<T> {

    @Override
    public boolean shouldInclude(T lineItem) {
        return true;
    }

}