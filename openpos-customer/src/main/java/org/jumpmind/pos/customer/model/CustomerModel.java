package org.jumpmind.pos.customer.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;
import org.jumpmind.pos.persist.Table;

@Table
public class CustomerModel extends Entity  {

    @Column(primaryKey = true)
    private String customerId;
    @Column
    private String firstName;
    @Column
    private String lastName;

    public CustomerModel() {

    }
}
