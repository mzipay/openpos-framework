package org.jumpmind.pos.server.service;

import org.jumpmind.pos.util.model.Message;

public interface IMessageService {

    public void sendMessage(String appId, String deviceId, Message message);
        
}
