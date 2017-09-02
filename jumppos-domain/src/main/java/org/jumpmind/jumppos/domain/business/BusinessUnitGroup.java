package org.jumpmind.jumppos.domain.business;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.jumpmind.jumppos.domain.BaseEntity;

@Entity
public class BusinessUnitGroup extends BaseEntity {

    @Id
    private String id;

    private String name;
    
    private int type;

    @ManyToMany(fetch=FetchType.LAZY)
    Collection<BusinessUnit> businessUnits = new ArrayList<BusinessUnit>();;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<BusinessUnit> getBusinessUnits() {
        return businessUnits;
    }

    public void setBusinessUnits(Collection<BusinessUnit> retailStores) {
        this.businessUnits = retailStores;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public int getType() {
        return type;
    }

    

}
