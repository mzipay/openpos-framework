package org.jumpmind.pos.translate;

import java.util.Properties;

import org.jumpmind.pos.core.screen.SellScreen;

public interface ILegacyScreenTranslatorFactory {

    public AbstractScreenTranslator<? extends SellScreen> createScreenTranslator(ILegacyScreen orposScreen, String appId, Properties properties);
    
}
