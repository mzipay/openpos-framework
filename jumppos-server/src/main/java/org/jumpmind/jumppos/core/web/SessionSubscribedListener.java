package org.jumpmind.jumppos.core.web;


import org.jumpmind.jumppos.core.flow.IStateManager;
import org.jumpmind.jumppos.core.flow.IStateManagerFactory;
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
	IStateManagerFactory stateManagerFactory;
		
	@Override
	public void onApplicationEvent(SessionSubscribeEvent event) {
	    String topicName = (String)event.getMessage().getHeaders().get("simpDestination");
	    String nodeId = topicName.substring(topicName.indexOf("/node/")+"/node/".length());
	    logger.info("subscribed to {}", topicName);
	    IStateManager stateManager = stateManagerFactory.retreiveOrCreate(nodeId);
	    stateManager.refreshScreen();

	}
		
}