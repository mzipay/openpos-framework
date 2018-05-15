package org.jumpmind.pos.context.model;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;
import org.jumpmind.pos.persist.Table;

@Table(name="tag")
public class TagModel extends Entity {
    public static final String TAG_ALL = "*";

    @Column(size="128", primaryKey=true)    
    private String tagName;
    @Column
    private String tagGroup;
    @Column
    private int tagLevel;
    
    public String getTagName() {
        return tagName;
    }
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
    public String getTagGroup() {
        return tagGroup;
    }
    public void setTagGroup(String tagGroup) {
        this.tagGroup = tagGroup;
    }
    public int getTagLevel() {
        return tagLevel;
    }
    public void setTagLevel(int tagLevel) {
        this.tagLevel = tagLevel;
    }

    
}
