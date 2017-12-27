package org.jumpmind.pos.core.device;

public class DevicePluginRequest extends DefaultDeviceRequest implements IDevicePlugin {

    private static final long serialVersionUID = 1L;
    private String pluginId;

    public DevicePluginRequest(String pluginId, String requestId, String deviceId, String payload) {
        super(requestId, deviceId, payload);
        this.pluginId = pluginId;
    }
    
    public DevicePluginRequest(String requestId, String deviceId, String payload) {
        super(requestId, deviceId, payload);
    }

    @Override
    public String getPluginId() {
        return this.pluginId;
    }

    @Override
    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

}
