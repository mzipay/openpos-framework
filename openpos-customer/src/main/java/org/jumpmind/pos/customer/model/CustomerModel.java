package org.jumpmind.pos.customer.model;

import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name = "customer")
public class CustomerModel extends AbstractModel {

    private static final long serialVersionUID = 1L;
    
    @Column(primaryKey = true)
    private String customerId;
    
    @Column
    private String firstName;
    
    @Column
    private String lastName;

    public CustomerModel() {

    }
}
