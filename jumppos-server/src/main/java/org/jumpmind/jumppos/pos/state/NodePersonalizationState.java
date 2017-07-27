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

import org.jumpmind.jumppos.core.flow.Action;
import org.jumpmind.jumppos.core.flow.IScreenManager;
import org.jumpmind.jumppos.core.flow.IState;
import org.jumpmind.jumppos.core.flow.IStateManager;
import org.springframework.beans.factory.annotation.Autowired;

public class NodePersonalizationState implements IState {
    
    static final String UNDEFINED_NODE = "UNDEFINED-";
    
    @Autowired IStateManager stateManager;
    @Autowired IScreenManager screenManager;

    @Override
    public void arrive() {
        stateManager.doAction(new Action("Complete"));
//        if (stateManager.getNodeId().startsWith(UNDEFINED_NODE)) {
//            screenManager.showScreen("NodePersonalization");
//        } else {
//            stateManager.doAction(new Action("Complete"));
//        }
    }

}
