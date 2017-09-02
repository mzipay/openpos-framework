package org.jumpmind.jumppos.domain.item;

import javax.persistence.Entity;

@Entity
public class AgeRestrictionRule extends SalesRestriction {

    private Integer minimumAge;
    private Integer minimumSellingAge;

    public Integer getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(Integer minimumAge) {
        this.minimumAge = minimumAge;
    }

    public Integer getMinimumSellingAge() {
        return minimumSellingAge;
    }

    public void setMinimumSellingAge(Integer minimumSellingAge) {
        this.minimumSellingAge = minimumSellingAge;
    }

}
