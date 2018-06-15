package org.jumpmind.pos.core.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TestStates {
    
    public static class HomeState implements IState {
        
        @Out(scope=ScopeType.Conversation, required=false)
        private String optionalInjectionFullfiled = "optionalInjectionFullfiled";
        
        public HomeState() {
            
        }
        
        @In(scope=ScopeType.Node)
        private IStateManager stateManager;

        @Override
        public void arrive(Action action) {
            
        }
    }
    
    public static class HelpState implements IState {
        
        @In(scope=ScopeType.Node)
        private IStateManager stateManager;

        @Override
        public void arrive(Action action) {
            
        }
    }
    
    public static class AboutState implements IState {
        
        @In(scope=ScopeType.Node)
        private IStateManager stateManager;
        
        @Override
        public void arrive(Action action) {
            
        }
    }     
    
    public static class SellState implements IState {
        
        @In(scope=ScopeType.Node)
        private IStateManager stateManager;

        @Override
        public void arrive(Action action) {
            
        }
        
        @ActionHandler
        public void onCustomerLookupComplete(Action action) {
            
        }
    } 
    
    public static class CustomerState implements IState {
        
        @In(scope=ScopeType.Config)
        private String customerFlowType;
        
        @In(scope=ScopeType.Node)
        private IStateManager stateManager;
        @Out(scope=ScopeType.Flow)
        private String selectedCustomer;
        @Out(scope=ScopeType.Conversation)
        private String customerFlowTypeWorked;
        
        @Override
        public void arrive(Action action) {
            assertEquals("LOYALTY", this.customerFlowType);
            this.customerFlowTypeWorked = "customerFlowTypeWorked";
            this.selectedCustomer = "customer1234";
        }
        
        @ActionHandler
        public void onCustomerSignupComplete(Action action) {
            arrive(action.getCausedBy());
        }
    } 
    
    public static class CustomerSearchState implements IState {
        
        @In(scope=ScopeType.Node)
        private IStateManager stateManager;
        
        @Override
        public void arrive(Action action) {
            
        }
    } 
    
    public static class CustomerSignupState implements IState {
        
        @In(scope=ScopeType.Node)
        private IStateManager stateManager;
        
        @Override
        public void arrive(Action action) {
            
        }
    }
    
    public static abstract class AbstractRepostActionState implements IState {
        @In(scope=ScopeType.Node)
        private IStateManager stateManager;        
        @ActionHandler
        public void onAnyAction(Action action) {
            stateManager.doAction("RepostActionStateGotToSell");
        }
    } 
    
    public static class RepostActionState extends AbstractRepostActionState {
        
        @In(scope=ScopeType.Node)
        private IStateManager stateManager;
        
        @Override
        public void arrive(Action action) {
            assertNotNull(stateManager);
            stateManager.doAction(new Action("ShouldGoTo_AbstractRepostActionState.AnyAction"));
        }
    }
    
    public static class InjectionFailedState implements IState {
        
        @In(scope=ScopeType.Node)
        private String failedInjection;
        
        @Override
        public void arrive(Action action) {
            
        }
    }
    
    public static class OptionalInjectionState implements IState {
        
        @In(scope=ScopeType.Conversation, required=false)
        private String optionalInjectionMissing;
        @In(scope=ScopeType.Conversation, required=false)
        private String optionalInjectionFullfiled;
        
        @Override
        public void arrive(Action action) {
            assertNull("optionalInjectionMissing", optionalInjectionMissing);
            assertNotNull("optionalInjectionFullfiled", optionalInjectionFullfiled);            
        }
    } 
    
    public static class ActionTestingState implements IState {
        
        @In(scope=ScopeType.Node)
        private IStateManager stateManager;
        
        @Out(scope=ScopeType.Conversation)
        private boolean specificActionMethodCalled = false;
        @Out(scope=ScopeType.Conversation)
        private boolean anyActionMethodCalled = false;

        @Override
        public void arrive(Action action) {
            
        }
        
        @ActionHandler
        public void onSpecificAction(Action action) {
            specificActionMethodCalled = true;
        }
        @ActionHandler
        public void onAnyAction(Action action) {
            anyActionMethodCalled = true;
            if (action.getName().equals("Done")) {
                stateManager.doAction(action);
            }
        }
    }
    
    public static class TestScopesState implements IState {
        
        @Out(scope=ScopeType.Conversation)
        private String conversationScopeValue = "conversationScopeValue";
        @Out(scope=ScopeType.Session)
        private String sessionScopeValue = "sessionScopeValue";
        @Out(scope=ScopeType.Node)
        private String nodeScopeValue = "nodeScopeValue";

        @Override
        public void arrive(Action action) {
            
        }
        
    }
    
    public static class TransitionInterceptionState implements IState {
        @In(scope=ScopeType.Node)
        private IStateManager stateManager;
        
        @Out(scope=ScopeType.Conversation)
        private boolean onSellCalled = false;
        @Out(scope=ScopeType.Conversation)
        private boolean anyActionMethodCalled = false;

        @Override
        public void arrive(Action action) {
            
        }
        
        @ActionHandler
        public void onSell(Action action) {
            onSellCalled = true;
            stateManager.doAction(action);
        }
        @ActionHandler
        public void onAnyAction(Action action) {
            anyActionMethodCalled = true;
        }
    }
    
    public static abstract class AbstractStackOverflowState implements IState {
        
        @In(scope=ScopeType.Node)
        private IStateManager stateManager;        
        
        @Override
        public void arrive(Action action) {
            stateManager.doAction(action);
        }        
        
        @ActionHandler
        public void onAnyAction(Action action) {
            stateManager.doAction(action);
        }
    }
    
    public static class StackOverflowState extends AbstractStackOverflowState {

    }    
    
//    
//    public static class DashboardState extends AbstractRemapState {
//        @In(scope=ScopeType.Node)
//        private IStateManager stateManager;
//        
//        @ActionHandler
//        public void onAnyAction(Action action) {
//            stateManager.doAction(action);
//        }
//    }    
//    public static class ItemLookupState extends AbstractRemapState {
//        @In(scope=ScopeType.Node)
//        private IStateManager stateManager;
//        
//        @Override
//        public void arrive(Action action) {
//        }        
//        
//        public void onAnyAction(Action action) {
//            super.onAnyAction(action);
//        }
//    }    
//    public static class VendorState extends AbstractRemapState {
//        @In(scope=ScopeType.Node)
//        private IStateManager stateManager;
//        
//        @Override
//        public void arrive(Action action) {
//        }
//        
//        public void onAnyAction(Action action) {
//            super.onAnyAction(action);
//        }        
//    }    



}
