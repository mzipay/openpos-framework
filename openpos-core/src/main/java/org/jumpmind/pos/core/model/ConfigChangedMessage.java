package org.jumpmind.pos.core.model;

import java.util.List;

import org.jumpmind.pos.core.Version;
import org.jumpmind.pos.util.model.Message;

public class ConfigChangedMessage extends Message {

    private static final long serialVersionUID = 1L;

    private String theme;

    private ClientConfiguration configuration;
    
    private List<Version> versions;
    
    public ConfigChangedMessage() {
    }

    public ConfigChangedMessage(String theme, ClientConfiguration configuration, List<Version> versions) {
        super(MessageType.ConfigChanged);
        this.theme = theme;
        this.configuration = configuration;
        this.versions = versions;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public ClientConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ClientConfiguration configuration) {
        this.configuration = configuration;
    }
    
    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }
    
    public List<Version> getVersions() {
        return versions;
    }

}
