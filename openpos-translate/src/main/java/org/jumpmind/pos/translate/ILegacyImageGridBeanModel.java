package org.jumpmind.pos.translate;


public interface ILegacyImageGridBeanModel {
    public ILegacyScanSheet getScanSheet();
    public void setCategoryId(String categoryId);
    public String getCategoryId();
    public ILegacyImageGridBeanModel getCurrentCategoryModel();
    public int getCurrentPageNo();
    public void setCurrentPageNo(int pageNo);
    public void setSelectedItemID(String selectedItemID);
    public void setSelectedItemIsCategory(boolean isCategory);
}
