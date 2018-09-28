package org.jumpmind.pos;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.persist.Query;
import org.jumpmind.pos.persist.cars.TestPersistCarsConfig;
import org.jumpmind.pos.tax.model.AuthorityModel;
import org.jumpmind.pos.tax.model.GroupModel;
import org.jumpmind.pos.tax.model.GroupRuleModel;
import org.jumpmind.pos.tax.model.JurisdictionModel;
import org.jumpmind.pos.tax.model.RateRuleModel;
import org.jumpmind.pos.tax.model.TaxRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestPersistCarsConfig.class})
public class TaxRepositoryTest {

	/* Designed for use with registerdb in OrposToOpenposUtility */
	
	@Autowired
	TaxRepository taxRepository;
	
	@Autowired
	@Qualifier("taxSession")
	DBSession dbSession;
	
	private Query<RateRuleModel> taxRateRuleLookup2 = new Query<RateRuleModel>().named("taxRateRuleLookup2").result(RateRuleModel.class);
    
    private Query<GroupRuleModel> taxGroupRuleLookup = new Query<GroupRuleModel>().named("taxGroupRuleLookup").result(GroupRuleModel.class);

	@Test
	public void matchAuthoritiesTest() {
		List<JurisdictionModel> jurisdictions = taxRepository.findTaxJurisdictions("700010040");
		AuthorityModel authAct = dbSession.findByNaturalId(AuthorityModel.class, "8");
		JurisdictionModel jurisdiction = jurisdictions.get(0);
		AuthorityModel authExp = jurisdiction.getAuthority();
		assertEquals(authExp.getId(), jurisdiction.getAuthorityId());
		assertEquals(authAct.compareTo(authExp), 0);
	}
	
	@Test
	public void matchGroupRulesTest() {
		List<JurisdictionModel> jurisdictions = taxRepository.findTaxJurisdictions("700010040");
		AuthorityModel authAct = dbSession.findByNaturalId(AuthorityModel.class, "8");
		String authorityId = authAct.getId();
		JurisdictionModel jurisdiction = jurisdictions.get(0);
		AuthorityModel authExp = jurisdiction.getAuthority();
		List<GroupRuleModel> groupRulesExp = (List<GroupRuleModel>) authExp.getGroupRules();
		List<GroupRuleModel> groupRulesAct = dbSession.query(taxGroupRuleLookup, authorityId);
		for (int i = 0; i < groupRulesAct.size(); i++) {
			//assertEquals(groupRulesExp.get(i).toString(), groupRulesAct.get(i).toString());
			assertEquals(groupRulesExp.get(i).getAuthority(), authExp);
			assertEquals(groupRulesExp.get(i).getAuthorityId(), authExp.getId());
		}
		//assertEquals(groupRulesExp,(List<GroupRule>) authAct.getGroupRules());
	}
	
	@Test
	public void matchRateRulesTest() {
		List<JurisdictionModel> jurisdictions = taxRepository.findTaxJurisdictions("700010040");
		JurisdictionModel jurisdiction = jurisdictions.get(0);
		AuthorityModel authority = jurisdiction.getAuthority();
		List<GroupRuleModel> groupRules = (List<GroupRuleModel>) authority.getGroupRules();
		GroupRuleModel groupRule = groupRules.get(0);
		String groupId = groupRule.getGroupId();
		String authorityId = authority.getId();
		
		Map<String, Object> params = new HashMap<>();	
		params.put("groupId", groupId);
		params.put("authorityId", authorityId);
		List<RateRuleModel> rateRulesAct = dbSession.query(taxRateRuleLookup2, params);
		List<RateRuleModel> rateRulesExp = (List<RateRuleModel>) groupRule.getRateRules();
		for (int i = 0; i < rateRulesAct.size(); i++) {
			//assertEquals(rateRulesAct.get(i).toString(), rateRulesExp.get(i).toString());
		}
	}
	
	@Test
	public void checkResultTest() {
		List<JurisdictionModel> jurisdictions = taxRepository.findTaxJurisdictions("51115040");
		for (JurisdictionModel jurisdiction : jurisdictions) {
			if (jurisdiction.getAuthorityId().equals("2398")) {
				GroupRuleModel groupRule = jurisdiction.getAuthority().getGroupRule("10002");
				RateRuleModel rateRule = groupRule.getFirstRateRule();
				
				assertEquals(jurisdiction.getAuthority().getAuthName(), "CALIFORNIA");
				assertEquals(groupRule.getGroup().getGroupName(), "Candy");
				assertEquals(rateRule.getId(), "2");
				assertEquals(rateRule.getMaxTaxableAmount(), null);
				assertEquals(rateRule.getMinTaxableAmount(), BigDecimal.ZERO);
				assertEquals(rateRule.getRateRuleSequenceNumber(), new Integer(0));
			}
		}
	}
	
	@Test
	public void checkResultWideAuthorityTest() {
		List<JurisdictionModel> jurisdictions = taxRepository.findTaxJurisdictions("430370620");
		for (JurisdictionModel jurisdiction : jurisdictions) {
			if (jurisdiction.getAuthorityId().equals("34975")) {
				GroupRuleModel groupRule = jurisdiction.getAuthority().getGroupRule("10015");
				RateRuleModel rateRule = groupRule.getFirstRateRule();
				
				assertEquals(jurisdiction.getAuthority().getAuthName(), "TENNESSEE");
				assertEquals(groupRule.getGroup().getGroupName(), "Women's Accessories");
				assertEquals(rateRule.getId(), "1");
				assertEquals(rateRule.getMaxTaxableAmount(), new BigDecimal(1600));
				assertEquals(rateRule.getMinTaxableAmount(), BigDecimal.ZERO);
				assertEquals(rateRule.getRateRuleSequenceNumber(), new Integer(0));
			}
		}
	}
	

	
	@Test
	public void checkResultWideGroupTest() {
		List<JurisdictionModel> jurisdictions = taxRepository.findTaxJurisdictions("10730150");
		for (JurisdictionModel jurisdiction : jurisdictions) {
			if (jurisdiction.getAuthorityId().equals("11")) {
				GroupRuleModel groupRule = jurisdiction.getAuthority().getGroupRule("535");
				RateRuleModel rateRule = groupRule.getFirstRateRule();
				
				assertEquals(jurisdiction.getAuthority().getAuthName(), "ALABAMA");
				assertEquals(groupRule.getGroup().getGroupName(), "CONTAINERS");
				assertEquals(rateRule.getId(), "2");
				assertEquals(rateRule.getMaxTaxableAmount(), null);
				assertEquals(rateRule.getMinTaxableAmount(), BigDecimal.ZERO);
				assertEquals(rateRule.getRateRuleSequenceNumber(), new Integer(0));
			}
		}
	}
	

	
	@Test
	public void checkResultWideRateRuleTest() {
		List<JurisdictionModel> jurisdictions = taxRepository.findTaxJurisdictions("130010030");
		for (JurisdictionModel jurisdiction : jurisdictions) {
			if (jurisdiction.getAuthorityId().equals("7488")) {
				GroupRuleModel groupRule = jurisdiction.getAuthority().getGroupRule("110");
				RateRuleModel rateRule = groupRule.getFirstRateRule();
				
				assertEquals(jurisdiction.getAuthority().getAuthName(), "IDAHO");
				assertEquals(groupRule.getGroup().getGroupName(), "CASUAL BLOUSES"); 
				assertEquals(rateRule.getId(), "2");
				assertEquals(rateRule.getMaxTaxableAmount(), null);
				assertEquals(rateRule.getMinTaxableAmount(), BigDecimal.ZERO);
				assertEquals(rateRule.getRateRuleSequenceNumber(), new Integer(0));
			}
		}
	}
	
//	@Test
//	public void checkResultLongTest() {
//		for (int i = 0; i < 150; i++) {
//		List<Jurisdiction> jurisdictions = taxRepository.findTaxJurisdictions("51115040");
//		for (Jurisdiction jurisdiction : jurisdictions) {
//			if (jurisdiction.getAuthorityId().equals("2398")) {
//				GroupRule groupRule = jurisdiction.getAuthority().getGroupRule("10002");
//				RateRule rateRule = groupRule.getFirstRateRule();
//				
//				assertEquals(jurisdiction.getAuthority().getAuthName(), "CALIFORNIA");
//				assertEquals(groupRule.getGroup().getGroupName(), "Candy");
//				assertEquals(rateRule.getId(), "2");
//				assertEquals(rateRule.getMaxTaxableAmount(), BigDecimal.ZERO);
//				assertEquals(rateRule.getMinTaxableAmount(), BigDecimal.ZERO);
//				assertEquals(rateRule.getRateRuleSequenceNumber(), new Integer(0));
//			}
//		}
//		}
//	}
//	
//	@Test
//	public void checkResultWideAuthorityLongTest() {
//		for (int i = 0; i < 150; i++) {
//		List<Jurisdiction> jurisdictions = taxRepository.findTaxJurisdictions("430370620");
//		for (Jurisdiction jurisdiction : jurisdictions) {
//			if (jurisdiction.getAuthorityId().equals("34975")) {
//				GroupRule groupRule = jurisdiction.getAuthority().getGroupRule("10015");
//				RateRule rateRule = groupRule.getFirstRateRule();
//				
//				assertEquals(jurisdiction.getAuthority().getAuthName(), "TENNESSEE");
//				assertEquals(groupRule.getGroup().getGroupName(), "Women's Accessories");
//				assertEquals(rateRule.getId(), "1");
//				assertEquals(rateRule.getMaxTaxableAmount(), new BigDecimal(1600));
//				assertEquals(rateRule.getMinTaxableAmount(), BigDecimal.ZERO);
//				assertEquals(rateRule.getRateRuleSequenceNumber(), new Integer(0));
//			}
//		}
//		}
//	}
//	
//	@Test
//	public void checkResultWideGroupLongTest() {
//		for (int i = 0; i < 150; i++) {
//		List<Jurisdiction> jurisdictions = taxRepository.findTaxJurisdictions("10730150");
//		for (Jurisdiction jurisdiction : jurisdictions) {
//			if (jurisdiction.getAuthorityId().equals("11")) {
//				GroupRule groupRule = jurisdiction.getAuthority().getGroupRule("535");
//				RateRule rateRule = groupRule.getFirstRateRule();
//				
//				assertEquals(jurisdiction.getAuthority().getAuthName(), "ALABAMA");
//				assertEquals(groupRule.getGroup().getGroupName(), "CONTAINERS");
//				assertEquals(rateRule.getId(), "2");
//				assertEquals(rateRule.getMaxTaxableAmount(), BigDecimal.ZERO);
//				assertEquals(rateRule.getMinTaxableAmount(), BigDecimal.ZERO);
//				assertEquals(rateRule.getRateRuleSequenceNumber(), new Integer(0));
//			}
//		}
//		}
//	}
//	
//	@Test
//	public void checkResultWideRateRuleLongTest() {
//		for (int i = 0; i < 150; i++) {
//		List<Jurisdiction> jurisdictions = taxRepository.findTaxJurisdictions("130010030");
//		for (Jurisdiction jurisdiction : jurisdictions) {
//			if (jurisdiction.getAuthorityId().equals("7488")) {
//				GroupRule groupRule = jurisdiction.getAuthority().getGroupRule("110");
//				RateRule rateRule = groupRule.getFirstRateRule();
//				
//				assertEquals(jurisdiction.getAuthority().getAuthName(), "IDAHO");
//				assertEquals(groupRule.getGroup().getGroupName(), "CASUAL BLOUSES"); 
//				assertEquals(rateRule.getId(), "2");
//				assertEquals(rateRule.getMaxTaxableAmount(), BigDecimal.ZERO);
//				assertEquals(rateRule.getMinTaxableAmount(), BigDecimal.ZERO);
//				assertEquals(rateRule.getRateRuleSequenceNumber(), new Integer(0));
//			}
//		}
//		}
//	}
	
	@Test
	public void matchGroupTest() {
		List<JurisdictionModel> jurisdictions = taxRepository.findTaxJurisdictions("700010040");
		JurisdictionModel jurisdiction = jurisdictions.get(0);
		AuthorityModel authority = jurisdiction.getAuthority();
		List<GroupRuleModel> groupRules = (List<GroupRuleModel>) authority.getGroupRules();
		for (GroupRuleModel groupRule : groupRules) {
			String groupID = groupRule.getGroupId();		
			
			GroupModel groupAct = dbSession.findByNaturalId(GroupModel.class, groupID);
			GroupModel groupExp = groupRule.getGroup();
			assertEquals(groupAct, groupExp);
		}
	}

}
