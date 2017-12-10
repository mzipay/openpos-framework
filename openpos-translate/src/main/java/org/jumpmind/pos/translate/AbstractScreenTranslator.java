package org.jumpmind.pos.translate;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.model.POSSessionInfo;
import org.jumpmind.pos.core.screen.DefaultScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class AbstractScreenTranslator<T extends DefaultScreen> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected ILegacyScreen headlessScreen;

    protected T screen;

    protected POSSessionInfo posSessionInfo;

    public AbstractScreenTranslator(ILegacyScreen headlessScreen, Class<T> screenClass) {
        this.headlessScreen = headlessScreen;
        try {
            screen = screenClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        screen.setName(headlessScreen.getSpecName());
    }

    public T build() {
        updatePosSessionInfo();
        buildMainContent();
        return screen;
    }

    abstract protected void buildMainContent();

    abstract protected void updatePosSessionInfo();

    protected T getScreen() {
        return screen;
    }

    public POSSessionInfo getPosSessionInfo() {
        return posSessionInfo;
    }

    public void setPosSessionInfo(POSSessionInfo posSessionInfo) {
        this.posSessionInfo = posSessionInfo;
    }

    public ILegacyScreen getHeadlessScreen() {
        return headlessScreen;
    }

    protected void resetScreen() {
        this.getScreen().clearAdditionalProperties();
        this.getScreen().clearMenuItems();
    }

    public void handleAction(ITranslationManagerSubscriber subscriber, TranslationManagerServer tmServer, Action action,
            DefaultScreen screen) {
        tmServer.sendAction(action.getName());
    }

}
