package org.jumpmind.pos.persist;

import static org.junit.Assert.assertEquals;

import org.jumpmind.pos.persist.model.TagConfig;
import org.junit.Test;

public class TagConfigFactoryTest {

    @Test 
    public void testLoadTags() throws Exception {
        TagConfig tagConfig = new TagConfigFactory().getTagConfig();
        assertEquals(8, tagConfig.getTags().size());
        
        assertEquals("REGION", tagConfig.getTags().get(0).getName());
        assertEquals("LOCATION", tagConfig.getTags().get(0).getGroup());
        
        assertEquals("STATE", tagConfig.getTags().get(2).getName());
        assertEquals("LOCATION", tagConfig.getTags().get(2).getGroup());        
        assertEquals(3, tagConfig.getTags().get(2).getLevel());
        
        assertEquals("APP_PROFILE", tagConfig.getTags().get(7).getName());        
    }
    
}
