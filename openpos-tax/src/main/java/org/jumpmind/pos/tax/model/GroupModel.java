package org.jumpmind.pos.tax.model;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Table;

/**
 * A group of Items for which a TaxAuthority defines TaxGroupRules.
 * 
 * @author elong
 * 
 */
@Table(name="group", description = "A group of Items for which a TaxAuthority defines TaxGroupRules.")
public class GroupModel extends AbstractModel implements Comparable<GroupModel> {

    private static final long serialVersionUID = 1L;

    @Column(primaryKey = true)
    private String id;

    @Column()
    private String groupName;

    @Column()
    private String description;

    @Column()
    private String receiptPrintCode;

    public GroupModel() {
    }

    public GroupModel(String id) {
        this.id = id;
    }

    public GroupModel(String id, String name, String description, String receiptPrintCode) {
        this.id = id;
        this.groupName = name;
        this.description = description;
        this.receiptPrintCode = receiptPrintCode;
    }

    public String toString() {
        return getClass().getSimpleName() + " " + id + ": " + groupName;
    }

    public boolean equals(Object o) {
        if (o != null && o instanceof GroupModel) {
            GroupModel group = (GroupModel) o;
            return group.getId().equals(id);
        }
        return false;
    }

    public int compareTo(GroupModel o) {
        if (o != null && o instanceof GroupModel) {
            GroupModel group = (GroupModel) o;
            return group.getId().compareTo(id);
        }
        return -1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String name) {
        this.groupName = name;
    }

    public String getReceiptPrintCode() {
        return receiptPrintCode;
    }

    public void setReceiptPrintCode(String receiptPrintCode) {
        this.receiptPrintCode = receiptPrintCode;
    }

}
