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
package org.jumpmind.jumppos.pos.state;

import org.apache.commons.lang3.StringUtils;
import org.jumpmind.jumppos.core.flow.Action;
import org.jumpmind.jumppos.core.flow.ActionHandler;
import org.jumpmind.jumppos.core.flow.IState;
import org.jumpmind.jumppos.core.flow.IStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class NodePersonalizationState implements IState {

    Logger logger = LoggerFactory.getLogger(getClass());

    static final String TEMPORARY_NODE_ID = "TEMPNODEID-";

    @Autowired
    IStateManager stateManager;

    @Override
    public void arrive() {
        if (stateManager.getNodeId().startsWith(TEMPORARY_NODE_ID)) {
            stateManager.showScreen(new NodePersonalizationForm());
        } else {
            stateManager.doAction(new Action("Complete"));
        }
    }
    
    @ActionHandler
    public void onSavePersonalization(Action action, NodePersonalizationForm screen) {
        String nodeId = screen.getNodeId();
        if (!StringUtils.isEmpty(nodeId)) {            
            stateManager.doAction(new Action("Complete"));
        } else {
            stateManager.showScreen(new NodePersonalizationForm());
        }
    }    
}
