package org.jumpmind.pos.persist;

import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.Logger;
import org.jumpmind.pos.persist.model.TagConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

@Configuration
public class TagConfigFactory {
    
    static Logger log = Logger.getLogger(TagConfigFactory.class);

    TagConfig tagConfig;

    @Bean
    public TagConfig getTagConfig() {
        if (tagConfig == null) {
            try {
                URL url = Thread.currentThread().getContextClassLoader().getResource("openpos-tags.yaml");
                if (url != null) {
                    log.info(String.format("Loading %s...", url.toString()));
                    InputStream queryYamlStream = url.openStream();
                    tagConfig = new Yaml(new Constructor(TagConfig.class)).load(queryYamlStream);
                    return tagConfig;
                } else {
                    log.info("Could not locate tags.yaml on the classpath.");
                    tagConfig = new TagConfig();
                    return tagConfig;
                }
            } catch (Exception ex) {
                throw new PersistException("Failed to load tags.yaml", ex);
            }
        } else {
            return tagConfig;
        }
    }
}
