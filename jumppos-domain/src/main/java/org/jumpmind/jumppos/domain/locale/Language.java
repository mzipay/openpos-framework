package org.jumpmind.jumppos.domain.locale;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.jumpmind.jumppos.domain.BaseEntity;

@Entity
public class Language extends BaseEntity {

    @Id
    /**
     * ISO 639 two character alpha code
     */
    private String isoCode2Alpha;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsoCode2Alpha() {
        return isoCode2Alpha;
    }

    public void setIsoCode2Alpha(String isoCode2Alpha) {
        this.isoCode2Alpha = isoCode2Alpha;
    }

}
