package org.jumpmind.pos.translate.state;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.jumpmind.pos.core.device.DefaultDeviceResponse;
import org.jumpmind.pos.core.device.IDeviceRequest;
import org.jumpmind.pos.core.device.IDeviceResponse;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.ActionHandler;
import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.core.service.IDeviceService;
import org.jumpmind.pos.translate.ITranslationManager;
import org.jumpmind.pos.translate.ITranslationManagerSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TranslatorState implements IState {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected IStateManager stateManager;
    
    @Autowired 
    protected ITranslationManager translationManager;
    
    @Autowired(required=false)
    protected ITranslationManagerSubscriber subscriber;
    
    @Autowired
    protected IDeviceService deviceService;

    @PostConstruct
    public void init() {
        if (translationManager == null) {
            throw new IllegalStateException("When using a translation state, we expect an implementation of "
                    + ITranslationManager.class.getSimpleName() + " to be bound at the prototype scope");
        }
    }
    
    @Override
    public void arrive( Action action ) {
        subscribe();
        translationManager.showActiveScreen();              
    } 
    
    protected void subscribe() {
        if (subscriber == null) {
            ITranslationManagerSubscriber subscriber = new ITranslationManagerSubscriber() {
                
                @Override
                public void showScreen(DefaultScreen screen) {
                   stateManager.showScreen(screen);                    
                }
                
                @Override
                public boolean isInTranslateState() {                   
                    return stateManager.getCurrentState() instanceof TranslatorState;
                }
                
                @Override
                public String getNodeId() {
                    return stateManager.getNodeId();
                }
                
                @Override
                public String getAppId() {
                    return stateManager.getAppId();
                }
                
                @Override
                public void doAction(Action action) {
                    stateManager.doAction(action);                    
                }

                @Override
                public IDeviceResponse sendDeviceRequest(IDeviceRequest request) {
                    IDeviceResponse response = null;
                    CompletableFuture<IDeviceResponse> futureResponse =  deviceService.send(stateManager.getAppId(), stateManager.getNodeId(), request );
                    try {
                        response = futureResponse.get(request.getTimeout(), TimeUnit.MILLISECONDS);
                    } catch (TimeoutException ex) {
                        futureResponse.cancel(true);
                        response = new DefaultDeviceResponse(request.getRequestId(), request.getDeviceId(), IDeviceResponse.DEVICE_TIMEOUT_RESPONSE_TYPE, "Timeout reached. " + ex.getMessage());
                    } catch (Exception ex) {
                        futureResponse.cancel(true);
                        String msg = String.format("Failure waiting for a response. Error: {}", ex.getMessage());
                        response = new DefaultDeviceResponse(request.getRequestId(), request.getDeviceId(), IDeviceResponse.DEVICE_ERROR_RESPONSE_TYPE, msg);
                        logger.error(msg);
                    }
                    return response;
                }
            };
            translationManager.setTranslationManagerSubscriber(subscriber);
            stateManager.setNodeScope("translationManager", translationManager);
            stateManager.setNodeScope("subscriber", subscriber);
        }
    }

    @ActionHandler
    public void onAnyAction(Action action, DefaultScreen screen) {
        translationManager.doAction(stateManager.getAppId(), action, screen);
    }

}
