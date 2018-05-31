package org.jumpmind.pos.context.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class TagCalculator {

    protected static final int DISQUALIFIED = Integer.MIN_VALUE;
    
    public ITaggedElement getMostSpecific(List<? extends ITaggedElement> taggedElements, Map<String, String> specifiedTags, TagConfig tagConfig) {
        
        List<TagValue> tagValues = buildTagValues(specifiedTags, tagConfig);
        
        int bestMatchScore = -1;
        ITaggedElement mostSpecific = null;
        
        for (ITaggedElement taggedElement : taggedElements) {
            int score = 0;
            for (TagValue tagValue : tagValues) {
                String taggedElementValue = taggedElement.getTagValue(tagValue.tagName);
                if (taggedElementValue == null 
                        || StringUtils.equals(taggedElementValue, TagModel.TAG_ALL)) {
                    continue;
                } else if (StringUtils.equals(taggedElementValue, tagValue.tagValue)) {
                    score += tagValue.weight;
                } else {
                    score = DISQUALIFIED;
                    break;
                }
            }
            
            if (score > bestMatchScore) {
                mostSpecific = taggedElement;
                bestMatchScore = score;
            }
        }
        
        return mostSpecific;
    }

    protected List<TagValue> buildTagValues(Map<String, String> specifiedTags, TagConfig tagConfig) {
        Map<String, List<TagModel>> tagsByGroup = tagConfig.getTagsByGroup();
        List<TagValue> tagValues = new ArrayList<>(); 
        
        tagsByGroup.values().forEach(tagDefinitions -> {
            int counter = 1;
            for (TagModel tagDefinition : tagDefinitions) {                
                TagValue tagValue = new TagValue();
                tagValue.tagName = tagDefinition.getName();
                tagValue.tagValue = specifiedTags.get(tagValue.tagName);
                tagValue.weight = counter++; 
                tagValues.add(tagValue);
            }
        });
        return tagValues;
    }
    
     class TagValue {
        private String tagName;
        private String tagValue;
        private int weight;
    }
    
}
