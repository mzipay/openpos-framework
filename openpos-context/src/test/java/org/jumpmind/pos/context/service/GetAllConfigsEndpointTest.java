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
package org.jumpmind.pos.context.service;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jumpmind.pos.context.ContextException;
import org.jumpmind.pos.context.model.ConfigModel;
import org.jumpmind.pos.persist.cars.TestPersistCarsConfig;
import org.jumpmind.pos.service.ServiceResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestPersistCarsConfig.class})
public class GetAllConfigsEndpointTest {
    
    @Autowired
    private GetAllConfigsEndpoint endpoint;
    
    @Test
    public void testGetConfigsByNameSingleEntry() {
        List<ConfigModel> configs = new ArrayList<>();
        {
            ConfigModel config = new ConfigModel();
            config.setConfigName("singleValueName");
            config.setConfigValue("singleValue");
            configs.add(config);
        }
        
        Map<String, List<ConfigModel>> configsByName = endpoint.getConfigsByName(configs);
        
        assertEquals(1, configsByName.size());
        assertEquals(1, configsByName.get("singleValueName").size());
    }
    
    @Test
    public void testGetConfigsByName2EntriesSameKey() {
        List<ConfigModel> configs = new ArrayList<>();
        {
            ConfigModel config = new ConfigModel();
            config.setConfigName("testConfigKey");
            config.setConfigValue("testConfigValue");
            configs.add(config);
        }
        {
            ConfigModel config = new ConfigModel();
            config.setConfigName("testConfigKey");
            config.setConfigValue("testConfigValue2");
            configs.add(config);
        }
        
        Map<String, List<ConfigModel>> configsByName = endpoint.getConfigsByName(configs);
        
        assertEquals(1, configsByName.size());
        assertEquals(2, configsByName.get("testConfigKey").size());
        assertEquals("testConfigValue", configsByName.get("testConfigKey").get(0).getConfigValue());
        assertEquals("testConfigValue2", configsByName.get("testConfigKey").get(1).getConfigValue());
    }
    
    @Test
    public void testGetConfigsByName2EntriesDifferentKey() {
        List<ConfigModel> configs = new ArrayList<>();
        {
            ConfigModel config = new ConfigModel();
            config.setConfigName("testConfigKey");
            config.setConfigValue("testConfigValue");
            configs.add(config);
        }
        {
            ConfigModel config = new ConfigModel();
            config.setConfigName("testConfigKey2");
            config.setConfigValue("testConfigValue2");
            configs.add(config);
        }
        
        Map<String, List<ConfigModel>> configsByName = endpoint.getConfigsByName(configs);
        
        assertEquals(2, configsByName.size());
        assertEquals(1, configsByName.get("testConfigKey").size());
        assertEquals(1, configsByName.get("testConfigKey2").size());
        assertEquals("testConfigValue", configsByName.get("testConfigKey").get(0).getConfigValue());
        assertEquals("testConfigValue2", configsByName.get("testConfigKey2").get(0).getConfigValue());
    }    
    
    @Test
    public void testGetConfigsByNameSinglesOnEnds() {
        List<ConfigModel> configs = new ArrayList<>();
        {
            ConfigModel config = new ConfigModel();
            config.setConfigName("testConfigKey");
            config.setConfigValue("testConfigValue");
            configs.add(config);
        }
        {
            ConfigModel config = new ConfigModel();
            config.setConfigName("testConfigKey2");
            config.setConfigValue("testConfigValue2");
            configs.add(config);
        }
        {
            ConfigModel config = new ConfigModel();
            config.setConfigName("testConfigKey2");
            config.setConfigValue("testConfigValue2b");
            configs.add(config);
        }
        {
            ConfigModel config = new ConfigModel();
            config.setConfigName("testConfigKey3");
            config.setConfigValue("testConfigValue3");
            configs.add(config);
        }
        
        Map<String, List<ConfigModel>> configsByName = endpoint.getConfigsByName(configs);
        
        assertEquals(3, configsByName.size());
        assertEquals(1, configsByName.get("testConfigKey").size());
        assertEquals(2, configsByName.get("testConfigKey2").size());
        assertEquals(1, configsByName.get("testConfigKey3").size());
        assertEquals("testConfigValue", configsByName.get("testConfigKey").get(0).getConfigValue());
        assertEquals("testConfigValue2", configsByName.get("testConfigKey2").get(0).getConfigValue());
        assertEquals("testConfigValue2b", configsByName.get("testConfigKey2").get(1).getConfigValue());
        assertEquals("testConfigValue3", configsByName.get("testConfigKey3").get(0).getConfigValue());
    }        
    
    @Test
    public void testGetConfigsByNameMultiplesOnEnds() {
        List<ConfigModel> configs = new ArrayList<>();
        {
            ConfigModel config = new ConfigModel();
            config.setConfigName("testConfigKey1");
            config.setConfigValue("testConfigValue1");
            configs.add(config);
        }
        {
            ConfigModel config = new ConfigModel();
            config.setConfigName("testConfigKey1");
            config.setConfigValue("testConfigValue1b");
            configs.add(config);
        }
        {
            ConfigModel config = new ConfigModel();
            config.setConfigName("testConfigKey2");
            config.setConfigValue("testConfigValue2");
            configs.add(config);
        }
        {
            ConfigModel config = new ConfigModel();
            config.setConfigName("testConfigKey3");
            config.setConfigValue("testConfigValue3");
            configs.add(config);
        }
        {
            ConfigModel config = new ConfigModel();
            config.setConfigName("testConfigKey3");
            config.setConfigValue("testConfigValue3b");
            configs.add(config);
        }
        
        Map<String, List<ConfigModel>> configsByName = endpoint.getConfigsByName(configs);
        
        assertEquals(3, configsByName.size());
        assertEquals(2, configsByName.get("testConfigKey1").size());
        assertEquals(1, configsByName.get("testConfigKey2").size());
        assertEquals(2, configsByName.get("testConfigKey3").size());
        assertEquals("testConfigValue1", configsByName.get("testConfigKey1").get(0).getConfigValue());
        assertEquals("testConfigValue1b", configsByName.get("testConfigKey1").get(1).getConfigValue());
        assertEquals("testConfigValue2", configsByName.get("testConfigKey2").get(0).getConfigValue());
        assertEquals("testConfigValue3", configsByName.get("testConfigKey3").get(0).getConfigValue());
        assertEquals("testConfigValue3b", configsByName.get("testConfigKey3").get(1).getConfigValue());
    }
    
    @Test
    public void getAllConfigsTestStore100() {
        
        ConfigListResult configs = endpoint.getAllConfigs("100-1", getDate("2018-05-31 00:00:00"));
        
        {            
            ConfigModel config = configs.getConfig("pos.welcome.text");
            assertEquals("pos.welcome.text", config.getConfigName());
            assertEquals("Welcome store 100 in OH, USA!", config.getConfigValue());
        }
        {            
            ConfigModel config = configs.getConfig("pos.login.timeout");
            assertEquals("pos.login.timeout", config.getConfigName());
            assertEquals("global login timeout", config.getConfigValue());
        }
        {            
            ConfigModel config = configs.getConfig("pos.login.retry.attempts");
            assertEquals("pos.login.retry.attempts", config.getConfigName());
            assertEquals("10", config.getConfigValue());
        }
    }
    
    @Test
    public void getAllConfigsTestStore900() {
        
        ConfigListResult configs = endpoint.getAllConfigs("900-10", getDate("2018-05-31 00:00:00"));
        
        {            
            ConfigModel config = configs.getConfig("pos.welcome.text");
            assertEquals("pos.welcome.text", config.getConfigName());
            assertEquals("Welcome UG POS app!", config.getConfigValue());
        }
        {            
            ConfigModel config = configs.getConfig("pos.login.timeout");
            assertEquals("pos.login.timeout", config.getConfigName());
            assertEquals("global login timeout", config.getConfigValue());
        }
        {            
            ConfigModel config = configs.getConfig("pos.login.retry.attempts");
            assertEquals("pos.login.retry.attempts", config.getConfigName());
            assertEquals("15", config.getConfigValue());
        }
    }
    @Test
    public void getAllConfigsTestExpired() {
        ConfigListResult configs = endpoint.getAllConfigs("100-1", getDate("2525-05-31 00:00:00"));
        assertEquals(configs.getResultStatus(), ServiceResult.RESULT_NOT_FOUND);
    }
    
    @Test
    public void getAllConfigsNotActiveYet() {
        ConfigListResult configs = endpoint.getAllConfigs("100-1", getDate("1980-05-31 00:00:00"));
        assertEquals(configs.getResultStatus(), ServiceResult.RESULT_NOT_FOUND);
    }

    // TODO this would should be in a date or test util.
    public Date getDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            throw new ContextException(e);
        }
    }

}
