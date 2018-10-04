package org.jumpmind.pos.item.service;

import org.jumpmind.pos.item.model.ItemRepository;
import org.jumpmind.pos.service.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(transactionManager="itmTxManager")
public class FindItemEndpoint {
    
    @Autowired
    ItemRepository itemRepository;

    @Endpoint("/findItem")
    public FindItemResponse find(FindItemRequest request) {
        String itemId = request.getItemId();
        
        // 1. look up in item id
        // 2. look up in biz unit item
        // 3. look up in item
        // 4. look up selling price and rules
        // 5. look up merch hierarchy
        return null;
    }
}
