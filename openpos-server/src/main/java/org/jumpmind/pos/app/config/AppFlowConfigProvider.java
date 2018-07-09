

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
package org.jumpmind.pos.app.config;

import org.jumpmind.pos.app.state.CheckTransBalanceState;
import org.jumpmind.pos.app.state.CommitTransState;
import org.jumpmind.pos.app.state.HomeScreenState;
import org.jumpmind.pos.app.state.ManageMenuState;
import org.jumpmind.pos.app.state.SellState;
import org.jumpmind.pos.app.state.TenderCashState;
import org.jumpmind.pos.app.state.TenderCreditDebitState;
import org.jumpmind.pos.app.state.TenderGiftCardState;
import org.jumpmind.pos.app.state.TenderMenuState;
import org.jumpmind.pos.app.state.customer.CustomerDetailsState;
import org.jumpmind.pos.app.state.customer.CustomerSearchResultState;
import org.jumpmind.pos.app.state.customer.CustomerSearchState;
import org.jumpmind.pos.core.flow.CompleteState;
import org.jumpmind.pos.core.flow.config.AbstractFlowConfigProvider;
import org.jumpmind.pos.core.flow.config.FlowBuilder;
import org.jumpmind.pos.core.flow.config.FlowConfig;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class AppFlowConfigProvider extends AbstractFlowConfigProvider {
    
    private FlowConfig config;

    @Override
    public FlowConfig getConfig(String appId, String nodeId) {
        if (config != null) {
            return config;
        }
        config = new FlowConfig("Default");
        
        config.setInitialState(FlowBuilder.addState(HomeScreenState.class)
                .withTransition("Manage", ManageMenuState.class)
                .withTransition("Sell", SellState.class).build());
        config.add(FlowBuilder.addState(SellState.class)
                .withTransition("Back", HomeScreenState.class)
                .withTransition("Checkout", TenderMenuState.class)
                .withSubTransition("CustomerSearch", getCustomerSearchConfig(appId, nodeId), "CustomerSearchFinished")
                .build());
        config.add(FlowBuilder.addState(ManageMenuState.class)
                .withTransition("Back", HomeScreenState.class)
                .build());        
        config.add(FlowBuilder.addState(TenderMenuState.class)
                .withTransition("Back", SellState.class)
                .withTransition("TenderCash", TenderCashState.class)
                .withTransition("TenderCreditDebit", TenderCreditDebitState.class)
                .withTransition("TenderGiftCard", TenderGiftCardState.class)
                .build());        
        config.add(FlowBuilder.addState(TenderCashState.class)
                .withTransition("Back", TenderMenuState.class)
                .withTransition("CheckTransBalance", CheckTransBalanceState.class)
                .build());        
        config.add(FlowBuilder.addState(TenderCreditDebitState.class)
                .withTransition("Back", TenderMenuState.class)
                .withTransition("CheckTransBalance", CheckTransBalanceState.class)
                .build());        
        config.add(FlowBuilder.addState(TenderGiftCardState.class)
                .withTransition("Back", TenderMenuState.class)
                .withTransition("CheckTransBalance", CheckTransBalanceState.class)
                .build());        
        config.add(FlowBuilder.addState(CheckTransBalanceState.class)
                .withTransition("Back", TenderMenuState.class)
                .withTransition("CommitTransaction", CommitTransState.class)
                .withTransition("ReturnToTenderMenu", TenderMenuState.class)
                .build());
        
        config.addGlobalSubTransition("AdvanceSearch", getCustomerSearchConfig(appId, nodeId));
        config.addGlobalTransition("BackToMain", HomeScreenState.class);
        
        return config;
    }
    
    protected FlowConfig getCustomerSearchConfig(String appId, String nodeId) {
        FlowConfig config = new FlowConfig("CustomerSearch");
        config.setInitialState(FlowBuilder.addState(CustomerSearchState.class)
                .withTransition("Back", CompleteState.class)
                .withTransition("SearchCustomer", CustomerSearchResultState.class)
                .build());
        
        config.add(FlowBuilder.addState(CustomerSearchResultState.class)
                .withTransition("Back", CustomerSearchState.class)
                .withTransition("ViewDetails", CustomerDetailsState.class)
                .withTransition("SelectCustomer", CompleteState.class)
                .build());   
        
        config.add(FlowBuilder.addState(CustomerDetailsState.class)
                .withTransition("Back", CustomerSearchState.class)
                .withTransition("ViewDetails", CustomerDetailsState.class)
                .withTransition("SelectCustomer", CompleteState.class)
                .build());   
        
        return config;
    }
}
