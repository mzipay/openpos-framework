package org.jumpmind.pos.context.model;

import java.util.Map;

public interface ITaggedElement {

    String getTagValue(String tagName);

    void setTagValue(String tagName, String tagValue);

    void setTags(Map<String, String> tags);

    void clearTagValue(String tagName);

    Map<String, String> getTags();

}