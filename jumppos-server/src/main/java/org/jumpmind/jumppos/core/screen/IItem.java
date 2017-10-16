package org.jumpmind.jumppos.core.screen;

public interface IItem {
    public Integer getIndex();
    public void setIndex(Integer index);
    public String getID();
    public void setID(String id);
    public String getDescription();
    public void setDescription(String description);
    public String getSubtitle();
    public void setSubtitle(String subtitle);
    public String getAmount();
    public void setAmount(String amount);
}
