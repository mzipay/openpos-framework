package org.jumpmind.pos.item.service;

import org.jumpmind.pos.service.EndpointDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class ItemService {

    @Autowired
    private EndpointDispatcher endpointDispatcher;

    @RequestMapping("/find")
    public FindItemResponse find(FindItemRequest request) {
        return endpointDispatcher.dispatch("/findItem", request);
    }
}
