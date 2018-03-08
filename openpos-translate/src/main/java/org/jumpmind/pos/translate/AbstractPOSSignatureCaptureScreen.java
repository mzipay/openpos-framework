package org.jumpmind.pos.translate;

import java.util.Optional;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.model.Form;
import org.jumpmind.pos.core.model.Signature;
import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.screen.SignatureCaptureScreen;
import org.jumpmind.pos.translate.ILegacyRegisterStatusService.Status;

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
            Form form);
    
    abstract protected void handleSignatureSaveAction(Signature signatureData, ITranslationManagerSubscriber subscriber, TranslationManagerServer tmServer, Action action,
            Form form);
    
    @Override
    public void handleAction(ITranslationManagerSubscriber subscriber, TranslationManagerServer tmServer, Action action,
            Form formResults) {
        if ("SaveSignature".equals(action.getName())) {
            Signature signatureData = null;
            if (action.getData() != null) {
                signatureData = this.getScreen().convertActionData(action.getData(), Signature.class);
                logger.debug("Returned signature data = {}", signatureData);
            }
            
            this.handleSignatureSaveAction(signatureData, subscriber, tmServer, action, formResults);
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
            this.handleSignatureCancelAction(subscriber, tmServer, action, formResults);
            // super.handleAction(subscriber, tmServer, action, screen);
        } else {
            super.handleAction(subscriber, tmServer, action, formResults);
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
   
    @Override
    protected void updatePosSessionInfo() {
        if (legacyScreen != null) {
            ILegacyRegisterStatusService registerStatusService = this.legacyPOSBeanService.getLegacyRegisterStatusService(legacyScreen);
            if (registerStatusService.isStatusDeterminable()) {
                posSessionInfo.setRegisterOpen(Optional.of(registerStatusService.getRegisterStatus() == Status.OPEN));

                Status storeStatus = registerStatusService.getStoreStatus();
                if (storeStatus != Status.UNKNOWN) {
                    posSessionInfo.setStoreOpen(Optional.of((storeStatus == Status.OPEN)));
                }
            }

            ILegacyBus bus = legacyPOSBeanService.getLegacyBus(legacyScreen);
            ILegacyCargo cargo = bus.getLegacyCargo();
            if (cargo != null) {
                posSessionInfo.setOperatorName(cargo.getOperatorFirstLastName());
            }

        }
    }
    
}
