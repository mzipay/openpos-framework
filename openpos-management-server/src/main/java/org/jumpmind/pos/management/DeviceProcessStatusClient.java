package org.jumpmind.pos.management;

import org.jumpmind.pos.util.model.ProcessInfo;

public interface DeviceProcessStatusClient {

    public ProcessInfo getRemoteProcessStatus(String deviceId, int port);
}
