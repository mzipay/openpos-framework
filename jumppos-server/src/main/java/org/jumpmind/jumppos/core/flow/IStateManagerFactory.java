package org.jumpmind.jumppos.core.flow;

public interface IStateManagerFactory {

    IStateManager create(String clientId);
    
}
