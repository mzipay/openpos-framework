package org.jumpmind.pos.translate;

public interface ILegacyRegisterStatusService {
    enum Status {
        OPEN,
        CLOSED,
        UNKNOWN
    }
    
    public boolean isStatusDeterminable();
    public Status getRegisterStatus();
    public Status getStoreStatus();
}
