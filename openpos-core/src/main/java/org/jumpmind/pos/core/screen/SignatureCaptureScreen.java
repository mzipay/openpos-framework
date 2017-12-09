package org.jumpmind.pos.core.screen;

public class SignatureCaptureScreen extends DefaultScreen {
    private static final long serialVersionUID = 1L;

    private String text;
    private String textIcon;
    private String signatureData;
    private String signatureMediaType;
    

    public SignatureCaptureScreen() {
        setType(ScreenType.SignatureCapture);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSignatureData() {
        return signatureData;
    }

    public void setSignatureData(String signatureData) {
        this.signatureData = signatureData;
    }
    
    public String getTextIcon() {
        return textIcon;
    }

    public void setTextIcon(String textIcon) {
        this.textIcon = textIcon;
    }

    public String getSignatureMediaType() {
        return signatureMediaType;
    }

    public void setSignatureMediaType(String signatureMediaType) {
        this.signatureMediaType = signatureMediaType;
    }
    
}
