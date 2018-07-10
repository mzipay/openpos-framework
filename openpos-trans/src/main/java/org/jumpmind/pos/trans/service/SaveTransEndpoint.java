package org.jumpmind.pos.trans.service;

import org.jumpmind.pos.service.Endpoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(transactionManager = "transTxManager")
public class SaveTransEndpoint {

//    @Autowired
//    UnitStatusRepository unitStatusRepository;
    
    @Endpoint("/transaction")
    public CreateTransResult saveTransaction(SaveTransRequest request) {
        return null;
    }
}
