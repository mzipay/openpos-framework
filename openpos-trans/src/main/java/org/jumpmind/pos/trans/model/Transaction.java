package org.jumpmind.pos.trans.model;

import java.util.Date;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;
import org.jumpmind.pos.persist.Table;

@Table
public class Transaction extends Entity {

    @Column(primaryKey = true)
    private Long sequenceNumber;
    
    @Column(primaryKey = true)
    private String businessUnitId;
    
    @Column(primaryKey = true)
    private String businessDate;
    
    @Column(primaryKey = true)
    private String deviceId;
    
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
