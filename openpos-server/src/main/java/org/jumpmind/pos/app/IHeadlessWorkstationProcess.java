package org.jumpmind.pos.app;

import org.jumpmind.pos.core.screen.translate.ITranslationManager;

public interface IHeadlessWorkstationProcess {

    public static String getDefaultServiceName(String storeId, String workstationId) {
        return String.format("%s/%s-%s", ITranslationManager.class.getSimpleName(), storeId, workstationId);
    }
}
