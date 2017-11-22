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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Action implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String name;
    private String stateHash;
    private Map<String, String> parameters = new HashMap<>();
    private Object data;

    public Action() {
        this(null);
    }
    
    public Action(String actionName) {
        this(actionName, null, null);
    }
    
    public Action(String actionName, Object data) {
        this(actionName, null, null);
        this.data = data;
    }

    public Action(String actionName, String stateHash, Map<String, String> parameters) {
        this.name = actionName;
        this.stateHash = stateHash;
        this.parameters = parameters;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateHash() {
        return stateHash;
    }

    public void setStateHash(String stateHash) {
        this.stateHash = stateHash;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
    public String toDataString() {
        return data != null ? data.toString() : null;
    }

    @Override
    public String toString() {
        return "Action [name=" + name + ", stateHash=" + stateHash + ", parameters=" + parameters + "]";
    }
}
