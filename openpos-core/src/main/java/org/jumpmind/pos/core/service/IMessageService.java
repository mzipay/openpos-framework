package org.jumpmind.pos.core.service;

import org.jumpmind.pos.core.model.Message;

public interface IMessageService {

    public void sendMessage(String appId, String nodeId, Message message);
        
}
