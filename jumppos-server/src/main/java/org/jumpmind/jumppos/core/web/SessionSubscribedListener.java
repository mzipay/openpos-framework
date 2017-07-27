package org.jumpmind.jumppos.core.web;


import org.jumpmind.jumppos.core.flow.IStateManager;
import org.jumpmind.jumppos.core.flow.IStateManagerFactory;
import org.jumpmind.jumppos.core.flow.StateManager;
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
	    String nodeId = (String)event.getMessage().getHeaders().get("simpDestination");
	    nodeId = nodeId.substring(nodeId.indexOf("/node/")+"/node/".length());
	    logger.info("subscribed to {}", nodeId);
	    IStateManager manager = stateManagerFactory.create(nodeId);
	    manager.init();
	}
		
}