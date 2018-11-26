package org.jumpmind.pos.server.service;

import org.jumpmind.pos.server.model.Message;

public interface IMessageService {

    public void sendMessage(String appId, String nodeId, Message message);
        
}
