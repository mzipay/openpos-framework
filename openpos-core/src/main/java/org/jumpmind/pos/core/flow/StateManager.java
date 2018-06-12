/**
 * license agreements.  See the NOTICE file distributed
 * with this work for additional information regarding
 * copyright ownership.  JumpMind Inc licenses this file
 * to you under the GNU General Public License, version 3.0 (GPLv3)
 * (the "License"); you may not use this file except in compliance
 * with the License.
 *
 * You should have received a copy of the GNU General Public License,
 * version 3.0 (GPLv3) along with this library; if not, see
 * <http://www.gnu.org/licenses/>.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jumpmind.pos.core.flow;

import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.collections.CollectionUtils;
import org.jumpmind.pos.core.flow.config.FlowConfig;
import org.jumpmind.pos.core.flow.config.StateConfig;
import org.jumpmind.pos.core.flow.config.SubTransition;
import org.jumpmind.pos.core.flow.ui.UIManager;
import org.jumpmind.pos.core.screen.Screen;
import org.jumpmind.pos.core.service.IScreenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component()
@org.springframework.context.annotation.Scope("prototype")
public class StateManager implements IStateManager {

    final Logger logger = LoggerFactory.getLogger(getClass());
    final Logger loggerGraphical = LoggerFactory.getLogger(getClass().getName() + ".graphical");
    private final StateManagerLogger stateManagerLogger = new StateManagerLogger(loggerGraphical);

    @Autowired
    private IScreenService screenService;
    
    @Autowired
    private ActionHandlerImpl actionHandler;

    @Autowired
    private Injector injector;
    
    @Autowired
    private Outjector outjector;
    
//    @Autowired(required=false)
//    private List<? extends IStateInterceptor> stateInterceptors;
    
    @Autowired(required=false)
    private List<? extends ITransitionStep> transitionSteps;
    
    @Autowired(required=false)
    private List<? extends ISessionTimeoutListener> sessionTimeoutListeners;    
    
    @Autowired
    UIManager uiManager;

    private String appId;
    private String nodeId;
    private Scope scope = new Scope();
    private Deque<StateContext> stateStack = new LinkedList<>();
    private FlowConfig initialFlowConfig;
    private StateContext currentContext;
    private Transition currentTransition;
    
    private AtomicReference<Date> lastInteractionTime = new AtomicReference<Date>(new Date());
    
    private long sessionTimeoutMillis = 0;

    public void init(String appId, String nodeId) {
        this.appId = appId;
        this.nodeId = nodeId;
        this.uiManager.setStateManager(this);
        this.currentContext = new StateContext(initialFlowConfig, null, null);
        this.scope.setNodeScope("stateManager", this);
        transitionTo(null, initialFlowConfig.getInitialState());
    }

    protected void transitionTo(Action action, StateConfig stateConfig) {
        IState newState = buildState(stateConfig);
        transitionTo(action, newState);
    }

    @Override
    public void transitionTo(Action action, IState newState) {
        transitionTo(action, newState, null, null);
    }
    
    protected void transitionTo(Action action, IState newState, SubTransition enterSubStateConfig, StateContext resumeSuspendedState) {
        if (this.currentContext == null) {
            throw new FlowException("There is no currentContext on this StateManager.  HINT: States should use @In to get the StateManager, not @Autowired.");
        }        
        
        if (enterSubStateConfig != null && resumeSuspendedState != null) {
            throw new FlowException("enterSubStateConfig and resumeSuspendedState should not BOTH be provided at the same time. enterSubStateConfig implies entering a subState, "
                    + "while resumeSuspendedState implies "
                    + "existing a subState. These two should be be happening at the same time.");
        }
        
        
        if (currentContext.getState() != null) {
            performOutjections(currentContext.getState());
        }
        
        TransitionResult transitionResult = executeTransition(currentContext, newState, action);
        if (transitionResult == TransitionResult.PROCEED) {            
            boolean enterSubState = enterSubStateConfig != null;
            boolean exitSubState = resumeSuspendedState != null;
            String returnActionName = currentContext.getReturnActionName();
            stateManagerLogger.logStateTransition(currentContext.getState(), newState, action, returnActionName, enterSubState, exitSubState);            
            
            if (enterSubState) {
                stateStack.push(currentContext);
                currentContext = new StateContext(enterSubStateConfig.getSubFlowConfig(), action);
                currentContext.setReturnActionName(enterSubStateConfig.getReturnActionName());
            } else if (exitSubState) { 
                currentContext = resumeSuspendedState;
            }
            
            currentContext.setState(newState);
            
            performInjections(newState);
            
            if (resumeSuspendedState == null || returnActionName == null) {
                currentContext.getState().arrive(action);
            } else {
                Action returnAction = new Action(returnActionName, action.getData());    
                returnAction.setCausedBy(action);
                if (actionHandler.canHandleAction(currentContext.getState(), returnAction)) {
                    actionHandler.handleAction(currentContext.getState(), returnAction);
                }  else {
                    throw new FlowException(String.format("Unexpected return action from substate: \"%s\". No @ActionHandler %s.on%s() method found.", 
                            returnAction.getName(), currentContext.getState().getClass().getName(), returnAction.getName()));                    
                }
            }
        } else {
            currentContext.getState().arrive(action);
        }
        
    }
    
    public void performInjections(Object stateOrStep) {
        injector.performInjections(stateOrStep, scope, currentContext);
    }
    
    public void performOutjections(Object stateOrStep) {
        outjector.performOutjections(stateOrStep, scope, currentContext);        
    }    

    protected void addConfigScope(Map<String, ScopeValue> extraScope) {
        if (currentContext != null && currentContext.getFlowConfig() != null
                && currentContext.getFlowConfig().getConfigScope() != null) {
            for (Map.Entry<String, Object> entry : currentContext.getFlowConfig().getConfigScope().entrySet()) {
                extraScope.put(entry.getKey(), new ScopeValue(entry.getValue()));
            }
        }
    }
    
    protected TransitionResult executeTransition(StateContext sourceStateContext, IState newState, Action action) {
        if (CollectionUtils.isEmpty(transitionSteps)) {
            return TransitionResult.PROCEED;
        }
        
        this.currentTransition = new Transition(transitionSteps, sourceStateContext, newState); 
        TransitionResult result = currentTransition.execute(this, action);
        this.currentTransition = null;
        return result;
    }

    protected IState buildState(StateConfig stateConfig) {
        IState state;
        try {
            state = stateConfig.getStateClass().newInstance();
        } catch (Exception ex) {
            throw new FlowException("Failed to instantiate state " + stateConfig.getStateName() + " class " + stateConfig.getStateClass(),
                    ex);
        }
        return state;
    }

    @Override
    public IState getCurrentState() {
        return currentContext.getState();
    }

    @Override
    public void refreshScreen() {
        showScreen(screenService.getLastScreen(appId, nodeId));
        showScreen(screenService.getLastDialog(appId, nodeId));
    }

    // Could come from a UI or a running state..
    @Override
    public void doAction(String actionName) {
        doAction(actionName, null);
    }

    @Override
    public void doAction(String actionName, Map<String, String> params) {
        Action action = new Action(actionName, params);
        doAction(action);
    }
    
    @Override
    public void keepAlive() {
        lastInteractionTime.set(new Date());         
    }

    @Override
    public void doAction(Action action) {
        lastInteractionTime.set(new Date());
        
        if (currentTransition != null)  {
            currentTransition.handleAction(action);
            return;
        }
        
        FlowConfig flowConfig = currentContext.getFlowConfig();
        
        StateConfig stateConfig = findStateConfig(flowConfig);
        if (handleTerminatingState(action, stateConfig)) {
            return;
        }
        
        validateStateConfig(currentContext.getState(), stateConfig);
        
        Class<? extends IState> transitionStateClass = stateConfig.getActionToStateMapping().get(action.getName());
        Class<? extends IState> globalTransitionStateClass = flowConfig.getActionToStateMapping().get(action.getName());
        SubTransition subStateConfig = stateConfig.getActionToSubStateMapping().get(action.getName());
        SubTransition globalSubStateConfig = flowConfig.getActionToSubStateMapping().get(action.getName());
        
        if (actionHandler.canHandleAction(currentContext.getState(), action)) {
            handleAction(action);
        } else if (transitionStateClass != null) {
            transitionToState(action, transitionStateClass);
        } else if (subStateConfig != null) {
            transitionToSubState(action, subStateConfig);
        } else if (actionHandler.canHandleAnyAction(currentContext.getState())) {
            actionHandler.handleAnyAction(currentContext.getState(), action);
        } else if (globalTransitionStateClass != null) {
            transitionToState(action, globalTransitionStateClass);
        } else if (globalSubStateConfig != null) {
            transitionToSubState(action, globalSubStateConfig);
        } else {
            throw new FlowException(String.format("Unexpected action \"%s\". Either no @ActionHandler %s.on%s() method found, or no withTransition(\"%s\"...) defined in the flow config.", 
                    action.getName(), currentContext.getState().getClass().getName(), action.getName(), action.getName()));                    
        }
    }

    private StateConfig findStateConfig(FlowConfig flowConfig) {
        StateConfig stateConfig = flowConfig.getStateConfig(currentContext.getState());
        Iterator<StateContext> itr = stateStack.iterator();
        while (stateConfig == null && itr.hasNext()) {
            StateContext context = itr.next();
            stateConfig = context.getFlowConfig().getStateConfig(currentContext.getState());

        }
        return stateConfig;
    }
    
    public void setScopeValue(ScopeType scopeType, String name, Object value) {
        switch (scopeType) {
            case Node:
                scope.setNodeScope(name, value);
                break;
            case Session:
                scope.setSessionScope(name, value);
                break;
            case Conversation:
                scope.setConversationScope(name, value);
                break;
            case Flow:
                currentContext.setFlowScope(name, value);
                break;
            default:
                throw new FlowException("Invalid scope " + scopeType);
        }
    }
    
    public <T> T getScopeValue(ScopeType scopeType, String name) {
        ScopeValue scopeValue = null;
        switch (scopeType) {
            case Node:
            case Session:
            case Conversation:
                scopeValue = scope.getScopeValue(scopeType, name);
                break;
            case Flow:
                scopeValue = currentContext.getFlowScope().get(name);
                break;
            default:
                throw new FlowException("Invalid scope " + scopeType);
        }
        
        if (scopeValue != null) {
            return scopeValue.getValue();
        } else {
            return null;
        }
    }
        
    @SuppressWarnings("unchecked")
    public <T> T getScopeValue(String name) {
        ScopeValue value = scope.resolve(name);
        if (value != null) {
            return (T) value.getValue();
        } else {
            value = currentContext.resolveScope(name);
            if (value != null) {
                return (T) value.getValue();    
            } else {
                return null;
            }
        }
    }
    
    protected boolean handleAction(Object state, Action action) {
        return actionHandler.handleAction(state, action);
    }
    
    protected boolean handleAction(Action action) {        
        return handleAction(currentContext.getState(), action);
    }

    protected boolean handleTerminatingState(Action action, StateConfig stateConfig) {
        if (stateConfig == null || stateConfig.getActionToStateMapping() == null) {
            return false;
        }
        
        Class<? extends IState> targetStateClass = stateConfig.getActionToStateMapping().get(action.getName());
        
        if (CompleteState.class == targetStateClass) {
            if (!stateStack.isEmpty()) {                
                StateContext suspendedState = stateStack.pop();
                transitionTo(action, suspendedState.getState(), null, suspendedState);
            } else {                
                throw new FlowException("No suspended state to return to for terminating action " + action + " from state " + currentContext.getState());
            }
            return true;
        } else {
            return false;
        }
    }

    protected void transitionToSubState(Action action, SubTransition subStateConfig) {
        Class<? extends IState> subState = subStateConfig.getSubFlowConfig().getInitialState().getStateClass();
        try {
            transitionTo(action, subState.newInstance(), subStateConfig, null);
        } catch (Exception ex) {
            throw new FlowException("Failed to create and transition to initial subState " + subState, ex);
        }
    }

    protected void transitionToState(Action action, Class<? extends IState> newStateClass) {
        StateConfig newStateConfig = currentContext.getFlowConfig().getStateConfig(newStateClass);
        if (newStateConfig != null) {
            transitionTo(action, newStateConfig);
        } else {
            throw new FlowException(String.format("No State found for class \"%s\"", newStateClass));
        }
    }

    protected void validateStateConfig(IState state, StateConfig stateConfig) {
        if (stateConfig == null) {
            throw new FlowException("No configuration found for state. \"" + 
                    state + "\" This state needs to be mapped in a IFlowConfigProvider implementation. ");
        }
    }

    @Override
    public void endConversation() {
        scope.clearConversationScope();
        transitionTo(null, currentContext.getFlowConfig().getInitialState());
    }

    @Override
    public void endSession() {
        scope.clearSessionScope();
    }

    public void setInitialFlowConfig(FlowConfig initialFlowConfig) {
        this.initialFlowConfig = initialFlowConfig;
    }

    @Override
    public void showScreen(Screen screen) {
        if (this.currentContext == null) {
            throw new FlowException("There is no currentContext on this StateManager.  HINT: States should use @In(scope=ScopeType.Node) to get the StateManager, not @Autowired.");
        }
        if (this.currentContext.getState() != null && this.currentContext.getState() instanceof IScreenInterceptor) {
            screen = ((IScreenInterceptor)this.currentContext.getState()).intercept(screen);            
        }
        
        if (screen != null) {
            sessionTimeoutMillis = screen.getSessionTimeoutMillis();
        } 
        
        screenService.showScreen(appId, nodeId, screen);
    }

    @Override
    public String getNodeId() {
        return nodeId;
    }
    
    @Override
    public String getAppId() {
        return appId;
    }
    

    @Override
    public IUI getUI() {
        return uiManager;
    }
    
    // called from a Timer thread.
    public void checkSessionTimeout() {
        if (sessionTimeoutMillis > 0) {            
            long inactiveMillis = System.currentTimeMillis() - lastInteractionTime.get().getTime();
            if (inactiveMillis > sessionTimeoutMillis) {
                sessionTimeout();
            }
        }
    }

    protected void sessionTimeout() {
        try {
            logger.info(String.format("Node %s session timed out.", nodeId));
            if (!CollectionUtils.isEmpty(sessionTimeoutListeners)) {
                for (ISessionTimeoutListener sessionTimeoutListener : sessionTimeoutListeners) {
                    sessionTimeoutListener.onSessionTimeout(this);
                }
            }
        } catch (Exception ex) {
            logger.error("Failed to process the session timeout", ex);
        }
    }

//    protected void transitionProceed() {
//        latch.countDown();
//    }
//    
//    protected void transitionCancel() {
//        latch.countDown();
//    }    
}
