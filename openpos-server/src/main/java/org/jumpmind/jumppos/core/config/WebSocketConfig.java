package org.jumpmind.jumppos.core.config;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    final protected Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    MutableBoolean initialized;
    
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic").setTaskScheduler(new DefaultManagedTaskScheduler())
				.setHeartbeatValue(new long[] { 0, 20000 });
		config.setApplicationDestinationPrefixes("/app");
		config.setPathMatcher(new AntPathMatcher("."));
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/api").setAllowedOrigins("*").withSockJS().setInterceptors(new HandshakeInterceptor() {
		    @Override
		    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
		            Exception exception) {		      
		    }
		    
		    @Override
		    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
		            Map<String, Object> attributes) throws Exception {
		        if (!initialized.booleanValue()) {
		            logger.info("Rejected websocket communication attempt because the server is not initialized");
		            return false;
		        } else {
		            return true;
		        }
		        
		    }
		});
	}

}