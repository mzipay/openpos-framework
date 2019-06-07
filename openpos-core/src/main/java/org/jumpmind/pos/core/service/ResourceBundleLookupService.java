package org.jumpmind.pos.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ResourceBundle;

public class ResourceBundleLookupService implements IResourceLookupService{

    @Autowired
    protected ResourceBundle screenProperties;
	
	@Override
	public String getString(String appId, String deviceId, String group, String key) {
		return screenProperties.getString(key.toLowerCase());
	}

}
