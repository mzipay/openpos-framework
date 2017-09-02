package org.jumpmind.jumppos.domain.party;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.security.Operator;

@Entity
public class Worker extends Person {
    
    @OneToOne 
    Operator operator;
    
    public void setOperator(Operator operator) {
        this.operator = operator;
    }
    
    public Operator getOperator() {
        return operator;
    }

}
