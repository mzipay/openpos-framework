package org.jumpmind.pos.translate;

import org.jumpmind.pos.core.screen.DefaultScreen;

public interface ILegacyScreenTranslatorFactory {

    public AbstractScreenTranslator<? extends DefaultScreen> createScreenTranslator(ILegacyScreen orposScreen);
    
}
