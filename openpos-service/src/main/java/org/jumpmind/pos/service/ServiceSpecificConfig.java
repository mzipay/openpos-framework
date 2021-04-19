package org.jumpmind.pos.service;

import org.jumpmind.pos.service.strategy.InvocationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceSpecificConfig implements Cloneable {

    @Autowired(required = false)
    IConfigApplicator additionalConfigSource;

    protected List<String> profileIds = null;
    protected InvocationStrategy strategy = InvocationStrategy.LOCAL_ONLY;
    protected List<EndpointSpecificConfig> endpoints = new ArrayList<>();
    protected SamplingConfig samplingConfig = null;

    public SamplingConfig getSamplingConfig() { return samplingConfig; }

    public void setSamplingConfig(SamplingConfig samplingConfig) {this.samplingConfig = samplingConfig;}

    public List<String> getProfileIds() {
        return profileIds;
    }

    public void setProfileIds(List<String> profileIds) {
        this.profileIds = profileIds;
    }

    public InvocationStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(InvocationStrategy strategy) {
        this.strategy = strategy;
    }

    public List<EndpointSpecificConfig> getEndpoints() { return endpoints; }

    public void setEndpoints(List<EndpointSpecificConfig> endpoints) { this.endpoints = endpoints; }

    public ServiceSpecificConfig copy() {
        ServiceSpecificConfig copy;
        try {
            copy = (ServiceSpecificConfig)this.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void findAdditionalConfigs(String serviceId) {
        if (samplingConfig == null) {
            samplingConfig = new SamplingConfig();
        }
        if(additionalConfigSource != null) {
            String startsWith = String.format("openpos.services.specificConfig.%s.samplingConfig", serviceId);
            additionalConfigSource.applyAdditionalConfiguration(startsWith, samplingConfig);

            for (int index = 0; index < endpoints.size(); index++) {
                endpoints.get(index).findAdditionalConfigs(serviceId, index);
                startsWith = String.format("openpos.services.specificConfig.%s.endpoints[%d]", serviceId, index);
                additionalConfigSource.applyAdditionalConfiguration(startsWith, endpoints.get(index));
            }
        }
    }
}
