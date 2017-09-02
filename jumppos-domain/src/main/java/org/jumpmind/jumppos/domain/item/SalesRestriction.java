package org.jumpmind.jumppos.domain.item;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.BaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class SalesRestriction extends BaseEntity {

    @Id
    private String id;

    private String salesRestrictionCode;

    @OneToOne
    private RestrictionValidationQuestion restrictionValidationQuestion;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public RestrictionValidationQuestion getRestrictionValidationQuestion() {
        return restrictionValidationQuestion;
    }

    public void setRestrictionValidationQuestion(
            RestrictionValidationQuestion restrictionValidationQuestion) {
        this.restrictionValidationQuestion = restrictionValidationQuestion;
    }

    public String getSalesRestrictionCode() {
        return salesRestrictionCode;
    }

    public void setSalesRestrictionCode(String salesRestrictionCode) {
        this.salesRestrictionCode = salesRestrictionCode;
    }

}
