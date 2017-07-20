package org.jumpmind.jumppos.web;

import org.jumpmind.jumppos.flow.IStateManager;
import org.jumpmind.jumppos.flow.StateManagerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class SessionSubscribedListener implements ApplicationListener<SessionSubscribeEvent> {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	StateManagerRepository repository;
	
	@Override
	public void onApplicationEvent(SessionSubscribeEvent event) {
	    String clientId = (String)event.getMessage().getHeaders().get("simpDestination");
	    clientId = clientId.substring(clientId.indexOf("/store"));
	    logger.info("subscribed to {}", clientId);
	    IStateManager manager = repository.createOrLookup(clientId);
	    manager.refreshScreen();
	}
		
}