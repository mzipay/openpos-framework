package org.jumpmind.jumppos.domain.locale;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.jumpmind.jumppos.domain.BaseEntity;

@Entity
public class Country extends BaseEntity {

    @Id
    /**
     * ISO-3166 three character alpha code
     */
    private String isoCode3Alpha;
    /**
     * ISO-3166 two character alpha code
     */
    private String isoCode2Alpha;
    private String phoneNumberCode;
    private String name;

    public String getIsoCode3Alpha() {
        return isoCode3Alpha;
    }

    public void setIsoCode3Alpha(String isoCode3Alpha) {
        this.isoCode3Alpha = isoCode3Alpha;
    }

    public String getIsoCode2Alpha() {
        return isoCode2Alpha;
    }

    public void setIsoCode2Alpha(String isoCode2Alpha) {
        this.isoCode2Alpha = isoCode2Alpha;
    }

    public String getPhoneNumberCode() {
        return phoneNumberCode;
    }

    public void setPhoneNumberCode(String phoneNumberCode) {
        this.phoneNumberCode = phoneNumberCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
