package org.jumpmind.pos.customer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.persist.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn(value = { "CustomerModule" })
public class CustomerRepository {

    // private Query<Jurisdiction> taxJurisdictionLookup = new Query<Jurisdiction>().named("taxJurisdictionLookup").result(Jurisdiction.class);

    @Autowired
    @Qualifier("customerSession")
    @Lazy
    private DBSession dbSession;

    // public List<> findTaxJurisdictions(String geoCode) {
    //     List<Jurisdiction> jurisdictions = dbSession.query(taxJurisdictionLookup, geoCode);
    //     HashMap<String, Group> groupMap = populateGroupMap();
    //     HashMap<String, List<RateRule>> rateRuleMap = new HashMap<>();

    //     for (Jurisdiction jurisdiction : jurisdictions) {
    //         String authorityId = jurisdiction.getAuthorityId();
    //         Authority authority = dbSession.findByNaturalId(Authority.class, authorityId);
    //         jurisdiction.setAuthority(authority);
            
    //         List<GroupRule> groupRules = dbSession.query(taxGroupRuleLookup, authorityId);
    //         if (groupRules != null) {
    //             for (GroupRule groupRule : groupRules) {
    //                 authority.addGroupRule(groupRule);
    //                 groupRule.setAuthority(authority);
    //                 String groupID = groupRule.getGroupId();
    //                 updateRateRuleMap(rateRuleMap, authorityId, groupID);
    //                 if (groupMap.containsKey(groupID)) {
    //                     Group group = groupMap.get(groupID);
    //                     groupRule.setGroup(group);
    //                 }
    //                 String key = authorityId + '-' + groupID;
    //                 if (rateRuleMap.containsKey(key)) {
    //                     List<RateRule> ruleList = rateRuleMap.get(key);
    //                     for (RateRule rateRule : ruleList) {
    //                         groupRule.addRateRule(rateRule);
    //                     }
    //                     rateRuleMap.put(key, ruleList);
    //                 }
    //             }
    //         }
    //     }
    //     return jurisdictions;
    // }
}
