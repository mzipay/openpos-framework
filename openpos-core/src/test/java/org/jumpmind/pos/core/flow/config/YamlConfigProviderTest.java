package org.jumpmind.pos.core.flow.config;

import org.junit.Test;

public class YamlConfigProviderTest {
    
    @Test
    public void testLoadResources() {
        YamlConfigProvider provider = new YamlConfigProvider();
        provider.load("pos", "flows");
        
        provider.load("selfcheckout", "flows");
        provider.load("selfcheckout", "flows/selfcheckout");
        
    }
}
