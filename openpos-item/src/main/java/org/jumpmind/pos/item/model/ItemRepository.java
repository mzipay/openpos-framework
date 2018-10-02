package org.jumpmind.pos.item.model;

import org.jumpmind.pos.persist.DBSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn(value = { "ItemModule" })
public class ItemRepository {

//    private Query<JurisdictionModel> taxJurisdictionLookup = new Query<JurisdictionModel>().named("taxJurisdictionLookup").result(JurisdictionModel.class);
//
//    private Query<GroupRuleModel> taxGroupRuleLookup = new Query<GroupRuleModel>().named("taxGroupRuleLookup").result(GroupRuleModel.class);
//
//    private Query<RateRuleModel> taxRateRuleLookup = new Query<RateRuleModel>().named("taxRateRuleLookup").result(RateRuleModel.class);

    @Autowired
    @Qualifier("itemSession")
    @Lazy
    private DBSession dbSession;

//    public List<JurisdictionModel> findTaxJurisdictions(String geoCode) {
//        List<JurisdictionModel> jurisdictions = dbSession.query(taxJurisdictionLookup, geoCode);
//        HashMap<String, GroupModel> groupMap = populateGroupMap();
//        HashMap<String, List<RateRuleModel>> rateRuleMap = new HashMap<>();
//
//        for (JurisdictionModel jurisdiction : jurisdictions) {
//            String authorityId = jurisdiction.getAuthorityId();
//            AuthorityModel authority = dbSession.findByNaturalId(AuthorityModel.class, authorityId);
//            jurisdiction.setAuthority(authority);
//            
//            List<GroupRuleModel> groupRules = dbSession.query(taxGroupRuleLookup, authorityId);
//            if (groupRules != null) {
//                for (GroupRuleModel groupRule : groupRules) {
//                    authority.addGroupRule(groupRule);
//                    groupRule.setAuthority(authority);
//                    String groupID = groupRule.getGroupId();
//                    updateRateRuleMap(rateRuleMap, authorityId, groupID);
//                    if (groupMap.containsKey(groupID)) {
//                        GroupModel group = groupMap.get(groupID);
//                        groupRule.setGroup(group);
//                    }
//                    String key = authorityId + '-' + groupID;
//                    if (rateRuleMap.containsKey(key)) {
//                        List<RateRuleModel> ruleList = rateRuleMap.get(key);
//                        for (RateRuleModel rateRule : ruleList) {
//                            groupRule.addRateRule(rateRule);
//                        }
//                        rateRuleMap.put(key, ruleList);
//                    }
//                }
//            }
//        }
//        return jurisdictions;
//    }
//
//    private void updateRateRuleMap(HashMap<String, List<RateRuleModel>> rateRuleMap, String authorityID, String groupId) {
//
//        if (!rateRuleMap.containsKey(authorityID + "-" + groupId)) {
//            List<RateRuleModel> rateRules = dbSession.query(taxRateRuleLookup, authorityID);
//            if (rateRules != null) {
//                for (RateRuleModel rateRule : rateRules) {
//                    StringBuilder keyB = new StringBuilder();
//                    keyB.append(rateRule.getAuthorityId());
//                    keyB.append('-');
//                    keyB.append(rateRule.getGroupId());
//                    String key = keyB.toString();
//
//                    List<RateRuleModel> obj = rateRuleMap.get(key);
//                    if (obj == null) {
//                        obj = new ArrayList<>();
//                        obj.add(rateRule);
//                        rateRuleMap.put(key, obj);
//                    } else {
//                        obj.add(rateRule);
//                    }
//                }
//            }
//        }
//    }
//
//    private HashMap<String, GroupModel> populateGroupMap() {
//        HashMap<String, GroupModel> groupMap = new HashMap<>();
//        List<GroupModel> groups = dbSession.findAll(GroupModel.class);
//        for (GroupModel group : groups) {
//            groupMap.put(group.getId(), group);
//        }
//        return groupMap;
//    }
}
