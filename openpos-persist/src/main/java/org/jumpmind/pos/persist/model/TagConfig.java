package org.jumpmind.pos.persist.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TagConfig {
    
    private final static String DEFAULT_GROUP = "DEFAULT";

    private List<TagModel> tags = new ArrayList<>();

    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }
    
    public Map<String, List<TagModel>> getTagsByGroup() {
        Map<String, List<TagModel>> tagsByGroup = new LinkedHashMap<>();
        
        for (TagModel tag : tags) {
            String group = tag.getGroup();
            if (group == null) {
                group = DEFAULT_GROUP;
            }
            
            List<TagModel> tagList = tagsByGroup.get(group);
            if (tagList == null) {
                tagList = new ArrayList<>();
                tagsByGroup.put(group, tagList);
            }
            tagList.add(tag);
        }
        
        return tagsByGroup;
        
    }
    
}
