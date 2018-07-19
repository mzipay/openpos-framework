package org.jumpmind.pos.trans.model;

import org.jumpmind.pos.persist.DBSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn(value = { "TransModule" })
public class TransRepository {

    
    @Autowired
    @Qualifier("transSession")
    @Lazy
    private DBSession dbSession;

    public void save(TransModel trans) {
        dbSession.save(trans);
    }
    
    
    public void save(TransQueueModel trans) {
        dbSession.save(trans);
    }
}