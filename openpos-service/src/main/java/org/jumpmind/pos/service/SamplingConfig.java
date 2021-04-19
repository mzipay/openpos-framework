package org.jumpmind.pos.service;

public class SamplingConfig implements Cloneable{
    private boolean enabled = false;
    private int retentionDays;

    public int getRetentionDays() {
        return retentionDays;
    }

    public void setRetentionDays(int retentionDays) {
        this.retentionDays = retentionDays;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public SamplingConfig copy(){
        SamplingConfig copy;
        try {
            copy = (SamplingConfig)this.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
