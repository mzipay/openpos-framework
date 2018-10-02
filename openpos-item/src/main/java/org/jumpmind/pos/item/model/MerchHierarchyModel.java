package org.jumpmind.pos.item.model;

import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name="merch_hierarchy")
public class MerchHierarchyModel extends AbstractModel {

    private static final long serialVersionUID = 1L;
    
    @Column(primaryKey=true)
    String hierarchyId;

    @Column
    String parendHierarchyId;
    
    @Column
    String levelCode;
    
    @Column
    String description;
    
    @Column
    int sortOrder;
    
    @Column
    boolean hidden;

    public void setHierarchyId(String merchHierarchyId) {
        this.hierarchyId = merchHierarchyId;
    }
    
    public String getHierarchyId() {
        return hierarchyId;
    }

    public String getParendHierarchyId() {
        return parendHierarchyId;
    }

    public void setParendHierarchyId(String parendHierarchyId) {
        this.parendHierarchyId = parendHierarchyId;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
    
    

}
