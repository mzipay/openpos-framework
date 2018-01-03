package org.jumpmind.pos.translate;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.HashMap;
import java.util.Map;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.model.POSSessionInfo;
import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.core.screen.DefaultScreen.ScanType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class AbstractScreenTranslator<T extends DefaultScreen> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected ILegacyScreen legacyScreen;

    protected T screen;

    protected POSSessionInfo posSessionInfo;

    protected IScreenThemeSelector screenThemeSelector;

    protected Map<String, String> iconRegistry = new HashMap<>();

    public AbstractScreenTranslator(ILegacyScreen headlessScreen, Class<T> screenClass) {
        this.legacyScreen = headlessScreen;
        try {
            screen = screenClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setIconRegistry(Map<String, String> iconRegistry) {
        this.iconRegistry = iconRegistry;
    }

    public void setScreenThemeSelector(IScreenThemeSelector screenThemeSelector) {
        this.screenThemeSelector = screenThemeSelector;
    }

    protected void chooseScreenTheme() {
        if (this.screenThemeSelector != null) {
            boolean trainingOn = posSessionInfo.getTrainingMode();
            getScreen().setTheme(this.screenThemeSelector.getScreenThemeName(trainingOn));
        }
    }

    protected void chooseScreenName() {
        if (getScreen().getName() == null) {
            getScreen().setName(legacyScreen.getSpecName());
        }
    }

    public T build() {
        if (isBlank(screen.getIcon())) {
            screen.setIcon(iconRegistry.get(screen.getName()));
        }
        chooseScreenName();
        updatePosSessionInfo();
        chooseScreenTheme();
        buildMainContent();
        return screen;
    }

    public ILegacyScreen getLegacyScreen() {
        return this.legacyScreen;
    }

    abstract protected void buildMainContent();

    abstract protected void updatePosSessionInfo();

    protected T getScreen() {
        return screen;
    }

    public void setPosSessionInfo(POSSessionInfo posSessionInfo) {
        this.posSessionInfo = posSessionInfo;
    }

    protected void resetScreen() {
        this.getScreen().clearAdditionalProperties();
    }

    public void handleAction(ITranslationManagerSubscriber subscriber, TranslationManagerServer tmServer, Action action,
            DefaultScreen screen) {
        tmServer.sendAction(action.getName());
    }

    protected void enableScan() {
        screen.setShowScan(true);
        screen.setScanType(ScanType.CAMERA_CORDOVA);
        screen.setScanActionName("Scan");
    }

}
