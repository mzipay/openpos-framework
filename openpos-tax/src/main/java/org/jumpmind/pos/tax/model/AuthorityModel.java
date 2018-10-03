package org.jumpmind.pos.tax.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Table;

/**
 * A government authority that levies sales taxes and on whose behalf the store
 * collects these sales taxes.
 * 
 * @author elong
 * 
 */
@Table(name="authority", description = "A government authority that levies sales taxes and on whose behalf the store collects these sales taxes.")
public class AuthorityModel extends AbstractModel implements Comparable<AuthorityModel> {

    private static final long serialVersionUID = 1L;

    @Column(primaryKey = true)
    private String id;

    @Column
    private String authName;

    @Column
    private String roundingCode;

    @Column
    private Integer roundingDigitsQuantity;

    private Collection<GroupRuleModel> groupRules;

    public AuthorityModel() {
    }

    public AuthorityModel(String id) {
        this.id = id;
    }

    public String toString() {
        return getClass().getSimpleName() + " " + id + ": " + authName;
    }

    public boolean equals(Object o) {
        if (o != null && o instanceof AuthorityModel) {
            AuthorityModel authority = (AuthorityModel) o;
            return authority.getId().equals(id);
        }
        return false;
    }

    public int compareTo(AuthorityModel o) {
        if (o != null && o instanceof AuthorityModel) {
            AuthorityModel authority = (AuthorityModel) o;
            return authority.getId().compareTo(id);
        }
        return -1;
    }

    public void addGroupRule(GroupRuleModel groupRule) {
        if (groupRules == null) {
            groupRules = new ArrayList<GroupRuleModel>();
        }
        groupRules.add(groupRule);
    }

    public GroupRuleModel getGroupRule(String groupId) {
    	if (groupRules != null) {
    		for (GroupRuleModel groupRule : groupRules) {
    			if (groupRule.getGroup().getId().equals(groupId)) {
    				return groupRule;
    			}
    		}
    	}
        return null;
    }

    public BigDecimal round(BigDecimal amount) {
        int digits = 2;
        if (roundingDigitsQuantity != null && roundingDigitsQuantity.intValue() >= 2) {
            digits = getRoundingDigitsQuantity();
        }

        RoundingMode mode = RoundingMode.HALF_UP;
        if (roundingCode != null) {
            if (roundingCode.equals(TaxConstants.ROUNDING_UP)) {
                mode = RoundingMode.HALF_UP;
            } else if (roundingCode.equals(TaxConstants.ROUNDING_DOWN)) {
                mode = RoundingMode.DOWN;
            } else if (roundingCode.equals(TaxConstants.ROUNDING_HALF_DOWN)) {
                mode = RoundingMode.HALF_DOWN;
            }
        }

        return amount.setScale(digits, mode);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String name) {
        this.authName = name;
    }

    public String getRoundingCode() {
        return roundingCode;
    }

    public void setRoundingCode(String roundingCode) {
        this.roundingCode = roundingCode;
    }

    public Integer getRoundingDigitsQuantity() {
        return roundingDigitsQuantity;
    }

    public void setRoundingDigitsQuantity(Integer roundingDigitsQuantity) {
        this.roundingDigitsQuantity = roundingDigitsQuantity;
    }

    public Collection<GroupRuleModel> getGroupRules() {
        return groupRules;
    }

    public void setGroupRules(Collection<GroupRuleModel> groupRules) {
        this.groupRules = groupRules;
    }

}
