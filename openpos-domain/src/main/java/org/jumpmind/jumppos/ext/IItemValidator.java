package org.jumpmind.jumppos.ext;

import org.jumpmind.jumppos.domain.item.PosIdentity;

public interface IItemValidator extends IExtensionPoint {

    public boolean validateItem(PosIdentity sale);
    
}
