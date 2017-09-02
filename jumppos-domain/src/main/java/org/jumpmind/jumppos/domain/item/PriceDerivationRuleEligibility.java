package org.jumpmind.jumppos.domain.item;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.jumpmind.jumppos.domain.BaseEntity;

/**
 * Defines the conditions which govern the application of a PRICE DERIVATION
 * RULE to a sale
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class PriceDerivationRuleEligibility extends BaseEntity {

    @Id
    private String id;
    private String typeCode;

    public PriceDerivationRuleEligibility() {
    }

    public PriceDerivationRuleEligibility(String typeCode) {
        this.typeCode = typeCode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
}
