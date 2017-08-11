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

import java.util.HashMap;
import java.util.Map;

import org.jumpmind.jumppos.core.flow.config.IFlowConfigProvider;
import org.jumpmind.jumppos.core.service.IScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class StateManagerFactory implements IStateManagerFactory {

    @Autowired
    IFlowConfigProvider flowConfigProvider;

    @Autowired
    IScreenService screenService;

    @Autowired
    ApplicationContext applicationContext;

    private Map<String, StateManager> stateManagersByNodeId = new HashMap<>();

    @Override
    public IStateManager retreive(String nodeId) {
        return  stateManagersByNodeId.get(nodeId);
    }
    
    @Override
    public IStateManager create(String nodeId) {
        StateManager stateManager = stateManagersByNodeId.get(nodeId);
        if (stateManager == null) {
            stateManager = applicationContext.getBean(StateManager.class);
            stateManager.setFlowConfig(flowConfigProvider.getConfig(nodeId));
            stateManager.init(nodeId);
            stateManagersByNodeId.put(nodeId, stateManager);
        }
        return stateManager;
    }

}
