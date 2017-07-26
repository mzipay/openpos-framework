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

import org.jumpmind.jumppos.core.flow.config.IFlowConfigProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class StateManagerFactory implements IStateManagerFactory {
    
    @Autowired
    IFlowConfigProvider flowConfigProvider;
    @Autowired
    IScreenService screenService;
    @Autowired
    ApplicationContext applicationContext;

    @Override
    public IStateManager create(String nodeId) {
        StateManager stateManager = new StateManager();
        stateManager.setNodeId(nodeId);
        stateManager.setFlowConfig(flowConfigProvider.getConfig(nodeId));
        stateManager.setScreenService(screenService);
        stateManager.setApplicationContext(applicationContext);
        return stateManager;
    }

}
