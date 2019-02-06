package org.jumpmind.pos.service;

import org.jumpmind.pos.service.strategy.InvocationStrategy;

public class ServiceSpecificConfig extends ServiceCommonConfig implements Cloneable {

    protected String profile = ServiceConfig.LOCAL_PROFILE;
    protected InvocationStrategy strategy = InvocationStrategy.LOCAL_ONLY;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
    
    public boolean isLocal() {
        return ServiceConfig.LOCAL_PROFILE.equals(profile);
    }

    public InvocationStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(InvocationStrategy strategy) {
        this.strategy = strategy;
    }
    
    public ServiceSpecificConfig copy() {
        ServiceSpecificConfig copy;
        try {
            copy = (ServiceSpecificConfig)this.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
