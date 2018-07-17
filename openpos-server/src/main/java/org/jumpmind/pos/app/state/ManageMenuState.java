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
package org.jumpmind.pos.app.state;

import org.apache.log4j.Logger;
import org.jumpmind.pos.app.state.user.StatePermission;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.ActionHandler;
import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.InOut;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.core.screen.HomeScreen;
import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.screen.Screen;
import org.jumpmind.pos.ops.service.OpsServiceClient;
import org.jumpmind.pos.trans.model.BusinessDate;
import org.jumpmind.pos.user.model.UserModel;

@StatePermission(permissionId = "manage.menu")
public class ManageMenuState extends AbstractState implements IState {

    private Logger logger = Logger.getLogger(ManageMenuState.class);

    @In(scope = ScopeType.Node)
    OpsServiceClient opsServiceClient;
    
    @In(scope = ScopeType.Session)
    UserModel currentUser;
    
    @InOut(scope = ScopeType.Session)
    BusinessDate businessDate;

    @Override
    public void arrive(Action action) {
        stateManager.showScreen(buildScreen());
    }

    @Override
    protected String getDefaultBundleName() {
        return "manage";
    }

    @ActionHandler
    public void onSell(Action action) {
        logger.info("Sell action intercepted for testing purposes.");
        stateManager.doAction(action);
    }

    protected Screen buildScreen() {
        HomeScreen screen = new HomeScreen();
        MenuItem openDevice = new MenuItem("OpenDevice", "Open Device", "tablet");
        MenuItem closeStore = new MenuItem("CloseStore", "Close Store", "store");
        MenuItem openStore = new MenuItem("OpenStore", "Open Store", "store");
        if (opsServiceClient.isStoreOpen()) {
            closeStore.setEnabled(!opsServiceClient.areDevicesOpen());
            screen.addMenuItem(closeStore);
            if (opsServiceClient.isDeviceOpen()) {
                screen.addMenuItem(new MenuItem("CloseDevice", "Close Device", "tablet"));
            } else {
                screen.addMenuItem(openDevice);
            }
        } else {
            screen.addMenuItem(openStore);
            openDevice.setEnabled(false);
            screen.addMenuItem(openDevice);
        }
        screen.setName("Manage");
        screen.setIcon("store");
        screen.setType("Home");
        screen.setBackButton(new MenuItem("Back"));
        return screen;
    }

    @ActionHandler
    public void onOpenStore(Action action) {
        // TODO - prompt for business date in the future
        BusinessDate date = new BusinessDate();
        this.opsServiceClient.openStore(currentUser, date);
        this.businessDate = date;
        stateManager.showScreen(buildScreen());
    }

    @ActionHandler
    public void onCloseStore(Action action) {
        this.opsServiceClient.closeStore(currentUser, businessDate);
        stateManager.showScreen(buildScreen());
    }
    
    @ActionHandler
    public void onOpenDevice(Action action) {
        this.opsServiceClient.openDevice(currentUser, businessDate);
        stateManager.showScreen(buildScreen());
    }

    @ActionHandler
    public void onCloseDevice(Action action) {
        this.opsServiceClient.closeDevice(currentUser, businessDate);
        stateManager.showScreen(buildScreen());
    }
    
    /*
+----------------------------+
| +------------------------+ |
| |    Enter Busn Date     | |
| |        [Prompt]        | |
| +------------------------+ |
+----------------------------+
{
  "name" : "Enter Busn Date",
  "type" : "Prompt",
  "icon" : null,
  "template" : {
    "type" : "Sell",
    "dialog" : false,
    "statusBar" : { },
    "localMenuItems" : [ ],
    "transactionMenuItems" : [ ],
    "scan" : null,
    "workstation" : {
      "storeId" : "00039",
      "workstationId" : "011"
    },
    "operatorText" : null
  },
  "locale" : "en-US",
  "sessionTimeoutMillis" : 60000,
  "sessionTimeoutAction" : null,
  "promptIcon" : "question_answer",
  "placeholderText" : "Business Date ",
  "text" : "Press Enter to accept the business date",
  "responseText" : "07/10/2018",
  "editable" : true,
  "responseType" : "DATE",
  "minLength" : null,
  "maxLength" : null,
  "action" : "Next",
  "actionButton" : {
    "action" : "Next",
    "title" : "Next",
    "icon" : "keyboard_arrow_right",
    "enabled" : true,
    "confirmationMessage" : null,
    "children" : null,
    "sensitive" : false
  },
  "comments" : "",
  "showComments" : false,
  "otherActions" : null,
  "sequenceNumber" : 38,
  "backButton" : {
    "action" : "Undo",
    "title" : "Back",
    "icon" : null,
    "enabled" : true,
    "confirmationMessage" : null,
    "children" : null,
    "sensitive" : false
  },
  "theme" : "ascena_ann-theme",
  "refreshAlways" : true
}
     */
}
