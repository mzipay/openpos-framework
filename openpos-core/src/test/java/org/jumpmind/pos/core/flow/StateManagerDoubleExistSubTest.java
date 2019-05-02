package org.jumpmind.pos.core.flow;

import static org.junit.Assert.assertEquals;

import org.jumpmind.pos.server.model.Action;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StateManagerDoubleExistSubTest {
    
    @InjectMocks
    Injector injector;    

    @Test
    public void testDoubleSubTransitionExit() throws Exception {

        
        StateManager stateManager = StateManagerTestUtils.buildStateManager(injector, "testflows/test-double-sub-exit-flow.yml");
        
        assertEquals(OrderDetailState.class, stateManager.getCurrentState().getClass());
        
        stateManager.doAction("BagPromptRequired");
        
        assertEquals(BagScanState.class, stateManager.getCurrentState().getClass());
        
        stateManager.doAction("BagNotFound");
        
        assertEquals(BagNotFoundState.class, stateManager.getCurrentState().getClass());
        
        stateManager.doAction("NoBagsAdded");
        
        assertEquals(OrderDetailState.class, stateManager.getCurrentState().getClass());
    }
    
    public static class OrderDetailState  {
        @OnArrive
        public void arrive(Action action) {
        }
        
        @ActionHandler
        public void onNoBagsAdded(Action action) {
            
        }
    }    
    public static class BagScanState  {
        @OnArrive
        public void arrive(Action action) {
        }
    }    
    public static class BagNotFoundState  {
        @OnArrive
        public void arrive(Action action) {
        }
    }    
     
    
    
}
