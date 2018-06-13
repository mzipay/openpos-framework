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

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.collections.CollectionUtils;
import org.jumpmind.pos.cache.service.impl.ICache;
import org.jumpmind.pos.cache.service.impl.JCSCache;
import org.jumpmind.pos.core.flow.config.FlowConfig;
import org.jumpmind.pos.core.flow.config.StateConfig;
import org.jumpmind.pos.core.flow.config.SubTransition;
import org.jumpmind.pos.core.flow.ui.UIManager;
import org.jumpmind.pos.core.screen.Screen;
import org.jumpmind.pos.core.service.IScreenService;
import org.jumpmind.pos.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

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
    
    @Autowired(required=false)
    private List<? extends ITransitionStep> transitionSteps;
    
    @Autowired(required=false)
    private List<? extends ISessionTimeoutListener> sessionTimeoutListeners;
    
    private ApplicationState applicationState = new ApplicationState();
    
//    @JsonIgnoreType
//    public abstract class MixInIgnoreType {
//    }
//    public class MyMixIgnoreCache {
//        
//    }
    
    @Autowired
    UIManager uiManager;

    private String appId;
    private String nodeId;
    private FlowConfig initialFlowConfig;
    
    private AtomicReference<Date> lastInteractionTime = new AtomicReference<Date>(new Date());
    
    private long sessionTimeoutMillis = 0;

    public void init(String appId, String nodeId) {
        this.appId = appId;
        this.nodeId = nodeId;
        this.uiManager.setStateManager(this);
        
//        boolean loadedFromSave = false;
//        
//        try {            
//            File f = new File("./openpos-state.json");
//            if (f.exists()) {                
//                String json = new String(Files.readAllBytes(Paths.get("./openpos-state.json")));
//                applicationState = mapper.readValue(json, ApplicationState.class);
//                loadedFromSave = true;
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        
        applicationState.getScope().setNodeScope("stateManager", this);
        
//        if (!loadedFromSave) {            
            applicationState.setCurrentContext(new StateContext(initialFlowConfig, null, null));
//        }
        
        
        screenService.setApplicationState(applicationState);
        
        transitionTo(new Action("Startup"), initialFlowConfig.getInitialState());
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
        if (applicationState.getCurrentContext() == null) {
            throw new FlowException("There is no applicationState.getCurrentContext() on this StateManager.  HINT: States should use @In to get the StateManager, not @Autowired.");
        }        
        
        if (enterSubStateConfig != null && resumeSuspendedState != null) {
            throw new FlowException("enterSubStateConfig and resumeSuspendedState should not BOTH be provided at the same time. enterSubStateConfig implies entering a subState, "
                    + "while resumeSuspendedState implies "
                    + "existing a subState. These two should be be happening at the same time.");
        }
        
        
        if (applicationState.getCurrentContext().getState() != null) {
            performOutjections(applicationState.getCurrentContext().getState());
        }
        
        TransitionResult transitionResult = executeTransition(applicationState.getCurrentContext(), newState, action);
        if (transitionResult == TransitionResult.PROCEED) {          
            ObjectMapper mapper = new ObjectMapper();
            {        
                mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                mapper.enableDefaultTyping(DefaultTyping.NON_FINAL);
            }

                try {
                    filterApplicationStateForSerialization(applicationState);
                    String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(applicationState);
                    PrintWriter writer = new PrintWriter("openpos-state.json", "UTF-8");
                    writer.println(json);
                    System.out.println(json);
                    writer.close();                
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                try {            
                    File f = new File("./openpos-state.json");
                    if (f.exists()) {                
                        String json = new String(Files.readAllBytes(Paths.get("./openpos-state.json")));
                        applicationState = mapper.readValue(json, ApplicationState.class);
                        applicationState.getScope().setNodeScope("stateManager", this);
                        if (this.applicationState.getCurrentContext().getState() != null) {                            
                            performInjections(this.applicationState.getCurrentContext().getState());
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }   
            
            
            
            boolean enterSubState = enterSubStateConfig != null;
            boolean exitSubState = resumeSuspendedState != null;
            String returnActionName = applicationState.getCurrentContext().getReturnActionName();
            stateManagerLogger.logStateTransition(applicationState.getCurrentContext().getState(), newState, action, returnActionName, enterSubState, exitSubState);            
            
            if (enterSubState) {
                applicationState.getStateStack().push(applicationState.getCurrentContext());
                applicationState.setCurrentContext(new StateContext(enterSubStateConfig.getSubFlowConfig(), action));
                applicationState.getCurrentContext().setReturnActionName(enterSubStateConfig.getReturnActionName());
            } else if (exitSubState) { 
                applicationState.setCurrentContext(resumeSuspendedState);
            }
            
            applicationState.getCurrentContext().setState(newState);
            
            performInjections(newState);
            
            if (resumeSuspendedState == null || returnActionName == null) {
                applicationState.getCurrentContext().getState().arrive(action);
            } else {
                Action returnAction = new Action(returnActionName, action.getData());    
                returnAction.setCausedBy(action);
                if (actionHandler.canHandleAction(applicationState.getCurrentContext().getState(), returnAction)) {
                    actionHandler.handleAction(applicationState.getCurrentContext().getState(), returnAction);
                }  else {
                    throw new FlowException(String.format("Unexpected return action from substate: \"%s\". No @ActionHandler %s.on%s() method found.", 
                            returnAction.getName(), applicationState.getCurrentContext().getState().getClass().getName(), returnAction.getName()));                    
                }
            }
        } else {
            applicationState.getCurrentContext().getState().arrive(action);
        }
        
    }
    
    public void performInjections(Object stateOrStep) {
        injector.performInjections(stateOrStep, applicationState.getScope(), applicationState.getCurrentContext());
    }
    
    public void performOutjections(Object stateOrStep) {
        outjector.performOutjections(stateOrStep, applicationState.getScope(), applicationState.getCurrentContext());        
    }    

    protected TransitionResult executeTransition(StateContext sourceStateContext, IState newState, Action action) {
        if (CollectionUtils.isEmpty(transitionSteps)) {
            return TransitionResult.PROCEED;
        }
        
        applicationState.setCurrentTransition(new Transition(transitionSteps, sourceStateContext, newState)); 
        
        TransitionResult result = applicationState.getCurrentTransition().execute(this, action); // This will block.
        applicationState.setCurrentTransition(null);
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
        return applicationState.getCurrentContext().getState();
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
        

        
         
        
        if (applicationState.getCurrentTransition() != null)  {
            applicationState.getCurrentTransition().handleAction(action);
            return;
        }
        
        FlowConfig flowConfig = applicationState.getCurrentContext().getFlowConfig();
        
        StateConfig stateConfig = applicationState.findStateConfig(flowConfig);
        if (handleTerminatingState(action, stateConfig)) {
            return;
        }
        
        validateStateConfig(applicationState.getCurrentContext().getState(), stateConfig);
        
        Class<? extends IState> transitionStateClass = stateConfig.getActionToStateMapping().get(action.getName());
        Class<? extends IState> globalTransitionStateClass = flowConfig.getActionToStateMapping().get(action.getName());
        SubTransition subStateConfig = stateConfig.getActionToSubStateMapping().get(action.getName());
        SubTransition globalSubStateConfig = flowConfig.getActionToSubStateMapping().get(action.getName());
        
        if (actionHandler.canHandleAction(applicationState.getCurrentContext().getState(), action)) {
            handleAction(action);
        } else if (transitionStateClass != null) {
            transitionToState(action, transitionStateClass);
        } else if (subStateConfig != null) {
            transitionToSubState(action, subStateConfig);
        } else if (actionHandler.canHandleAnyAction(applicationState.getCurrentContext().getState())) {
            actionHandler.handleAnyAction(applicationState.getCurrentContext().getState(), action);
        } else if (globalTransitionStateClass != null) {
            transitionToState(action, globalTransitionStateClass);
        } else if (globalSubStateConfig != null) {
            transitionToSubState(action, globalSubStateConfig);
        } else {
            throw new FlowException(String.format("Unexpected action \"%s\". Either no @ActionHandler %s.on%s() method found, or no withTransition(\"%s\"...) defined in the flow config.", 
                    action.getName(), applicationState.getCurrentContext().getState().getClass().getName(), action.getName(), action.getName()));                    
        }
    }
    
    private void filterApplicationStateForSerialization(ApplicationState appStateForSerialization) {
        appStateForSerialization.getScope().getNodeScope().remove("stateManager");
        Set<String> keys = new HashSet<>(appStateForSerialization.getScope().getNodeScope().keySet());
        
        for (String key : keys) {
            Object value = appStateForSerialization.getScope().getNodeScope().get(key).getValue();
            if (ICache.class.isAssignableFrom(value.getClass())
                    || value.getClass().toString().contains("ContextServiceClient")) {
                appStateForSerialization.getScope().getNodeScope().remove(key);
            }
        }
        
        appStateForSerialization.setCurrentTransition(null);
        
//        if (appStateForSerialization.getCurrentTransition() != null) {
//            
//            appStateForSerialization.getCurrentTransition().setStateManager(null);
//            appStateForSerialization.getCurrentTransition().setCurrentTransitionStep(null);
//            appStateForSerialization.getCurrentTransition().setStepIndex(0);
//        }
        
        
    }

    public void setScopeValue(ScopeType scopeType, String name, Object value) {
        applicationState.getScope().setScopeValue(scopeType, name, value);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getScopeValue(ScopeType scopeType, String name) {
        return (T)applicationState.getScopeValue(scopeType, name);
    }
        
    @SuppressWarnings("unchecked")
    public <T> T getScopeValue(String name) {
        return (T)applicationState.getScopeValue(name);
    }
    
    protected boolean handleAction(Object state, Action action) {
        return actionHandler.handleAction(state, action);
    }
    
    protected boolean handleAction(Action action) {        
        return handleAction(applicationState.getCurrentContext().getState(), action);
    }

    protected boolean handleTerminatingState(Action action, StateConfig stateConfig) {
        if (stateConfig == null || stateConfig.getActionToStateMapping() == null) {
            return false;
        }
        
        Class<? extends IState> targetStateClass = stateConfig.getActionToStateMapping().get(action.getName());
        
        if (CompleteState.class == targetStateClass) {
            if (!applicationState.getStateStack().isEmpty()) {                
                StateContext suspendedState = applicationState.getStateStack().pop();
                transitionTo(action, suspendedState.getState(), null, suspendedState);
            } else {                
                throw new FlowException("No suspended state to return to for terminating action " + action + " from state " + applicationState.getCurrentContext().getState());
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
        StateConfig newStateConfig = applicationState.getCurrentContext().getFlowConfig().getStateConfig(newStateClass);
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
        applicationState.getScope().clearConversationScope();
        transitionTo(new Action("EndConversation"), applicationState.getCurrentContext().getFlowConfig().getInitialState());
    }

    @Override
    public void endSession() {
        applicationState.getScope().clearSessionScope();
    }

    public void setInitialFlowConfig(FlowConfig initialFlowConfig) {
        this.initialFlowConfig = initialFlowConfig;
    }

    @Override
    public void showScreen(Screen screen) {
        if (applicationState.getCurrentContext() == null) {
            throw new FlowException("There is no applicationState.getCurrentContext() on this StateManager.  HINT: States should use @In(scope=ScopeType.Node) to get the StateManager, not @Autowired.");
        }
        if (applicationState.getCurrentContext().getState() != null && applicationState.getCurrentContext().getState() instanceof IScreenInterceptor) {
            screen = ((IScreenInterceptor)applicationState.getCurrentContext().getState()).intercept(screen);            
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

    @Override
    public ApplicationState getApplicationState() {
        return applicationState;
    }
}
