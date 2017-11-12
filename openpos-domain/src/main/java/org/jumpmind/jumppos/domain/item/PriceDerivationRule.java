/*
 * Created on May 18, 2005
 */
package org.jumpmind.jumppos.domain.item;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.jumpmind.jumppos.domain.BaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class PriceDerivationRule extends BaseEntity {

    @Id
    String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
