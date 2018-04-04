package org.jumpmind.pos.persist.cars;

import java.util.Map;

public interface EntityId {

    default Map<String, Object> getIdFields() {
        return null;
    }
    
}
