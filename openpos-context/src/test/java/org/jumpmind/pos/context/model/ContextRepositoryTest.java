package org.jumpmind.pos.context.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.jumpmind.pos.persist.cars.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class})
public class ContextRepositoryTest {
    
    @Autowired
    ContextRepository contextRepository;

    @Test
    public void testFindNode() {
        {            
            Node node = contextRepository.findNode("100-1");
            assertNotNull(node);
            assertEquals("100-1", node.getNodeId());
            assertEquals("WORKSTATION", node.getNodeType());
            assertEquals("Store 100 Register 1", node.getDescription());
            assertEquals("N_AMERICA", node.getTagValue("REGION"));
            assertEquals("US", node.getTagValue("COUNTRY"));
            assertEquals("OH", node.getTagValue("STATE"));
            assertEquals("100", node.getTagValue("STORE_NUMBER"));
            assertEquals("REGULAR", node.getTagValue("STORE_TYPE"));
            assertEquals("WORKSTATION", node.getTagValue("DEVICE_TYPE"));
            assertEquals("Metl", node.getTagValue("BRAND_ID"));
            assertEquals("POS", node.getTagValue("APP_PROFILE"));
        }
    }
    @Test
    public void findNodesByTag() {
        {            
            List<Node> nodes = contextRepository.findNodesByTag("STORE_NUMBER", "500");
            assertNotNull(nodes);
            assertEquals(2, nodes.size());
            {
                Node node = nodes.get(0);
                assertEquals("WORKSTATION", node.getNodeType());
                assertEquals("Store 500 Register 1", node.getDescription());
                assertEquals("N_AMERICA", node.getTagValue("REGION"));
                assertEquals("US", node.getTagValue("COUNTRY"));
                assertEquals("CA", node.getTagValue("STATE"));
                assertEquals("500", node.getTagValue("STORE_NUMBER"));
                assertEquals("POPUP", node.getTagValue("STORE_TYPE"));
                assertEquals("WORKSTATION", node.getTagValue("DEVICE_TYPE"));
                assertEquals("Metl", node.getTagValue("BRAND_ID"));
                assertEquals("POS", node.getTagValue("APP_PROFILE"));
            }
            {
                Node node = nodes.get(1);
                assertEquals("WORKSTATION", node.getNodeType());
                assertEquals("Store 500 Register 2", node.getDescription());
                assertEquals("N_AMERICA", node.getTagValue("REGION"));
                assertEquals("US", node.getTagValue("COUNTRY"));
                assertEquals("CA", node.getTagValue("STATE"));
                assertEquals("500", node.getTagValue("STORE_NUMBER"));
                assertEquals("POPUP", node.getTagValue("STORE_TYPE"));
                assertEquals("WORKSTATION", node.getTagValue("DEVICE_TYPE"));
                assertEquals("Metl", node.getTagValue("BRAND_ID"));
                assertEquals("POS", node.getTagValue("APP_PROFILE"));
            }
        }
    }
    
    @Test
    public void testLoadConfigs() {
        Node node = contextRepository.findNode("100-1");
        List<ConfigModel> welcomeTextConfigs = contextRepository.findConfigs(new Date(), "pos.welcome.text");
        assertEquals(10,  welcomeTextConfigs.size());
        for (ConfigModel configModel : welcomeTextConfigs) {
            assertEquals("pos.welcome.text", configModel.getConfigName());
        }
        
        assertEquals("Welcome global!", welcomeTextConfigs.get(0).getConfigValue());
        assertEquals("Welcome store 100 in OH, USA!", welcomeTextConfigs.get(9).getConfigValue());
    }
    
    @Test 
    public void testLoadTags() throws Exception {
        TagConfig tagConfig = ContextRepository.getTagConfig();
        assertEquals(8, tagConfig.getTags().size());
        
        assertEquals("REGION", tagConfig.getTags().get(0).getName());
        assertEquals("LOCATION", tagConfig.getTags().get(0).getGroup());
        
        assertEquals("STATE", tagConfig.getTags().get(2).getName());
        assertEquals("LOCATION", tagConfig.getTags().get(2).getGroup());        
        assertEquals(3, tagConfig.getTags().get(2).getLevel());
        
        assertEquals("APP_PROFILE", tagConfig.getTags().get(7).getName());        
    }
}
