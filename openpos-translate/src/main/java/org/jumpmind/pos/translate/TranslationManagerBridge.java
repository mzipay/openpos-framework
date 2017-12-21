package org.jumpmind.pos.translate;

import static java.lang.String.format;

import org.jumpmind.pos.core.device.IDeviceMessageDispatcher;
import org.jumpmind.pos.core.device.IDeviceMessageSubscriber;
import org.jumpmind.pos.core.device.IDeviceRequest;
import org.jumpmind.pos.core.device.IDeviceResponse;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.util.RMICallbackProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class TranslationManagerBridge implements ITranslationManager, IDeviceMessageDispatcher {
    static private final Logger logger = LoggerFactory.getLogger(TranslationManagerBridge.class);

    @Autowired
    ILegacyStartupService headlessStartupService;
    
    @Autowired
    RMICallbackProxyManager rmiCallbackProxyManager;

    @Value("${rmi.registry.port}")
    int rmiRegistryPort;

    @Value("${external.process.enabled}")
    boolean externalProcessEnabled;
    
    ITranslationManagerSubscriber translationManagerSubscriber;
    
    IDeviceMessageSubscriber deviceMessageSubscriber;

    String nodeId;

    @Override
    public void setTranslationManagerSubscriber(ITranslationManagerSubscriber subscriber) {
        this.translationManagerSubscriber = subscriber;
        this.nodeId = subscriber.getNodeId();
        ITranslationManager implementation = headlessStartupService.getTranslationManagerRef(nodeId);
        logger.debug( "ITranslationManager implementation for nodeId {} is: {}", nodeId, implementation );
        if (implementation == null) {
            headlessStartupService.start(subscriber.getNodeId());
        }

        // Only create a Remote-able proxy TranslactionManagerSubscriber if we are running
        // in external mode, otherwise use reference to local TranslactionManagerSubscriber
        exportTranslationManagerSubscriber();

        setTranslationManagerSubscriber();
    }

    @Override
    public void setDeviceMessageSubscriber(IDeviceMessageSubscriber subscriber) {
        this.deviceMessageSubscriber = subscriber;
    }
    

    @Override
    public void doAction(String appId, Action action, DefaultScreen screen) {
        try {
            ITranslationManager implementation = headlessStartupService.getTranslationManagerRef(nodeId);
            implementation.doAction(appId, action, screen);
        } catch (RemoteConnectFailureException e) {
            headlessStartupService.start(nodeId);
            setTranslationManagerSubscriber();
            ITranslationManager implementation = headlessStartupService.getTranslationManagerRef(nodeId);
            implementation.doAction(appId, action, screen);
        }
    }

    @Override
    public void showActiveScreen() {
        try {
            ITranslationManager implementation = headlessStartupService.getTranslationManagerRef(nodeId);
            implementation.showActiveScreen();
        } catch (RemoteConnectFailureException e) {
            headlessStartupService.start(nodeId);
            setTranslationManagerSubscriber();
            ITranslationManager implementation = headlessStartupService.getTranslationManagerRef(nodeId);
            implementation.showActiveScreen();
        }
    }
    
    /*
    @Override
    public IDeviceResponse doDeviceAction(String appId, IDeviceRequest request) {
        try {
            ITranslationManager implementation = headlessStartupService.getTranslationManagerRef(nodeId);
            this.su
            return implementation.doDeviceAction(appId, request);
        } catch (RemoteConnectFailureException e) {
            headlessStartupService.start(nodeId);
            setTranslationManagerSubscriber();
            ITranslationManager implementation = headlessStartupService.getTranslationManagerRef(nodeId);
            return implementation.doDeviceAction(appId, request);
        }
    }
    */
    @Override
    public void ping() {
    }

    private void exportTranslationManagerSubscriber() {
        if ( this.externalProcessEnabled ) {
        	logger.debug( "externalProcess mode detected, creating Remotable version of given TranslationManagerSubscriber {}", this.translationManagerSubscriber );
            // Wrap the client-side subscriber in a dynamically created Remote wrapper so that we can make callbacks to it
        	// from the remote process
            this.translationManagerSubscriber = rmiCallbackProxyManager.registerAndExport(this.translationManagerSubscriber, ITranslationManagerSubscriber.class);
        } else {
            logger.debug( "externalProcess mode not detected, bypassing creation of RMI proxy for {}", this.translationManagerSubscriber );
        }
    }

    private void setTranslationManagerSubscriber() {
        ITranslationManager implementation = headlessStartupService.getTranslationManagerRef(nodeId);
        if (implementation != null) {
            implementation.setTranslationManagerSubscriber(this.translationManagerSubscriber);
            
            logger.debug( "TranslationManagerSubscriber set to {} on TranslationManager: {}", this.translationManagerSubscriber, implementation );
        } else {
            throw new IllegalStateException(format("Failed to find a translator for %s", nodeId));
        }
    }




    @Override
    public IDeviceResponse sendDeviceRequest(IDeviceRequest request) {
        // TODO Auto-generated method stub
        return null;
    }





}
