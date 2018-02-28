package org.jumpmind.pos.translate;

public interface ILegacyPOSListModel {
    
    public Object getSelectedValue();
    public Object getSelectedItem();
    public void setSelectedValue(Object propValue);
    public void setSelectedItem(Object propValue);
    public int getSelectedIndex();
    public void setSelectedIndex(int index);
    
    public Object[] toArray();
    public Object firstElement();
    public boolean isEmpty();
}
