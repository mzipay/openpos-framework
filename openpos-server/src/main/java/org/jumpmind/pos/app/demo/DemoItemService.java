package org.jumpmind.pos.app.demo;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.jumpmind.pos.context.ContextException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

@Component
public class DemoItemService {
    
    private static Logger log = LoggerFactory.getLogger(DemoItemService.class);
    
    private DemoItems demoItems;
    private Map<String, DemoItem> demoItemsById = new HashMap<>();

    public DemoItem lookupItem(String itemId) {
        return demoItemsById.get(itemId);
    }
    
    public List<DemoItem> searchItems(String search) {
        List<DemoItem> searchResults = new ArrayList<>();
        final String searchLower = search.toLowerCase();
        for (DemoItem item : demoItems.getItems()) {
            if (item.getDescription().toLowerCase().contains(searchLower)) {
                searchResults.add(item);
            }
        }
        
        return searchResults;
    }
    
    @PostConstruct
    public void loadDemoItems() {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource("org/jumpmind/pos/app/demo/demo-items.yaml");
            if (url != null) {
                log.info(String.format("Loading %s...", url.toString()));
                InputStream queryYamlStream = url.openStream();
                demoItems = new Yaml(new Constructor( DemoItems.class)).load(queryYamlStream);
                demoItemsById = demoItems.getItems().stream().collect(Collectors.toMap(DemoItem::getItemId, Function.identity()));
            } else {
                log.info("Could not locate demo-items.yaml on the classpath.");
                demoItems = new DemoItems();
            }
        } catch (Exception ex) {
            throw new ContextException("Failed to load tags.yaml", ex);
        }
    }    

}
