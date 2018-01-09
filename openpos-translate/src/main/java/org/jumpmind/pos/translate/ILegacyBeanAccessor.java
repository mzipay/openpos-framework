package org.jumpmind.pos.translate;

public interface ILegacyBeanAccessor {
    public ILegacyPOSBeanService getLegacyPOSBeanService();
    public void setLegacyPOSBeanService(ILegacyPOSBeanService beanService);
    
    public ILegacyStoreProperties getLegacyStoreProperties();
    public void setLegacyStoreProperties(ILegacyStoreProperties legacyStoreProperties);
}
