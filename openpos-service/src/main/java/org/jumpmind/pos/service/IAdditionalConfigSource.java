package org.jumpmind.pos.service;

public interface IAdditionalConfigSource {

    public void applyAdditionalConfiguration(String deviceId, String startsWith, Object applyTo);
    
}
