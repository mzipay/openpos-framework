package org.jumpmind.pos.core.model;

import java.io.Serializable;

public interface IMaskSpec extends Serializable {
    enum MaskSpecType {
        GenericMask,
        NumberMask
    }
    
    enum PipeName {
        ToUpper
    }
    
    MaskSpecType getType();
}
