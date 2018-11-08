package org.jumpmind.boot;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class DeployConfig extends JSONFile {

    BootConfig config;

    public DeployConfig(URL url, BootConfig config) {
        super(url);
        this.config = config;
    }

    public String getVersion() {
        return json.getString("version");
    }

    public List<Artifact> getArtifacts() {
        List<Artifact> list = new ArrayList<>();
        JSONArray files = json.getJSONArray("artifacts");
        for (int i = 0; i < files.length(); i++) {
            JSONObject file = (JSONObject) files.get(i);
            list.add(new Artifact(file));
        }
        return list;
    }

    public boolean isOverwrite() {
        return json.has("overwrite") && json.getBoolean("overwrite");
    }

}
