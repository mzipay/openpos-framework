package org.jumpmind.pos.service;

public class ServiceSpecificConfig extends ServiceCommonConfig implements Cloneable {

    protected String profile = ServiceConfig.LOCAL_PROFILE;
    protected InvocationiStrategy strategy;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public InvocationiStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(InvocationiStrategy strategy) {
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
