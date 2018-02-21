package org.jumpmind.pos.translate;

import java.util.Properties;

import org.jumpmind.pos.core.screen.DefaultScreen;

public interface ILegacyScreenTranslatorFactory {

    public AbstractScreenTranslator<? extends DefaultScreen> createScreenTranslator(ILegacyScreen orposScreen, String appId, Properties properties);
    
}
