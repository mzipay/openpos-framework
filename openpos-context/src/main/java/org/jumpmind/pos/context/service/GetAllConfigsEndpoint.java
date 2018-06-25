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

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jumpmind.pos.context.model.ConfigModel;
import org.jumpmind.pos.context.model.ContextRepository;
import org.jumpmind.pos.context.model.DeviceModel;
import org.jumpmind.pos.context.model.TagCalculator;
import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.service.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class GetAllConfigsEndpoint {

    @Autowired
    private ContextRepository contextRepository;
    @Autowired
    private TagCalculator tagCalculator;
    @Autowired
    ContextService contextService;

    @Endpoint("/getAllConfigs")
    public ConfigListResult getAllConfigs(
            @RequestParam(value="deviceId", defaultValue="*") String deviceId,
            @RequestParam(value="currentTime") Date currentTime) {

        ConfigListResult result = new ConfigListResult();

        DeviceResult deviceResult = contextService.getDevice(deviceId);
        if (!deviceResult.isSuccess()) {
            result.setResultStatus(deviceResult.getResultStatus());
            result.setResultMessage(deviceResult.getResultMessage());
            return result;
        }        

        DeviceModel device = deviceResult.getDevice();
        List<ConfigModel> dbConfigs = contextRepository.findConfigsByTag(currentTime, device.getTags());
        if (CollectionUtils.isEmpty(dbConfigs)) {
            result.setResultStatus(ServiceResult.RESULT_NOT_FOUND);
            return result;
        }

        List<ConfigModel> configs = new ArrayList<>(); 

        Map<String, List<ConfigModel>> configsByName = getConfigsByName(dbConfigs);

        for (String configName : configsByName.keySet()) {
            List<ConfigModel> configsForName = configsByName.get(configName);
            ConfigModel config = (ConfigModel) tagCalculator.getMostSpecific(configsForName, device.getTags(), ContextRepository.getTagConfig());
            configs.add(config);
        }

        result.setResultStatus(ServiceResult.RESULT_SUCCESS);
        result.setConfigs(configs);

        return result;
    }

    protected Map<String, List<ConfigModel>> getConfigsByName(List<ConfigModel> configs) {
        // This assumes the configs are sorted by name.

        String currentName = null;
        List<ConfigModel> currentList = null;
        Map<String, List<ConfigModel>> configsByName = new LinkedHashMap<>();

        for (ConfigModel config : configs) {
            if (!config.getConfigName().equals(currentName)) {
                if (currentList != null) {
                    configsByName.put(currentName, currentList);
                }
                currentName = config.getConfigName();
                currentList = new ArrayList<>();
            }
            if (currentList != null) {                
                currentList.add(config);
            }
        }

        if (!configsByName.containsKey(currentName)) {
            configsByName.put(currentName, currentList);
        }

        return configsByName;
    }    

}
