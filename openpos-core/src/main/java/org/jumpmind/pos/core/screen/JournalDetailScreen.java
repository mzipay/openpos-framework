package org.jumpmind.pos.core.screen;

import org.jumpmind.pos.core.template.SellTemplate;

public class JournalDetailScreen extends Screen {
    
    private static final long serialVersionUID = 1L;
    
    private String text;
    
    private int index;
    
    private int size;
    
    public JournalDetailScreen() {
        setScreenType(ScreenType.JournalDetail);
        setTemplate(new SellTemplate());
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    
}
