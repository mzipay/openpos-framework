package org.jumpmind.pos.core.config;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    final protected Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    MutableBoolean initialized;
    
    @Autowired
    Environment env;
    
    /*
     * TODO: This was added to increase the acceptable message size after adding the ItemSearchScreen.
     * This screen has multiple combo boxes with a lot of options. This is most likely what is causing the message size to blow up.
     * We should consider changing the way we populate large comboboxes.
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(80000); //75681
        registration.setSendBufferSizeLimit(80000);
    }
    
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic").setTaskScheduler(new DefaultManagedTaskScheduler())
				.setHeartbeatValue(new long[] { 0, 20000 });
		config.setApplicationDestinationPrefixes("/app");
		config.setPathMatcher(new AntPathMatcher("."));
	}
	
	 @Override
	    public void configureClientInboundChannel(ChannelRegistration registration) {
	        registration.interceptors(new ChannelInterceptorAdapter() {
	            @Override
	            public Message<?> preSend(Message<?> message, MessageChannel channel) {
	                StompHeaderAccessor accessor =
	                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
	                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
	                    String serverAuthToken = env.getProperty("openpos.auth.token");
	                    if (isNotBlank(serverAuthToken)) {
	                       @SuppressWarnings("unchecked")
	                       Map<String, List<String>> nativeHeaders = (Map<String, List<String>>)message.getHeaders().get("nativeHeaders");
	                       List<String> authTokens = nativeHeaders.get("authToken");
	                       String authToken = authTokens != null && authTokens.size() > 0 ? authTokens.get(0) : null;
	                       if (serverAuthToken.equals(authToken)) {
	                           accessor.setUser(new Principal() {                                
                                @Override
                                public String getName() {
                                    return "authenticatedClient";
                                }
                            });       
	                       } else {
	                           throw new MessagingException("Auth failed. Invalid auth token received");
	                       }
	                    }
	                    
	                    
	                }
	                return message;
	            }
	        });
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