package org.jumpmind.pos.devices.model;

import java.util.ArrayList;
import java.util.List;

import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name = "device")
public class DeviceModel extends AbstractModel {

    private static final long serialVersionUID = 1L;

    @Column(primaryKey = true)
    String profile;

    @Column(primaryKey = true)
    String deviceName;

    @Column
    String description;

    @Column
    String serviceClass;

    @Column
    String factoryClass;

    List<DevicePropsModel> properties = new ArrayList<>();

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String name) {
        this.deviceName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getFactoryClass() {
        return factoryClass;
    }

    public void setFactoryClass(String factoryClass) {
        this.factoryClass = factoryClass;
    }

    public void setProperties(List<DevicePropsModel> properties) {
        this.properties = properties;
    }

    public List<DevicePropsModel> getProperties() {
        return properties;
    }

    public void add(DevicePropsModel prop) {
        this.properties.add(prop);
    }

}
