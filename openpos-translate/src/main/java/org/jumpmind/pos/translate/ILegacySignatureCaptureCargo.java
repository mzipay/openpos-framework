package org.jumpmind.pos.translate;

public interface ILegacySignatureCaptureCargo extends ILegacyCargo {
    /**
     * If the cargo is of type <code>SignatureCaptureCargo</code>, this method will set the given signature onto the cargo.
     * @param signatureData The encoded signature data.
     */
    public void setSignature(String signatureData);

}
