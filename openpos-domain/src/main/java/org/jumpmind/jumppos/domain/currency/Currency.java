package org.jumpmind.jumppos.domain.currency;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.BaseEntity;
import org.jumpmind.jumppos.domain.locale.Country;

@Entity
public class Currency extends BaseEntity {

    @Id
    /**
     * ISO 4217 Currency code - Three character alpha
     */
    private String isoCode3Alpha;
    /**
     * 
     * ISO 4217 Currency code - three character numeric
     */
    private String isoCodeNumeric;

    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Denomination> denominations;

    @OneToOne
    private Country issuingCountry;

    private int scale;

    public String getIsoCode3Alpha() {
        return isoCode3Alpha;
    }

    public void setIsoCode3Alpha(String isoCode3Alpha) {
        this.isoCode3Alpha = isoCode3Alpha;
    }

    public String getIsoCodeNumeric() {
        return isoCodeNumeric;
    }

    public void setIsoCodeNumeric(String isoCodeNumeric) {
        this.isoCodeNumeric = isoCodeNumeric;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Denomination> getDenominations() {
        return denominations;
    }

    public void setDenominations(List<Denomination> denominations) {
        this.denominations = denominations;
    }

    public Country getIssuingCountry() {
        return issuingCountry;
    }

    public void setIssuingCountry(Country issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

}
