package org.jumpmind.pos.trans.model;

import java.util.Date;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table
public class Transaction extends TransactionEntity {

    @Column
    private int transType;
    
    @Column 
    private int transStatus;
    
    @Column
    private String operatorUsername; 
    
    @Column
    private Date beginTime;
    
    @Column Date endTime;
        
}
