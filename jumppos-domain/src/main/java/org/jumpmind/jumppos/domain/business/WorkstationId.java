package org.jumpmind.jumppos.domain.business;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

@Embeddable
public class WorkstationId implements Serializable {

    private static final long serialVersionUID = 1L;
    
    String id;
    
    @OneToOne
    BusinessUnit retailStore;
    
    public WorkstationId() {
    }
    
    public WorkstationId(String id, BusinessUnit retailStore) {
        this.id = id;
        this.retailStore = retailStore;
    }


    public void setId(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }

    public void setRetailStore(BusinessUnit retailStore) {
        this.retailStore = retailStore;
    }
    
    public BusinessUnit getRetailStore() {
        return retailStore;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((retailStore == null) ? 0 : retailStore.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WorkstationId other = (WorkstationId) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (retailStore == null) {
            if (other.retailStore != null)
                return false;
        } else if (!retailStore.equals(other.retailStore))
            return false;
        return true;
    }

    
}
