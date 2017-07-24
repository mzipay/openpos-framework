package org.jumpmind.jumppos.flow;

public interface IStateManagerFactory {

    IStateManager create(String clientId);
    
}
