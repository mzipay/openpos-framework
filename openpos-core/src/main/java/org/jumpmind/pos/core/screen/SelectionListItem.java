package org.jumpmind.pos.core.screen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectionListItem implements Serializable {

    private static final long serialVersionUID = 363696137283106343L;

    private String title;

    private List<String> body;

    public SelectionListItem() {
        this.body = new ArrayList<String>();
    }

    public SelectionListItem(String title, String body) {
        this.title = title;
        this.body = new ArrayList<String>();
        this.body.add(body);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getBody() {
        return body;
    }

    public void setBody(List<String> body) {
        this.body = body;
    }
    
    public void addLine(String line) {
        this.body.add(line);
    }

}