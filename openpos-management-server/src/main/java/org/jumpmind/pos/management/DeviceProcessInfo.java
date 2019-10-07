package org.jumpmind.pos.management;

import java.time.Instant;

import org.jumpmind.pos.util.StreamCopier;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class DeviceProcessInfo {
    @Getter
    private String deviceId;
    @Getter
    private Integer port;
    @Getter
    private Integer pid;
    @Getter @Setter(AccessLevel.PACKAGE)
    DeviceProcessStatus status;
    @Getter
    private Process process;
    @Getter
    private Instant lastUpdated;
    @Getter @Setter(AccessLevel.PACKAGE)
    private StreamCopier streamCopier;

    public DeviceProcessInfo(String deviceId, DeviceProcessStatus status) {
        this.deviceId = deviceId;
        this.status = status;
        touch();
    }
    
    public boolean isPreviouslyStarted() {
        return this.getProcess() != null;
    }

    public boolean isNotPreviouslyStarted() {
        return ! isPreviouslyStarted();
    }
    
    public boolean isProcessAlive() {
        return this.getProcess() != null &&
               this.getProcess().isAlive();
    }
    
    void setProcess(Process process) {
        this.process = process;
        touch();
    }

    void setPid(Integer pid) {
        this.pid = pid;
        touch();
    }

    void setPort(Integer port) {
        this.port = port;
        touch();
    }
    
    private void touch() {
        this.lastUpdated = Instant.now();
    }
}
