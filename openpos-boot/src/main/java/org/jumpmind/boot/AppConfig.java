package org.jumpmind.boot;

import java.io.File;
import java.net.MalformedURLException;

public class AppConfig extends PropertiesFile {

    BootConfig bootConfig;

    public AppConfig(BootConfig config) {
        super(new File(config.getAppDir() + "/app.conf"), false);
        this.bootConfig = config;
    }

    public String getVersion() {
        return properties.getProperty("version", "?");
    }
    
    public void setVersion(String version) {
        this.properties.setProperty("version", version);
    }
    
    public void refresh() {
        this.load(this.file, false);
    }

    public DeployConfig getCurrentDeployConfig() {
        File file = new File(String.format("%s/%s/deploy.json", this.bootConfig.getAppDir(), this.getVersion()));
        if (file.exists()) {
            DeployConfig config;
            try {
                config = new DeployConfig(file.toURI().toURL(), this.bootConfig);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            return config;
        } else {
            throw new RuntimeException("Could not find a deploy.json for the current application");
        }
    }    

}
