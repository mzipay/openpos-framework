package org.jumpmind.pos.tax.model;

import java.util.List;

import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.persist.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn(value = { "TaxModule" })
public class TaxRepository {

    private Query<AuthorityRule> taxAuthorityRuleLookup = new Query<AuthorityRule>().named("taxAuthorityRuleLookup")
            .result(AuthorityRule.class);

    @Autowired
    @Qualifier("taxDbSession")
    @Lazy
    private DBSession dbSession;

    public List<AuthorityRule> findTaxAuthorityRules(String taxCalculationGeocode) {
        List<AuthorityRule> taxAuthorityRules = dbSession.query(taxAuthorityRuleLookup, taxCalculationGeocode);
        return taxAuthorityRules;
    }

}
