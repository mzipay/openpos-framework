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
package org.jumpmind.pos.core.flow;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.jumpmind.pos.core.flow.config.FlowConfig;
import org.jumpmind.pos.core.flow.config.StateConfig;
import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.core.screen.translate.ITranslationManager;
import org.jumpmind.pos.core.screen.translate.ITranslationManagerSubscriber;
import org.jumpmind.pos.core.service.IScreenService;
import org.jumpmind.pos.core.state.TranslatorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component()
@org.springframework.context.annotation.Scope("prototype")
public class StateManager implements IStateManager, ITranslationManagerSubscriber {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IScreenService screenService;

    @Autowired
    private ActionHandlerImpl actionHandler;

    @Autowired
    private Injector injector;

    @Autowired(required = false)
    private ITranslationManager translationManager;

    private String appId;
    private String nodeId;
    private Scope scope = new Scope();
    private FlowConfig flowConfig;
    private IState currentState;

    private ObjectMapper jsonMapper = new ObjectMapper();

    @PostConstruct
    public void postConstruct() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AnnotationTypeFilter(org.jumpmind.pos.core.model.annotations.Screen.class));

        for (BeanDefinition bd : scanner.findCandidateComponents("org.jumpmind.pos")) {
            logger.info("" + bd);
        }
    }

    public void init(String appId, String nodeId) {
        this.appId = appId;
        this.nodeId = nodeId;
        if (translationManager != null) {
            translationManager.setTranslationManagerSubscriber(this);
        }
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
        Map<String, ScopeValue> extraScope = new HashMap<>();
        extraScope.put("stateManager", new ScopeValue(this));
        injector.performInjections(newState, scope, extraScope);
        currentState = newState;
        currentState.arrive();
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
    public boolean isInTranslateState() {
        return currentState != null && currentState instanceof TranslatorState;
    }

    @Override
    public IState getCurrentState() {
        return currentState;
    }

    @Override
    public DefaultScreen getLastScreen() {
        return screenService.getLastScreen(appId, nodeId);
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
            DefaultScreen deserializedScreen = screenService.deserializeScreenPayload(appId, nodeId, action);

            boolean handled = actionHandler.handleAction(currentState, action, deserializedScreen);
            if (handled) {
                if (savedCurrentState == currentState) {
                    // state did not change, reassert the current state.
                    // transitionTo(currentState);
                }
            } else {
                logger.warn("Unexpected action {}", action);
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

    @Override
    public void setNodeScope(String name, Object value) {
        scope.setNodeScope(name, value);
    }

    @Override
    public void setSessionScope(String name, Object value) {
        scope.setSessionScope(name, value);
    }

    public void setConversationScope(String name, Object value) {
        scope.setConversationScope(name, value);
    }

    public FlowConfig getFlowConfig() {
        return flowConfig;
    }

    public void setFlowConfig(FlowConfig flowConfig) {
        this.flowConfig = flowConfig;
    }

    @Override
    public void showScreen(DefaultScreen screen) {
        if (this.currentState != null && this.currentState instanceof IScreenInterceptor) {
            screen = ((IScreenInterceptor)this.currentState).intercept(screen);            
        }
        screenService.showScreen(appId, nodeId, screen);
    }

    public String toJSONPretty(Object o) {
        try {
            return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException ex) {
            logger.warn("Failed to format object to json", ex);
            return String.valueOf(o);
        }
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
    public ITranslationManager getTranslationManager() {
        return translationManager;
    }

}
