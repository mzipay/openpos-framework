package org.jumpmind.jumppos.domain.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jumpmind.jumppos.domain.transaction.RetailTransactionLineItem;

/**
 * This class acts as a filter for line items. It will narrow down the line
 * items to the generic type specified when using this class. It also gives the
 * programmer an opportunity to further refine the search by overriding the
 * shouldInclude method.
 */
abstract public class AbstractLineItemFilter<T extends RetailTransactionLineItem> {

    public List<T> filter(Collection<RetailTransactionLineItem> lineItems, Class<T> filterClass) {
        List<T> list = new ArrayList<T>();

        if (lineItems != null) {
            for (RetailTransactionLineItem lineItem : lineItems) {
                if (lineItem != null && filterClass.isInstance(lineItem)
                        && shouldInclude(filterClass.cast(lineItem))) {
                    list.add(filterClass.cast(lineItem));
                }
            }
        }
        return list;
    }

    abstract public boolean shouldInclude(T lineItem);

}