package org.jumpmind.boot;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

public class JSONFile {

    JSONObject json;

    BootConfig bootConfig;

    public JSONFile(URL url) {
        load(url);
    }

    @SuppressWarnings("unchecked")
    public <T extends JSONFile> T load(URL url) {
        InputStream is = null;
        Scanner s = null;
        try {
            is = url.openStream();
            s = new Scanner(is);
            s.useDelimiter("\\A");
            json = new JSONObject(s.hasNext() ? s.next() : "");
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(s);
            close(is);
        }
        return (T) this;
    }

    public void store(File file) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            json.write(writer, 2, 0);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(writer);
        }
    }

    protected void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
