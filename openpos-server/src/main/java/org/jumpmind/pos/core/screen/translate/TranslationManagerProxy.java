package org.jumpmind.pos.core.screen.translate;

import static java.lang.String.format;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.core.service.IHeadlessStartupService;
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
public class TranslationManagerProxy implements ITranslationManager {
    static private final Logger logger = LoggerFactory.getLogger(TranslationManagerProxy.class);

    @Autowired
    IHeadlessStartupService headlessStartupService;
    
    @Autowired
    RMICallbackProxyManager rmiCallbackProxyManager;

    @Value("${orpos.rmi.registry.port}")
    int rmiRegistryPort;

    @Value("${external.process.enabled}")
    boolean externalProcessEnabled;
    
    ITranslationManagerSubscriber subscriber;

    String nodeId;

    @Override
    public void setTranslationManagerSubscriber(ITranslationManagerSubscriber subscriber) {
        this.subscriber = subscriber;
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
    
    @Override
    public String getActiveScreenID() {
        String activeScreenId = null;
        try {
            ITranslationManager implementation = headlessStartupService.getTranslationManagerRef(nodeId);
            activeScreenId = implementation.getActiveScreenID();
        } catch (RemoteConnectFailureException e) {
            headlessStartupService.start(nodeId);
            setTranslationManagerSubscriber();
            ITranslationManager implementation = headlessStartupService.getTranslationManagerRef(nodeId);
            activeScreenId = implementation.getActiveScreenID();
        }
        
        return activeScreenId;
    }
    
    @Override
    public void ping() {
    }

    private void exportTranslationManagerSubscriber() {
        if ( this.externalProcessEnabled ) {
        	logger.debug( "externalProcess mode detected, creating Remotable version of given TranslationManagerSubscriber {}", this.subscriber );
            // Wrap the client-side subscriber in a dynamically created Remote wrapper so that we can make callbacks to it
        	// from the remote process
            this.subscriber = rmiCallbackProxyManager.registerAndExport(this.subscriber, ITranslationManagerSubscriber.class);
        } else {
            logger.debug( "externalProcess mode not detected, bypassing creation of RMI proxy for {}", this.subscriber );
        }
    }

    private void setTranslationManagerSubscriber() {
        ITranslationManager implementation = headlessStartupService.getTranslationManagerRef(nodeId);
        if (implementation != null) {
            implementation.setTranslationManagerSubscriber(this.subscriber);
            
            logger.debug( "TranslationManagerSubscriber set to {} on TranslationManager: {}", this.subscriber, implementation );
        } else {
            throw new IllegalStateException(format("Failed to find a translator for %s", nodeId));
        }
    }


}
