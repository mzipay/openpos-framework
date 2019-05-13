package org.jumpmind.pos.core.content;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.ScopeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("fileSystemContentProvider")
@ConfigurationProperties(prefix = "openpos.ui.content.file-system")
@Scope("device")
public class FileSystemContentProvider implements IContentProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    List<String> providerProperties = new ArrayList<>();

    String contentDir;

    @In(scope = ScopeType.Device, required = false)
    Map<String, String> personalizationProperties;

    @In(scope = ScopeType.Device)
    IStateManager stateManager;

    private static Map<String, ContentIndex> deviceContent = new HashMap<>();

    @Override
    public String getContentUrl(String deviceId, String key) {

        String serverUrl = "${apiServerBaseUrl}/content?contentPath=";

        StringBuilder resourceBuilder = new StringBuilder(contentDir);
        resourceBuilder.append(key);
        String resourcePath = resourceBuilder.toString();

        String restUrl = null;

        ContentPath contentPath = getMostSpecificContent(resourcePath);
        if (contentPath != null) {
            File dir = contentPath.getDir();
            StringBuilder contentBuilder = new StringBuilder(contentPath.getResource());

            File[] files = getAssetFiles(dir);

            if (files != null && files.length > 0) {
                int index = getDeviceIndex(deviceId, key, files.length);
                contentBuilder.append("/").append(files[index].getName());
                StringBuilder restBuilder = new StringBuilder(serverUrl);
                restBuilder.append(contentBuilder.toString());
                restUrl = restBuilder.toString();
            } else {
                logger.warn("No files found in directory: {}", dir.getAbsolutePath());
            }
        }

        return restUrl;
    }

    private File[] getAssetFiles(File dir) {
        File[] files = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });
        return files;
    }

    private ContentPath getMostSpecificContent(String resource) {
        if (providerProperties != null && personalizationProperties != null) {
            for (int prop = providerProperties.size(); prop >= 0; prop--) {
                StringBuilder builder = new StringBuilder(resource);

                for (int i = 0; i < prop; i++) {
                    String property = providerProperties.get(i);
                    if (personalizationProperties.containsKey(property)) {
                        builder.append("/").append(personalizationProperties.get(property));
                    }
                }

                ContentPath content = getContentPath(builder.toString());
                if (content != null) {
                    return content;
                }
            }
        } else {
            ContentPath content = getContentPath(resource);
            if (content != null) {
                return content;
            }
        }

        logger.warn("No content found for resource: {}", resource);

        return null;
    }

    private ContentPath getContentPath(String resource) {
        File dir = new File(resource);
        if (dir.exists() && dir.isDirectory()) {
            return new ContentPath(dir, resource);
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

    public void setContentDir(String contentDir) {
        this.contentDir = contentDir;
    }

    public String getContentDir() {
        return this.contentDir;
    }

    public List<String> getProviderProperties() {
        return providerProperties;
    }

    public void setProviderProperties(List<String> providerProperties) {
        this.providerProperties = providerProperties;
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

    private class ContentPath {
        private File dir;
        private String resource;

        private ContentPath(File dir, String resource) {
            this.dir = dir;
            this.resource = resource;
        }

        public File getDir() {
            return dir;
        }

        public String getResource() {
            return resource;
        }
    }

}
