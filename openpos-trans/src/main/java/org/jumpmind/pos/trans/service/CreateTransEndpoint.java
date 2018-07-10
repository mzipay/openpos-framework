package org.jumpmind.pos.trans.service;

import org.jumpmind.pos.service.Endpoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(transactionManager = "transTxManager")
public class CreateTransEndpoint {

//    @Autowired
//    UnitStatusRepository unitStatusRepository;
    
    @Endpoint("/transaction/create")
    public CreateTransResult createTransaction(CreateTransRequest request) {
        return null;
    }
}
