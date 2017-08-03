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

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.jumpmind.jumppos.core.flow.config.FlowConfig;


public class FlowServer implements IUiModelListener {
    
    private String nodeId;
    
    private BlockingQueue<Action> actionQueue = new LinkedBlockingQueue<Action>();
    
    // TODO switch to Jackson?
//    private Gson gsonPrettyPrint = new GsonBuilder().setPrettyPrinting().create();
//    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    private StateManager flowManager = new StateManager();
    
    public FlowServer(FlowConfig flowConfig) {
        flowManager.setFlowConfig(flowConfig);
        flowManager.registerUiModelListener(this);
    }
    
    public void start() {
        new Thread(() -> flowManager.init(), "FlowServerThread-" + getNodeId()).start();
        new Thread(() -> mainActionLoop(), "FlowServerActionLoop-" + getNodeId()).start();        
    }    
    
    public void postAction(String action) {
        postAction(action, null, null);
    }
    
    public void flushActions() {
        actionQueue.clear();
    }
    
    public void postAction(String actionName, String stateHash, Map<String, String> parameters) {
        Action action = new Action(actionName, stateHash, parameters);
        try {
            actionQueue.put(action);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public void mainActionLoop() {
        while (true) {
            Action action = readActionWithTimeout(60000*10); // TODO this should match auto logout?
            if (action != null) {
                flowManager.doAction(action);
            } else {
                System.out.println("Session timed out..");
                flowManager.endSession();
            }
        }
    }
    
    public Action readActionWithTimeout(long timeoutMills) {
        try {
            return actionQueue.poll(timeoutMills, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            return null;
        }
    }
    
    @Override
    public void notifyUiUpdate(UiModel model) {
//        System.out.println("UI Model Change:" + getGsonPrettyPrint().toJson(model));
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

//    public Gson getGsonPrettyPrint() {
//        return gsonPrettyPrint;
//    }
//
//    public void setGsonPrettyPrint(Gson gsonPrettyPrint) {
//        this.gsonPrettyPrint = gsonPrettyPrint;
//    }
//
//    public Gson getGson() {
//        return gson;
//    }
//
//    public void setGson(Gson gson) {
//        this.gson = gson;
//    }

    
    
    
}
