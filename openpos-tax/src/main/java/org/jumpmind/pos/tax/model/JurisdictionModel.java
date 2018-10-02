package org.jumpmind.pos.tax.model;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Table;

@Table(name="jurisdiction", description = "Tax Jurisdiction")
public class JurisdictionModel extends AbstractModel {

    private static final long serialVersionUID = 1L;

    @Column(primaryKey = true)
    private String id;

    @Column
    private String geoCode;

    @Column
    private String authorityId;

    private AuthorityModel authority;

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

    public void setAuthority(AuthorityModel authority) {
        this.authority = authority;
    }

    public AuthorityModel getAuthority() {
        return authority;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }

    public String getAuthorityId() {
        return authorityId;
    }

}
