package org.jumpmind.pos.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestNodeId {
    
    
    @Test
    public void testParseNodeId() {
        {            
            NodeId nodeId = new NodeId("1-2");
            assertEquals("1", nodeId.getLocationId());
            assertEquals("2", nodeId.getWorkstationId());
        }
        {            
            NodeId nodeId = new NodeId("11-22");
            assertEquals("11", nodeId.getLocationId());
            assertEquals("22", nodeId.getWorkstationId());
        }
    }
    
    @Test(expected = PosServerException.class)
    public void testInvalidNodeId1() {            
        new NodeId("1-");
    }
    
    @Test(expected = PosServerException.class)
    public void testInvalidNodeId2() {            
        new NodeId("-2");
    }
    
    @Test(expected = PosServerException.class)
    public void testInvalidNodeId3() {            
        new NodeId("nodash");
 
    }
    
    @Test(expected = PosServerException.class)
    public void testInvalidNodeId4() {            
        new NodeId("");
    }
    
    @Test(expected = PosServerException.class)
    public void testInvalidNodeId5() {            
        new NodeId(null);
    }
    
    
}
