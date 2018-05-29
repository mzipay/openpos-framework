package org.jumpmind.pos.tax.model;

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
@DependsOn(value = { "TaxModule" })
public class TaxRepository {

    private Query<Jurisdiction> taxJurisdictionLookup = new Query<Jurisdiction>().named("taxJurisdictionLookup").result(Jurisdiction.class);

    private Query<GroupRule> taxGroupRuleLookup = new Query<GroupRule>().named("taxGroupRuleLookup").result(GroupRule.class);

    private Query<RateRule> taxRateRuleLookup = new Query<RateRule>().named("taxRateRuleLookup").result(RateRule.class);

    @Autowired
    @Qualifier("taxSession")
    @Lazy
    private DBSession dbSession;

    public List<Jurisdiction> findTaxJurisdictions(String geoCode) {
        List<Jurisdiction> jurisdictions = dbSession.query(taxJurisdictionLookup, geoCode);
        HashMap<String, Group> groupMap = populateGroupMap();
        HashMap<String, List<RateRule>> rateRuleMap = new HashMap<>();

        for (Jurisdiction jurisdiction : jurisdictions) {
            String authorityId = jurisdiction.getAuthorityId();
            Authority authority = dbSession.findByNaturalId(Authority.class, authorityId);
            jurisdiction.setAuthority(authority);

            updateRateRuleMap(rateRuleMap, authorityId);

            List<GroupRule> groupRules = dbSession.query(taxGroupRuleLookup, authorityId);
            if (groupRules != null) {
                for (GroupRule groupRule : groupRules) {
                    authority.addGroupRule(groupRule);
                    groupRule.setAuthority(authority);
                    String groupID = groupRule.getGroupId();
                    if (groupMap.containsKey(groupID)) {
                        Group group = groupMap.get(groupID);
                        groupRule.setGroup(group);
                    }
                    String key = authorityId + '-' + groupID;
                    if (rateRuleMap.containsKey(key)) {
                        List<RateRule> ruleList = rateRuleMap.get(key);
                        for (RateRule rateRule : ruleList) {
                            groupRule.addRateRule(rateRule);
                        }
                        rateRuleMap.put(key, ruleList);
                    }
                }
            }
        }
        return jurisdictions;
    }

    private void updateRateRuleMap(HashMap<String, List<RateRule>> rateRuleMap, String authorityID) {
        Set<String> keySet = rateRuleMap.keySet();
        boolean contains = false;
        for (String string : keySet) {
            if (string.contains(authorityID)) {
                contains = true;
            }
        }
        // TODO this should just be !rateRuleMap.contains(authorityID + "-" + groupdId") and pass in groupId
        if (!contains) {
            List<RateRule> rateRules = dbSession.query(taxRateRuleLookup, authorityID);
            if (rateRules != null) {
                for (RateRule rateRule : rateRules) {
                    StringBuilder keyB = new StringBuilder();
                    keyB.append(rateRule.getAuthorityId());
                    keyB.append('-');
                    keyB.append(rateRule.getGroupId());
                    String key = keyB.toString();

                    List<RateRule> obj = rateRuleMap.get(key);
                    if (obj == null) {
                        obj = new ArrayList<>();
                        obj.add(rateRule);
                        rateRuleMap.put(key, obj);
                    } else {
                        obj.add(rateRule);
                    }
                }
            }
        }
    }

    private HashMap<String, Group> populateGroupMap() {
        HashMap<String, Group> groupMap = new HashMap<>();
        List<Group> groups = dbSession.findAll(Group.class);
        for (Group group : groups) {
            groupMap.put(group.getId(), group);
        }
        return groupMap;
    }
}
