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
package org.jumpmind.pos.core.flow.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jumpmind.pos.core.flow.IState;


public class FlowConfig {
    
    private StateConfig initialState;
    private Map<Class<? extends IState>, StateConfig> stateConfigs = new HashMap<>();
    private Map<String, Object> configScope = new HashMap<>();
    private Map<String, Class<? extends IState>> actionToStateMapping = new HashMap<>();
    private Map<String, SubTransition> actionToSubStateMapping = new HashMap<>();
    
    public FlowConfig() {
    }
    
    public FlowConfig(Map<String, Object> configScope) {
        this.configScope = configScope;
    }
    
    public StateConfig getStateConfig(IState state) {
        return stateConfigs.get(state.getClass());
    }
    
    public StateConfig getStateConfig(Class<? extends IState> stateClass) {
        return stateConfigs.get(stateClass);
    }
    
    public void add(StateConfig config) {
        stateConfigs.put(config.getStateClass(), config);
        autoConfigureTargetStates(config);
    }
    
    protected void autoConfigureTargetStates(StateConfig config) {
        Collection<Class<? extends IState>> targetStateClasses = 
                config.getActionToStateMapping().values();
        
        for (Class<? extends IState> targetStateClass : targetStateClasses) {
            autoConfigureTargetState(targetStateClass);
        }
    }
    
    protected void autoConfigureTargetState(Class<? extends IState> targetStateClass) {
        if (!stateConfigs.containsKey(targetStateClass)) {
            StateConfig stateConfig = new StateConfig();
            stateConfig.setStateName(FlowUtil.getStateName(targetStateClass));
            stateConfig.setStateClass(targetStateClass);                
            stateConfigs.put(targetStateClass, stateConfig);
        }        
    }

    public StateConfig getInitialState() {
        return initialState;
    }

    public void setInitialState(StateConfig initialState) {
        add(initialState);
        this.initialState = initialState;
    }

    public Map<String, Object> getConfigScope() {
        return configScope;
    }

    public void setConfigScope(Map<String, Object> configScope) {
        this.configScope = configScope;
    }
    
    public void addGlobalTransition(String actionName, Class<? extends IState> destination) {
        actionToStateMapping.put(actionName, destination);
        autoConfigureTargetState(destination);
    }

    public void addGlobalSubTransition(String string, FlowConfig customerFlow) {
        SubTransition subTransition = new SubTransition(null, customerFlow);
        actionToSubStateMapping.put(string, subTransition);
    }

    public Map<Class<? extends IState>, StateConfig> getStateConfigs() {
        return stateConfigs;
    }

    public void setStateConfigs(Map<Class<? extends IState>, StateConfig> stateConfigs) {
        this.stateConfigs = stateConfigs;
    }

    public Map<String, Class<? extends IState>> getActionToStateMapping() {
        return actionToStateMapping;
    }

    public void setActionToStateMapping(Map<String, Class<? extends IState>> actionToStateMapping) {
        this.actionToStateMapping = actionToStateMapping;
    }

    public Map<String, SubTransition> getActionToSubStateMapping() {
        return actionToSubStateMapping;
    }

    public void setActionToSubStateMapping(Map<String, SubTransition> actionToSubStateMapping) {
        this.actionToSubStateMapping = actionToSubStateMapping;
    }
    
}
