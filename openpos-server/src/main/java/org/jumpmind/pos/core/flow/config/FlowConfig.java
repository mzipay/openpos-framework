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

import java.util.HashMap;

import java.util.Map;

import org.jumpmind.pos.core.flow.IState;


public class FlowConfig {
    private StateConfig initialState;
    private Map<String, StateConfig> stateConfigs = new HashMap<>();
    
    public StateConfig getStateConfig(IState state) {
        String stateName = FlowUtil.getStateName(state.getClass());
        return stateConfigs.get(stateName);
    }
    
    public StateConfig getStateConfig(String stateName) {
        return stateConfigs.get(stateName);
    }
    
    public void add(StateConfig config) {
        stateConfigs.put(config.getStateName(), config);
    }
    
    public StateConfig getInitialState() {
        return initialState;
    }

    public void setInitialState(StateConfig initialState) {
        add(initialState);
        this.initialState = initialState;
    }
}
