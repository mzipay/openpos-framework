package org.jumpmind.pos.translate;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.model.Signature;
import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.screen.SignatureCaptureScreen;

public abstract class AbstractPOSSignatureCaptureScreen extends AbstractScreenTranslator<SignatureCaptureScreen> implements ILegacyBeanAccessor {
    protected ILegacyPOSBeanService legacyPOSBeanService;
    protected ILegacyStoreProperties legacyStoreProperties;

    public AbstractPOSSignatureCaptureScreen(ILegacyScreen legacyScreen, Class<SignatureCaptureScreen> screenClass) {
        super(legacyScreen, screenClass);
    }
    
    @Override
    public void buildMainContent() {

        // TODO: get strings from resources
        getScreen().setText("Please sign below:");
        //getScreen().setTextIcon("gesture");
        // Need to specify jpeg or png since other formats such as tiff are not widely supported in the various browser implementations of
        // the HTML5 canvas 
        getScreen().setSignatureMediaType("image/png");
        getScreen().addLocalMenuItem(new MenuItem("Continue", "SaveSignature", true));

        getScreen().setBackButton(new MenuItem("Back", "Cancel", true));
    }

    abstract protected void handleSignatureCancelAction(ITranslationManagerSubscriber subscriber, TranslationManagerServer tmServer, Action action,
            DefaultScreen screen);
    abstract protected void handleSignatureSaveAction(Signature signatureData, ITranslationManagerSubscriber subscriber, TranslationManagerServer tmServer, Action action,
            DefaultScreen screen);
    
    @Override
    public void handleAction(ITranslationManagerSubscriber subscriber, TranslationManagerServer tmServer, Action action,
            DefaultScreen screen) {
        if ("SaveSignature".equals(action.getName())) {
            Signature signatureData = null;
            if (action.getData() != null) {
                signatureData = this.getScreen().convertActionData(action.getData(), Signature.class);
                logger.debug("Returned signature data = {}", signatureData);
            }
            
            this.handleSignatureSaveAction(signatureData, subscriber, tmServer, action, screen);
            /*
            // Points format is available, but OrPOS now uses TIFF format
            // String signaturePoints = TranslationUtils.toPointsString(signatureData);
            String encodedImage = null;
            try {
                // Need to convert to points
                encodedImage = this.toPlatformSignatureFormat(signatureData); //TranslationUtils.encodedPngToEncodedTiff(signatureData);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to convert signature image",ex);
            }
*/            
/*            
            ILegacyCargo cargo = this.getCargo();
            if (cargo != null) {
                cargo.setSignature(encodedImage);
            }
*/
//            tmServer.executeMacro(new InteractionMacro().sendLetter("Continue").waitForScreen("VerifySignature").sendLetter("Yes"));
        } else if ("Cancel".equals(action.getName())) {
            this.handleSignatureCancelAction(subscriber, tmServer, action, screen);
            // super.handleAction(subscriber, tmServer, action, screen);
        } else {
            super.handleAction(subscriber, tmServer, action, screen);
        }

    }
    
    @Override
    public ILegacyPOSBeanService getLegacyPOSBeanService() {
        return this.legacyPOSBeanService;
    }

    @Override
    public void setLegacyPOSBeanService(ILegacyPOSBeanService beanService) {
        this.legacyPOSBeanService = beanService;
    }

    @Override
    public ILegacyStoreProperties getLegacyStoreProperties() {
        return this.legacyStoreProperties;
    }

    @Override
    public void setLegacyStoreProperties(ILegacyStoreProperties legacyStoreProperties) {
        this.legacyStoreProperties = legacyStoreProperties;
    }
   
}
