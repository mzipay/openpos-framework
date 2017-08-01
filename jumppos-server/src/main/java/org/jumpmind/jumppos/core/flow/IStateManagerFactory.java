package org.jumpmind.jumppos.core.flow;

public interface IStateManagerFactory {

    IStateManager retreiveOrCreate(String clientId);
    
}
