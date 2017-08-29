package org.jumpmind.jumppos.core.screen;

public class EmbeddedWebPageScreen extends DefaultScreen {

    private static final long serialVersionUID = 1L;
    
    private String url;
    
    public EmbeddedWebPageScreen(String url) {
        setType(EMBEDDED_WEB_PAGE);
        this.url = url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
        
    public String getUrl() {
        return url;
    }
}
