package org.jumpmind.boot;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

public class PropertiesFile {

    Properties properties = new Properties();

    File file;

    public PropertiesFile() {
    }

    public PropertiesFile(File propertiesFile, boolean mustExist) {
        this.file = propertiesFile;
        if (this.file.exists() || mustExist) {
            load(propertiesFile, mustExist);
        }
    }

    public PropertiesFile(URL propertiesFile) {
        load(propertiesFile);
    }
    
    public  <T extends PropertiesFile> T store() {
        return store(this.file);
    }
    
    @SuppressWarnings("unchecked")
    public  <T extends PropertiesFile> T store(File file) {
        if (file != null) {
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(file);
                properties.store(os, "saved at " + new Date());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } finally {
                close(os);
            }
        }
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends PropertiesFile> T load(File propertiesFile, boolean mustExist) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(propertiesFile);
            properties.load(is);
            return (T) this;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            close(is);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends PropertiesFile> T load(URL url) {
        InputStream is = null;
        try {
            is = url.openStream();
            properties.load(is);
            return (T) this;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            close(is);
        }
    }

    protected void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
            }
        }
    }
    

}
