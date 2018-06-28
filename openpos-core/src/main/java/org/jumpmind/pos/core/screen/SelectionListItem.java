package org.jumpmind.pos.core.screen;

import java.io.Serializable;

public class SelectionListItem implements Serializable {

    private static final long serialVersionUID = 363696137283106343L;

    private String title;

    private String body;

    public SelectionListItem() {

    }

    public SelectionListItem(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}