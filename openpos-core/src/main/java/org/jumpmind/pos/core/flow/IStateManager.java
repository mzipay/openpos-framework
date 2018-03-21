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

import java.util.Map;

import org.jumpmind.pos.core.screen.AbstractScreen;


public interface IStateManager {

    public void init(String appId, String nodeId);
    public String getNodeId();
    public String getAppId();
    public void doAction(String action);
    public void doAction(String action, Map<String, String> params);
    public void doAction(Action action);    
    public void endConversation();
    public void endSession();
    public <T> T getScopeValue(String name);
    public void setNodeScope(String name, Object value);
    public void setSessionScope(String name, Object value);
    public void setConversationScope(String name, Object value);
    public String toJSONPretty(Object o);
    public void showScreen(AbstractScreen screen);    
    public AbstractScreen getLastScreen();    
    public void refreshScreen();
    public IState getCurrentState();
    
}
