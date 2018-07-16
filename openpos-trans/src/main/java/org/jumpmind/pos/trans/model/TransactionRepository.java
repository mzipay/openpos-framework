package org.jumpmind.pos.trans.model;

import org.jumpmind.pos.persist.DBSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn(value = { "TransModule" })
public class TransactionRepository {

    
    @Autowired
    @Qualifier("transSession")
    @Lazy
    private DBSession dbSession;

    public void save(TransactionModel trans) {
        dbSession.save(trans);
    }
    
    
    public void save(TransactionQueueModel trans) {
        dbSession.save(trans);
    }
}