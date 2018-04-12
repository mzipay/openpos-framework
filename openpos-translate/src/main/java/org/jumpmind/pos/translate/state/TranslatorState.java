package org.jumpmind.pos.translate.state;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.jumpmind.pos.core.device.DefaultDeviceResponse;
import org.jumpmind.pos.core.device.IDeviceRequest;
import org.jumpmind.pos.core.device.IDeviceResponse;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.ActionHandler;
import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.model.Form;
import org.jumpmind.pos.core.screen.AbstractScreen;
import org.jumpmind.pos.core.service.IDeviceService;
import org.jumpmind.pos.translate.ITranslationManager;
import org.jumpmind.pos.translate.ITranslationManagerSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

public class TranslatorState implements IState {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected IStateManager stateManager;

    @Autowired
    protected ITranslationManager translationManager;

    @Autowired(required = false)
    protected ITranslationManagerSubscriber subscriber;

    @Autowired
    protected IDeviceService deviceService;

    @Autowired
    protected Environment env;

    @PostConstruct
    public void init() {
        if (translationManager == null) {
            throw new IllegalStateException("When using a translation state, we expect an implementation of "
                    + ITranslationManager.class.getSimpleName() + " to be bound at the prototype scope");
        }
    }

    @Override
    public void arrive(Action action) {
        if (subscribe()) {
            translationManager.showActiveScreen();
        } else {
            translationManager.doAction(subscriber.getAppId(), action, new Form());
        }
    }

    protected boolean subscribe() {
        if (subscriber == null) {
            ITranslationManagerSubscriber subscriber = new ITranslationManagerSubscriber() {

                Properties properties;

                @Override
                public void showScreen(AbstractScreen screen) {
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
                public Properties getProperties() {
                    if (properties == null) {
                        properties = new Properties();
                        MutablePropertySources propSrcs = ((AbstractEnvironment) env).getPropertySources();
                        StreamSupport.stream(propSrcs.spliterator(), false).filter(ps -> ps instanceof EnumerablePropertySource)
                                .map(ps -> ((EnumerablePropertySource<?>) ps).getPropertyNames()).flatMap(Arrays::<String> stream)
                                .forEach(propName -> properties.setProperty(propName, env.getProperty(propName)));
                    }
                    return properties;
                }

                @Override
                public void doAction(Action action) {
                    stateManager.doAction(action);
                }

                @Override
                public IDeviceResponse sendDeviceRequest(IDeviceRequest request) {
                    IDeviceResponse response = null;
                    CompletableFuture<IDeviceResponse> futureResponse = deviceService.send(stateManager.getAppId(), stateManager.getNodeId(),
                            request);
                    try {
                        response = futureResponse.get(request.getTimeout(), TimeUnit.MILLISECONDS);
                    } catch (TimeoutException ex) {
                        futureResponse.cancel(true);
                        response = new DefaultDeviceResponse(request.getRequestId(), request.getDeviceId(),
                                IDeviceResponse.DEVICE_TIMEOUT_RESPONSE_TYPE, "Timeout reached. " + ex.getMessage());
                    } catch (Exception ex) {
                        futureResponse.cancel(true);
                        String msg = String.format("Failure waiting for a response. Error: {}", ex.getMessage());
                        response = new DefaultDeviceResponse(request.getRequestId(), request.getDeviceId(),
                                IDeviceResponse.DEVICE_ERROR_RESPONSE_TYPE, msg);
                        logger.error(msg);
                    }
                    return response;
                }
            };
            translationManager.setTranslationManagerSubscriber(subscriber);
            stateManager.setNodeScope("translationManager", translationManager);
            stateManager.setNodeScope("subscriber", subscriber);
            return true;
        } else {
            return false;
        }
    }

    @ActionHandler
    public void onAnyAction(Action action, Form form) {
        translationManager.doAction(stateManager.getAppId(), action, form);
    }

}
