package org.jumpmind.pos.tax.model;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;
import org.jumpmind.pos.persist.Table;

/**
 * A group of Items for which a TaxAuthority defines TaxGroupRules.
 * 
 * @author elong
 * 
 */
@Table(description = "A group of Items for which a TaxAuthority defines TaxGroupRules.")
public class TaxableGroup extends Entity implements Comparable<TaxableGroup> {

    @Column(primaryKey = true)
    private String id;

    @Column()
    private String name;

    @Column()
    private String description;

    @Column()
    private String receiptPrintCode;

    public TaxableGroup() {
    }

    public TaxableGroup(String id) {
        this.id = id;
    }

    public TaxableGroup(String id, String name, String description, String receiptPrintCode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.receiptPrintCode = receiptPrintCode;
    }

    public String toString() {
        return getClass().getSimpleName() + " " + id + ": " + name;
    }

    public boolean equals(Object o) {
        if (o != null && o instanceof TaxableGroup) {
            TaxableGroup taxableGroup = (TaxableGroup) o;
            return taxableGroup.getId().equals(id);
        }
        return false;
    }

    public int compareTo(TaxableGroup o) {
        if (o != null && o instanceof TaxableGroup) {
            TaxableGroup taxableGroup = (TaxableGroup) o;
            return taxableGroup.getId().compareTo(id);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReceiptPrintCode() {
        return receiptPrintCode;
    }

    public void setReceiptPrintCode(String receiptPrintCode) {
        this.receiptPrintCode = receiptPrintCode;
    }

}
