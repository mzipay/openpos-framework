package org.jumpmind.jumppos.domain.item;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.BaseEntity;

@Entity
public class PromotionPriceDerivationRule extends BaseEntity {

    @Id
    private String id;
    
    @OneToOne
    private Promotion promotion;
    
    @OneToOne
    private PriceDerivationRule priceDerivationRule;
    
    @OneToOne
    private PriceDerivationRuleEligibility priceDerivationRuleEligibility;

    public PromotionPriceDerivationRule() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /**
     * @return Returns the promotion.
     */
    public Promotion getPromotion() {
        return promotion;
    }

    /**
     * @param promotion
     *            The promotion to set.
     */
    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    /**
     * @return Returns the priceDerivationRule.
     */
    public PriceDerivationRule getPriceDerivationRule() {
        return priceDerivationRule;
    }

    /**
     * @param priceDerivationRule
     *            The priceDerivationRule to set.
     */
    public void setPriceDerivationRule(PriceDerivationRule priceDerivationRule) {
        this.priceDerivationRule = priceDerivationRule;
    }

    /**
     * @return Returns the priceDerivationRuleEligibility.
     */
    public PriceDerivationRuleEligibility getPriceDerivationRuleEligibility() {
        return priceDerivationRuleEligibility;
    }

    /**
     * @param priceDerivationRuleEligibility
     *            The priceDerivationRuleEligibility to set.
     */
    public void setPriceDerivationRuleEligibility(
            PriceDerivationRuleEligibility priceDerivationRuleEligibility) {
        this.priceDerivationRuleEligibility = priceDerivationRuleEligibility;
    }
}
