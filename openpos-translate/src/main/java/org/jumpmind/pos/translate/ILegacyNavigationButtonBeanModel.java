package org.jumpmind.pos.translate;

public interface ILegacyNavigationButtonBeanModel {
    ILegacyButtonSpec[] getModifyButtons();
    ILegacyButtonSpec[] getNewButtons();
    Object getModel();
}
