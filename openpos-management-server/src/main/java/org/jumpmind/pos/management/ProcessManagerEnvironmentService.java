package org.jumpmind.pos.management;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProcessManagerEnvironmentService {
    
    @Autowired
    OpenposManagementServerConfig config;

    // TODO: Add persisting of process info to file

    public void ensureMainWorkDirExists() {
        File work = new File(config.getMainWorkDirPath());
        if (! work.exists()) {
            if (work.mkdirs()) {
                log.info("Openpos Management Service created working dir at '{}'", work.getPath());
            } else {
                throw new OpenposManagementException(String.format("Failed to main working directory at '%s'", work.getPath())) ;
            }
        } else {
            log.debug("Openpos Management Service working dir, '{}', already exists, no need to create", work.getPath());
        }
    }

    public Map<String,DeviceProcessInfo> getDeviceProcessTrackingInfo() {
        // TODO: read/write process tracking info
        return null;
    }
    
    
}
