package org.jumpmind.pos.core.content;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileSystemContentProvider implements IContentProvider {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${file.system.content.dir:content/}")
    private String contentDir;

    private Map<String, ContentIndex> deviceContent = new HashMap<>();

    @Override
    public String getContentUrl(String deviceId, String key) {

        String serverUrl = "${apiServerBaseUrl}/content?contentPath=";

        StringBuilder resourceBuilder = new StringBuilder(contentDir);
        resourceBuilder.append(key);
        String resourcePath = resourceBuilder.toString();

        String restUrl = null;

        File dir = getContentDir(resourcePath);
        if (dir != null) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {

                int index = getDeviceIndex(deviceId, key, files.length);

                resourceBuilder.append("/").append(files[index].getName());
                StringBuilder restBuilder = new StringBuilder(serverUrl);
                restBuilder.append(resourceBuilder.toString());
                restUrl = restBuilder.toString();
            }
        }

        return restUrl;
    }

    private File getContentDir(String resource) {

        File dir = new File(resource);
        if (dir.exists() && dir.isDirectory()) {
            return dir;
        }

        return null;
    }

    private int getDeviceIndex(String deviceId, String resource, int size) {
        Integer index = null;
        ContentIndex contentIndex = deviceContent.get(deviceId);

        if (contentIndex == null) {
            contentIndex = new ContentIndex(resource, (1 % size));
            deviceContent.put(deviceId, contentIndex);
            index = 0;

        } else {
            index = contentIndex.getIndex(resource);
            if (index == null) {
                contentIndex.setIndex(resource, (1 % size));
                index = 0;
            } else {
                contentIndex.setIndex(resource, ((index + 1) % size));
                index %= size;
            }
        }

        return index;
    }

    private class ContentIndex {
        private Map<String, Integer> contentIndex;

        private ContentIndex() {
            contentIndex = new HashMap<>();
        }

        private ContentIndex(String resource, int index) {
            contentIndex = new HashMap<>();
            contentIndex.put(resource, index);
        }

        private void setIndex(String resource, int index) {
            contentIndex.put(resource, index);
        }

        private Integer getIndex(String resource) {
            return contentIndex.get(resource);
        }
    }

}
