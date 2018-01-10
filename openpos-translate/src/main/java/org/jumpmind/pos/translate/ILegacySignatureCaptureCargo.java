package org.jumpmind.pos.translate;

import java.io.Serializable;

public interface ILegacySignatureCaptureCargo extends ILegacyCargo {
    /**
     * If the cargo is of type <code>SignatureCaptureCargo</code>, this method will set the given signature data onto the cargo.
     * @param signatureData The signature data.
     */
    public void setSignature(Serializable signatureData);

}
