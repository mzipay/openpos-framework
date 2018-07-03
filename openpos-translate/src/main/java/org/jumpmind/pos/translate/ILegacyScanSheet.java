package org.jumpmind.pos.translate;

import java.util.List;
import java.util.Map;


public interface ILegacyScanSheet {
    public List<ILegacyScanSheetComponent> getItemList();
    public Map<String, List<ILegacyScanSheetComponent>> getCategoryMap();
    public List<String> getCategories();
}
