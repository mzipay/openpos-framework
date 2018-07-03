package org.jumpmind.pos.translate;


public interface ILegacyImageGridBeanModel {
    public ILegacyScanSheet getScanSheet();
    public void setCategoryId(String categoryId);
    public String getCategoryId();
    public ILegacyImageGridBeanModel getCurrentCategoryModel();
}
