package org.jumpmind.pos.service;

public interface IAdditionalConfigSource {

    public void applyAdditionalConfiguration(String prefix, Object applyTo);
    
}
