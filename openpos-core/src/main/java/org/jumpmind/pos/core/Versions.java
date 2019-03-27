package org.jumpmind.pos.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Versions {

    List<Version> versions = new ArrayList<>();

    protected static List<InputStream> loadResources(final String name, final ClassLoader classLoader) throws IOException {
        final List<InputStream> list = new ArrayList<InputStream>();
        final Enumeration<URL> systemResources = (classLoader == null ? ClassLoader.getSystemClassLoader() : classLoader).getResources(name);
        while (systemResources.hasMoreElements()) {
            list.add(systemResources.nextElement().openStream());
        }
        return list;
    }

    @PostConstruct
    protected void init() {
        try {
            List<InputStream> resources = loadResources("openpos-version.properties", null);
            for (InputStream is : resources) {
                Properties properties = new Properties();
                properties.load(is);
                ObjectMapper m = new ObjectMapper();
                Version version = m.convertValue(properties, Version.class);
                versions.add(version);

            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public List<Version> getVersions() {
        return versions;
    }
}
