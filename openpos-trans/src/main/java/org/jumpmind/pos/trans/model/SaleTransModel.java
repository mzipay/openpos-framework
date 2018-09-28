package org.jumpmind.pos.trans.model;

import java.math.BigDecimal;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name="sale_trans")
public class SaleTransModel extends AbstractTransModel {

    private static final long serialVersionUID = 1L;
    
    @Column
    BigDecimal total;
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

}
