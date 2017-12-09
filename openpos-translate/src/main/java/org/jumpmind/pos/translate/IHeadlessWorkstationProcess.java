package org.jumpmind.pos.translate;

public interface IHeadlessWorkstationProcess {

    public static String getDefaultServiceName(String storeId, String workstationId) {
        return String.format("%s/%s-%s", ITranslationManager.class.getSimpleName(), storeId, workstationId);
    }
}
