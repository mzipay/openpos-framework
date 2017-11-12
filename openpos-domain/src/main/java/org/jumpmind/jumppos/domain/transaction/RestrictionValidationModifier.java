package org.jumpmind.jumppos.domain.transaction;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.BaseEntity;
import org.jumpmind.jumppos.domain.item.RestrictionValidationQuestion;

@Entity
public class RestrictionValidationModifier extends BaseEntity {

    @Id
    private String id;

    private String answer;
    
    @OneToOne
    private SaleReturnLineItem lineItem;

    @OneToOne
    private RestrictionValidationQuestion restrictionValidationQuestion;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public RestrictionValidationQuestion getRestrictionValidationQuestion() {
        return restrictionValidationQuestion;
    }

    public void setRestrictionValidationQuestion(
            RestrictionValidationQuestion restrictionValidationQuestion) {
        this.restrictionValidationQuestion = restrictionValidationQuestion;
    }

    public void setLineItem(SaleReturnLineItem lineItem) {
        this.lineItem = lineItem;
    }
    
    public SaleReturnLineItem getLineItem() {
        return lineItem;
    }
}
