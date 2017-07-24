package org.jumpmind.jumppos.flow;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StateManagerRepository {

    Map<String, IStateManager> repositoryById = new HashMap<>();

    @Autowired
    IStateManagerFactory factory;

    public synchronized IStateManager createOrLookup(String clientId) {
        IStateManager manager = repositoryById.get(clientId);
        if (manager == null) {
            manager = factory.create(clientId);
            repositoryById.put(clientId, manager);
        }
        return manager;
    }
}
