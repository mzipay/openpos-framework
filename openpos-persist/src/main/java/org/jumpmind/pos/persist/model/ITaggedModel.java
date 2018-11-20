package org.jumpmind.pos.persist.model;

import java.util.Map;

public interface ITaggedModel {

    String getTagValue(String tagName);

    void setTagValue(String tagName, String tagValue);

    void setTags(Map<String, String> tags);

    void clearTagValue(String tagName);

    Map<String, String> getTags();

}