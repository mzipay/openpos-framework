package org.jumpmind.pos.translate;

public interface ILegacyScanSheetComponent {
    int getComponentId();
    String getItemId();
    String getCategoryId();
    String getDescription();
    boolean isCategory();
    String getParentCategoryId();
    int getSequence();
    String getImageLocation();
}
