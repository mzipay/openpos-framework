package org.jumpmind.pos.translate.state;

import javax.annotation.PostConstruct;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.ActionHandler;
import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.screen.DefaultScreen;
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

    @PostConstruct
    public void init() {
        if (translationManager == null) {
            throw new IllegalStateException("When using a translation state, we expect an implementation of "
                    + ITranslationManager.class.getSimpleName() + " to be bound at the prototype scope");
        }
    }
    
    @Override
    public void arrive() {
        subscribe();
        translationManager.showActiveScreen();                
    } 
    
    private void subscribe() {
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
            };
            translationManager.setTranslationManagerSubscriber(subscriber);
            stateManager.setNodeScope("subscriber", subscriber);
        }
    }

    @ActionHandler
    public void onAnyAction(Action action, DefaultScreen screen) {
        translationManager.doAction(stateManager.getAppId(), action, screen);
    }

}
