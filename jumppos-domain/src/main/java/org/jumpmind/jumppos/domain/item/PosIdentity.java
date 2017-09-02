package org.jumpmind.jumppos.domain.item;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.BaseEntity;

@Entity
public class PosIdentity extends BaseEntity {

    @Id
    private String id;

    @OneToOne
    private Item item;

    public PosIdentity() {
    }
    
    public PosIdentity(String posId) {
        this.id = posId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

}
