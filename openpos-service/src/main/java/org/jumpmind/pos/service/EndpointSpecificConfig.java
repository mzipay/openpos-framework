package org.jumpmind.pos.service;

import org.jumpmind.pos.service.strategy.InvocationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EndpointSpecificConfig implements Cloneable {

    @Autowired(required = false)
    IConfigApplicator additionalConfigSource;

    protected String profile;
    protected InvocationStrategy strategy;
    protected String path;
    protected SamplingConfig samplingConfig;

    public SamplingConfig getSamplingConfig() {
        return samplingConfig;
    }

    public void setSamplingConfig(SamplingConfig samplingConfig) {
        this.samplingConfig = samplingConfig;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public InvocationStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(InvocationStrategy strategy) {
        this.strategy = strategy;
    }

    public EndpointSpecificConfig copy() {
        EndpointSpecificConfig copy;
        try {
            copy = (EndpointSpecificConfig)this.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void findAdditionalConfigs(String serviceId, int index) {
        if(samplingConfig == null){
            samplingConfig = new SamplingConfig();
        }

        if(additionalConfigSource != null){
            String startsWith = String.format("openpos.services.specificConfig.%s.endpoints[%d].samplingConfig", serviceId, index);
            additionalConfigSource.applyAdditionalConfiguration(startsWith, samplingConfig);
        }
    }
}
