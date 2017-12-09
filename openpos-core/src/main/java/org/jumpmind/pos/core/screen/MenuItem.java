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
package org.jumpmind.pos.core.screen;

import java.io.Serializable;

public class MenuItem implements IUIAction, Serializable {

    private static final long serialVersionUID = 1L;
    
    private String action;
    private String title;
    private String icon;
    private boolean enabled = true;
    
    public MenuItem() {
    }
    
    public MenuItem(String action, String title, String icon) {
        super();
        this.action = action;
        this.title = title;
        this.icon = icon;
    }

    public MenuItem(String action, String title, IIcon icon) {
        this(action, title, icon.getName());
    }
    
    public MenuItem(String title, String action, boolean enabled) {
        super();
        this.action = action;
        this.title = title;
        this.enabled = enabled;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
