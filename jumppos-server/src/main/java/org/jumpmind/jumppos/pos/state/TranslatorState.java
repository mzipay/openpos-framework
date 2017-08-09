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

import javax.annotation.PostConstruct;

import org.jumpmind.jumppos.core.flow.Action;
import org.jumpmind.jumppos.core.flow.ActionHandler;
import org.jumpmind.jumppos.core.flow.IState;
import org.jumpmind.jumppos.core.flow.IStateManager;
import org.jumpmind.jumppos.core.screen.DefaultScreen;
import org.jumpmind.jumppos.pos.screen.translate.ITranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TranslatorState implements IState {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    IStateManager stateManager;

    @PostConstruct
    public void init() {
        if (stateManager.getTranslationManager() == null) {
            throw new IllegalStateException("When using a translation state, we expect an implementation of "
                    + ITranslationManager.class.getSimpleName() + " to be bound at the prototype scope");
        }
    }

    @Override
    public void arrive() {
        stateManager.getTranslationManager().showActiveScreen();
    }

    @ActionHandler
    public void onAnyAction(Action action, DefaultScreen screen) {
        ITranslationManager translationManager = stateManager.getTranslationManager();
        translationManager.doAction(action, screen);
    }

}
