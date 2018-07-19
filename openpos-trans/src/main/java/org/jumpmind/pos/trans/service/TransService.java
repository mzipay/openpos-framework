package org.jumpmind.pos.trans.service;

import org.jumpmind.pos.service.EndpointDispatcher;
import org.jumpmind.pos.service.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trans")
public class TransService {

    @Autowired
    SaveTransQueueHelper saveTransactionQueueEndpoint;
    
    @Autowired
    private EndpointDispatcher endpointDispatcher;
    
    @RequestMapping(value="/transaction", method=RequestMethod.POST)
    @ResponseBody
    public ServiceResult finalizeTransaction(SaveTransRequest request) {
        return endpointDispatcher.dispatch("/transaction/finalize", request);
    }
    
    @RequestMapping(value="/transactionqueue", method=RequestMethod.POST)
    public void saveTransactionQueue(SaveTransRequest request) {
        saveTransactionQueueEndpoint.saveInBackground(request.getTransaction());
    }

    
    @RequestMapping(value="/transaction/create", method=RequestMethod.PUT)
    @ResponseBody
    public CreateTransResult createTransaction(CreateTransRequest request) {
        return endpointDispatcher.dispatch("/transaction/create", request);
    }
}
