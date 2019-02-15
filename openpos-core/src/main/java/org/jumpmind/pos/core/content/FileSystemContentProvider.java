package org.jumpmind.pos.core.content;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class FileSystemContentProvider implements IContentProvider {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${file.system.content.dir:/public/content/}")
    private String contentDir;
    
    @Override
    public String getContentUrl(String group, String key) {

        // TODO: Get config for relative directory instead of resources
        String serverUrl = "${apiServerBaseUrl}/content?contentPath=";

        StringBuilder resourceBuilder = new StringBuilder(contentDir);
        resourceBuilder.append(key);
        String resourcePath = resourceBuilder.toString();

        String restUrl = null;

        File dir = getContentDir(resourcePath);
        if (dir != null) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                resourceBuilder.append("/").append(files[0].getName());
                StringBuilder restBuilder = new StringBuilder(serverUrl);
                restBuilder.append(resourceBuilder.toString());
                restUrl = restBuilder.toString();
            }
        }

        return restUrl;
    }

    private File getContentDir(String resource) {

        File dir = null;
        try {
            dir = new ClassPathResource(resource).getFile();
        } catch (IOException e) {
            logger.warn("Resource does not exist: {}", resource);
        }

        return dir;
    }

}
