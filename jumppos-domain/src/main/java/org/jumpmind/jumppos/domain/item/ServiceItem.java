package org.jumpmind.jumppos.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * A type of ITEM that provides a detailed identifier and description for a
 * service offered for sale to a customer in the retail store. This entity also
 * identifies and describes rental items and other tangible items that are used
 * by a customer for a contracted period of time but not purchased.
 */
@Entity
@DiscriminatorValue("2")
public class ServiceItem extends Item {

    public ServiceItem() {

    }
    
}
