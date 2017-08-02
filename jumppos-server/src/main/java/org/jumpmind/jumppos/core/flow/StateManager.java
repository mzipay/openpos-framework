/**
 * Licensed to JumpMind Inc under one or more contributor
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
package org.jumpmind.jumppos.core.flow;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.jumpmind.jumppos.core.flow.config.FlowConfig;
import org.jumpmind.jumppos.core.flow.config.StateConfig;
import org.jumpmind.jumppos.core.model.IScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component()
@org.springframework.context.annotation.Scope("prototype")
public class StateManager implements IStateManager {
    
    Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private IScreenService screenService;
    
    private String nodeId;
    private Scope scope = new Scope();
    private FlowConfig flowConfig;
    private IState currentState;
    
    private ObjectMapper jsonMapper = new ObjectMapper();
    
    public void init() {
        transitionTo(flowConfig.getInitialState());
    }

    protected void transitionTo(StateConfig stateConfig) {
        IState newState = buildState(stateConfig);
        transitionTo(newState);
    }
    
    protected void transitionTo(IState newState) {
        if (currentState != newState) {
            logger.info("Transition from " + currentState + " to " + newState);
        }
        performInjections(newState, null);
        performPostContruct(newState);
        currentState = newState;
        currentState.arrive();
    }
    
    @Override
    public IState getCurrentState() {
        return currentState;
    }

    protected IState buildState(StateConfig stateConfig) {
        IState state;
        try {
            state = stateConfig.getStateClass().newInstance();
        } catch (Exception ex) {
            throw new FlowException("Failed to instantiate state " + 
                    stateConfig.getStateName() + " class " + stateConfig.getStateClass(), ex);
        }
        return state;
    }
    
    @Override
    public IScreen getLastScreen() {
        return screenService.getLastScreen(nodeId);
    }
    
    @Override
    public void refreshScreen() {
        showScreen(getLastScreen());        
    }

    // Could come from a UI or a running state..
    @Override
    public void doAction(String actionName) {
        doAction(actionName, null);
    }

    @Override
    public void doAction(String actionName, Map<String, String> params) {
        // TODO this needs to be put on the action event queue and processed
        // on main run loop thread.
        Action action = new Action(actionName, null, params);
        doAction(action);
    }
    
    @Override
    public void doAction(Action action) {
        StateConfig stateConfig = flowConfig.getStateConfig(currentState);
        String newStateName = stateConfig.getActionToStateMapping().get(action.getName());
        if (newStateName != null) {  
            StateConfig newStateConfig = flowConfig.getStateConfig(newStateName);
            if (newStateConfig != null) {                
                transitionTo(newStateConfig);
            } else {
                throw new FlowException("No State found for name " + newStateName);
            }
        } else {            
            IState savedCurrentState = currentState;
            boolean handled  = handleAction(currentState, action);
            if (handled) {   
                if (savedCurrentState == currentState) {
                    // state did not change, reassert the current state.
                    transitionTo(currentState);
                }
            } else {
                System.out.println("Unexpeted action " + action);                
            }
        }
    }
    
    @Override
    public void endConversation() {
        scope.clearConversationScope();
        transitionTo(flowConfig.getInitialState());
    }

    @Override
    public void endSession() {
        scope.clearSessionScope();
    }

    @Override
    public ScopeValue getScopeValue(String name) {
        return scope.resolve(name);
    }
    
    public void setConversationScope(String name, Object value) {
        scope.setConversationScope(name, value);
    }

    @Override
    public void setSessionScope(String name, Object value) {
        scope.setSessionScope(name, value);
    }

    public FlowConfig getFlowConfig() {
        return flowConfig;
    }

    public void setFlowConfig(FlowConfig flowConfig) {
        this.flowConfig = flowConfig;
    }    

    // TODO move to Injector class. 
    protected void performInjections(Object target, Map<String, ScopeValue> extraScope) {
        Field[] fields = target.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (autowired != null) {
                String name = field.getName();
                ScopeValue value = scope.resolve(name);
                if (value == null && extraScope != null) {
                    value = extraScope.get(name);
                } else if (name.equals("stateManager")) {
                    value = new ScopeValue(this);
                }
                if (value == null) {
                    System.out.println(Arrays.toString(applicationContext.getBeanNamesForType(field.getDeclaringClass())));
                    if (applicationContext.containsBean(name)) {
                        value = new ScopeValue(applicationContext.getBean(name));
                    } else {
                        try {                            
                            Object beanByClass = applicationContext.getBean(field.getType());
                            if (beanByClass != null) {
                                value = new ScopeValue(beanByClass);
                            }
                        } catch (NoSuchBeanDefinitionException ex) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("No bean found", ex);
                            }
                        }
                    }
                }
                
                if (value != null) {
                    try {
                        field.set(target, value.getValue());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (autowired.required()) {
                    throw new FlowException("Failed to resolve required injection: " + name + " for " + target);
                }
            }
        }
    }
    
    protected void performPostContruct(Object target) {
        Method[] methods = target.getClass().getDeclaredMethods();
        for (Method method : methods) {
            PostConstruct postConstructAnnotation = method.getAnnotation(PostConstruct.class);
            if (postConstructAnnotation != null) {
                method.setAccessible(true);
                try {
                    method.invoke(target);
                } catch (Exception ex) {
                    throw new FlowException("Failed to invoke @PostConstruct method " + method, ex);
                }
            }
        }
    }

    private boolean handleAction(Object state, Action action) {
        Method[] methods = state.getClass().getDeclaredMethods();
        
        String METHOD_ON_ANY = "onAnyAction";
        
        Method anyMethod = null;

        String actionName = action.getName();
        for (Method method : methods) {
            ActionHandler actionHandlerAnnotation = method.getAnnotation(ActionHandler.class);
            String matchingMethodName = "on" + actionName;
            if (actionHandlerAnnotation != null) {
                method.setAccessible(true);
                if (matchingMethodName.equals(method.getName())) {
                    try {
                        method.invoke(state, action); // TODO allow for some flexibility in args.
                        return true;
                    } catch (Exception ex) {
                        logger.error("", ex);
                    }                     
                } else if (METHOD_ON_ANY.equals(method.getName())) {
                    anyMethod = method;
                }
            }
        }
        
        if (anyMethod != null) {            
            try {
                anyMethod.invoke(state, action); // TODO allow for some flexibility in args.
                return true;
            } catch (Exception ex) {
                logger.error("", ex);
            }
        }

        return false;
    }

    public void registerUiModelListener(IUiModelListener uiModelListener) {
//        this.uiModelListener = uiModelListener;
    }

    @Override
    public void showScreen(IScreen screen) {
        screenService.showScreen(nodeId, screen);        
    }
    
    public String toJSONPretty(Object o) {
        try {
            return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException ex) {
            logger.warn("Failed to format object to json", ex);
            return String.valueOf(o);
        }
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public IScreenService getScreenService() {
        return screenService;
    }

}
