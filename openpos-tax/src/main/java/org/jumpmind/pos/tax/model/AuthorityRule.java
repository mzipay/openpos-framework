package org.jumpmind.pos.tax.model;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;
import org.jumpmind.pos.persist.Table;

@Table(description = "Tax Authority Rule")
public class AuthorityRule extends Entity {

    @Column(primaryKey = true)
    private String id;

    @Column
    private String geoCode;
    
    @Column
    private String authorityId;

    private Authority taxAuthority;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    
    public void setGeoCode(String geoCode) {
        this.geoCode = geoCode;
    }
    
    public String getGeoCode() {
        return geoCode;
    }

    public void setTaxAuthority(Authority taxAuthority) {
        this.taxAuthority = taxAuthority;
    }

    public Authority getTaxAuthority() {
        return taxAuthority;
    }
    
    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }
    
    public String getAuthorityId() {
        return authorityId;
    }

}
