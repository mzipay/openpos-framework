package org.jumpmind.pos.translate;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang.WordUtils;
import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.core.screen.IUIAction;
import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.screen.Workstation;
import org.jumpmind.pos.translate.AbstractScreenTranslator;
import org.jumpmind.pos.translate.ILegacyAssignmentSpec;
import org.jumpmind.pos.translate.ILegacyBeanSpec;
import org.jumpmind.pos.translate.ILegacyBus;
import org.jumpmind.pos.translate.ILegacyButtonSpec;
import org.jumpmind.pos.translate.ILegacyNavigationButtonBeanModel;
import org.jumpmind.pos.translate.ILegacyPOSBeanService;
import org.jumpmind.pos.translate.ILegacyPromptAndResponseModel;
import org.jumpmind.pos.translate.ILegacyRegisterStatusService;
import org.jumpmind.pos.translate.ILegacyRegisterStatusService.Status;

import org.jumpmind.pos.translate.ILegacyScreen;
import org.jumpmind.pos.translate.ILegacyStatusBeanModel;
import org.jumpmind.pos.translate.ILegacyStoreProperties;
import org.jumpmind.pos.translate.ILegacyUIModel;
import org.jumpmind.pos.translate.ILegacyUtilityManager;


public abstract class AbstractLegacyScreenTranslator <T extends DefaultScreen> extends AbstractScreenTranslator<T>{
    
    // TODO: Move elsewhere?
    public final static String LOCAL_NAV_PANEL_KEY = "LocalNavigationPanel";
    public final static String GLOBAL_NAV_PANEL_KEY = "GlobalNavigationPanel";
    public final static String PROMPT_RESPONSE_PANEL_KEY = "PromptAndResponsePanel";
    public final static String WORK_PANEL_KEY = "WorkPanel";
    public final static String STATUS_PANEL_KEY = "StatusPanel";

    private ILegacyUtilityManager legacyUtilityManager;

    // TODO: instantiate via configuration
    private ILegacyPOSBeanService legacyPOSBeanService;
    private ILegacyStoreProperties legacyStoreProperties;
    
    public AbstractLegacyScreenTranslator(ILegacyScreen headlessScreen, Class<T> screenClass) {
        super(headlessScreen, screenClass);
    }

    public void setLegacyPOSBeanService(ILegacyPOSBeanService beanService) {
        this.legacyPOSBeanService = beanService;
    }
    
    public ILegacyPOSBeanService getLegacyPOSBeanService() {
        return this.legacyPOSBeanService;
    }
    
    public ILegacyStoreProperties getLegacyStoreProperties() {
        return legacyStoreProperties;
    }

    public void setLegacyStoreProperties(ILegacyStoreProperties legacyStoreProperties) {
        this.legacyStoreProperties = legacyStoreProperties;
    }

    @Override
    protected void buildMainContent() {
        buildBackButton();
        logAvailableLocalMenuItems();
        buildStatusItems();
        
        ILegacyStoreProperties storeProperties = this.getLegacyStoreProperties();
        Workstation workstation = new Workstation();
        workstation.setStoreId(storeProperties.getStoreNumber());
        workstation.setWorkstationId(storeProperties.getWorkstationNumber());
        screen.setWorkstation(workstation);
    }

    protected void buildStatusItems() {
        screen.setOperatorName(WordUtils.capitalizeFully(posSessionInfo.getOperatorName()));
        screen.setRegisterStatus(posSessionInfo.isRegisterOpen()
                .map(registerOpen -> new MenuItem((registerOpen ? DefaultScreen.TITLE_OPEN_STATUS : DefaultScreen.TITLE_CLOSED_STATUS), "", true)).orElse(null));
        screen.setStoreStatus(posSessionInfo.isStoreOpen()
                .map(storeOpen -> new MenuItem((storeOpen ? DefaultScreen.TITLE_OPEN_STATUS : DefaultScreen.TITLE_CLOSED_STATUS), "", true)).orElse(null));
    }
    
    protected void logAvailableLocalMenuItems() {
        ILegacyAssignmentSpec assignmentPanelSpec = getLegacyAssignmentSpec(LOCAL_NAV_PANEL_KEY);
        if (assignmentPanelSpec != null) {
            ILegacyBeanSpec localNavSpec = this.legacyPOSBeanService.getLegacyBeanSpec(this.legacyScreen, assignmentPanelSpec.getBeanSpecName());
            Map<String, Boolean> enabledState = parseButtonStates(assignmentPanelSpec);

            Arrays.stream(localNavSpec.getButtons())
                    .filter(buttonSpec -> Optional.ofNullable(enabledState.get(buttonSpec.getActionName())).orElse(buttonSpec.getEnabled()))
                    .forEachOrdered(enabledButtonSpec -> {
                        logger.info("Available local menu action: {}", enabledButtonSpec.getActionName());
                    });
            
        }
    }
    
    protected ILegacyUIModel getLegacyUIModel() {
        ILegacyAssignmentSpec assignmentPanelSpec = getLegacyAssignmentSpec(WORK_PANEL_KEY);
        ILegacyUIModel legacyUIModel = null;
        if (assignmentPanelSpec != null) {
            logger.trace("The work panel bean spec name was {}", assignmentPanelSpec.getBeanSpecName());

            legacyUIModel = this.legacyPOSBeanService.getLegacyUIModel(legacyScreen);
            
        }

        return legacyUIModel;
    }

    protected ILegacyBus getBus() {
        return this.legacyPOSBeanService.getLegacyBus(legacyScreen);
    }

    protected void buildBackButton() {
        ILegacyAssignmentSpec assignmentPanelSpec = getLegacyAssignmentSpec(GLOBAL_NAV_PANEL_KEY);
        if (null != assignmentPanelSpec) {
            ILegacyBeanSpec globalNavSpec = this.legacyPOSBeanService.getLegacyBeanSpec(this.legacyScreen, assignmentPanelSpec.getBeanSpecName());
            Map<String, Boolean> enabledState = parseButtonStates(assignmentPanelSpec);

            Arrays.stream(globalNavSpec.getButtons())
                    .filter(buttonSpec -> Optional.ofNullable(enabledState.get(buttonSpec.getActionName())).orElse(buttonSpec.getEnabled()))
                    .forEachOrdered(enabledButtonSpec -> {
                        if ("Undo".equals(enabledButtonSpec.getLabelTag())) {
                            screen.setBackButton(new MenuItem("Back", enabledButtonSpec.getActionName(), true));
                        }
                    });
            
        }
    }

    protected String getPanelPropertyValue(String panelName, String propertyName) {
        String propertyValue = propertyName; // default to propertyName
        ILegacyAssignmentSpec assignmentSpec = this.getLegacyAssignmentSpec(panelName);
        if (assignmentSpec != null) {
            propertyValue = this.getLegacyUtilityManager().retrieveText(assignmentSpec.getBeanSpecName(), getResourceBundleFilename(), propertyName, propertyName);
        }
        return propertyValue;
        
    }
    
    protected String getWorkPanelPropertyValue(String propertyName) {
        String propertyValue = propertyName; // default to propertyName
        ILegacyAssignmentSpec assignmentSpec = this.getLegacyAssignmentSpec();
        if (assignmentSpec != null) {
            propertyValue = this.getLegacyUtilityManager().retrieveText(assignmentSpec.getBeanSpecName(), getResourceBundleFilename(), propertyName, propertyName);
        }
        return propertyValue;
    }
    
    protected String getSpecPropertyValue(ILegacyAssignmentSpec spec, String key, String modelValue) {
        if (isBlank(modelValue)) {
            String propValue = spec.getPropertyValue(key);
            if (propValue != null) {
                return propValue;
            } else {
                return null;
            }
        } else {
            return modelValue;
        }
    }
    
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
            
        }
    }
    
    
    public ILegacyUtilityManager getLegacyUtilityManager() {
        if (legacyUtilityManager == null) {
            legacyUtilityManager = this.legacyPOSBeanService.getLegacyUtilityManager(legacyScreen);
        }
        
        return legacyUtilityManager;
    }

    protected String getResourceBundleFilename() {
        return this.legacyScreen.getResourceBundleFilename();
    }

    protected List<ILegacyButtonSpec> getPanelButtons(String panelKey, Optional<Boolean> enabledButtonsOnlyOpt) {
        //Optional<AssignmentSpec> assignmentPanelSpec = getLegacyAssignmentSpec(panelKey);
        ILegacyAssignmentSpec assignmentPanelSpec = getLegacyAssignmentSpec(panelKey);
        List<ILegacyButtonSpec> buttons = new ArrayList<>();

        if (assignmentPanelSpec != null) {
            ILegacyBeanSpec localNavSpec = this.legacyPOSBeanService.getLegacyBeanSpec(legacyScreen, assignmentPanelSpec.getBeanSpecName());
            Map<String, Boolean> buttonStateMap = parseButtonStates(assignmentPanelSpec);

            Arrays.stream(localNavSpec.getButtons()).filter(buttonSpec -> {
                if (enabledButtonsOnlyOpt.orElse(false)) {
                    // If only enabled buttons are wanted, use the button spec
                    // enabled/disabled status to
                    // only keep the enabled buttons
                    Boolean buttonState = buttonStateMap.get(buttonSpec.getActionName());
                    return buttonState != null ? buttonState : buttonSpec.getEnabled();
                } else { // Else don't filter any of the buttons
                    return true;
                }
            }).forEachOrdered(buttonSpec -> {
                buttons.add(buttonSpec);
                logger.info("Available local menu action: {}", buttonSpec.getActionName());
            });
            
        }
        return buttons;
    }

    protected ILegacyAssignmentSpec getLegacyAssignmentSpec() {
        return getLegacyAssignmentSpec(WORK_PANEL_KEY);
        
    }

    public ILegacyPromptAndResponseModel getPromptAndResponseModel() {
        return this.legacyPOSBeanService.getLegacyPromptAndResponseModel(legacyScreen);
    }

    public Optional<String> getPromptText(ILegacyUIModel uiModel, ILegacyAssignmentSpec promptResponsePanel, String resourceBundleFilename) {
        Optional<String> optPromptText = Optional.empty();
        try {
            ILegacyPromptAndResponseModel promptAndResponseModel = this.legacyPOSBeanService.getLegacyPromptAndResponseModel(legacyScreen);
            
            String promptTextTag = promptResponsePanel.getPropertyValue("promptTextTag");
            Properties resourceBundle = this.legacyPOSBeanService.getLegacyResourceBundleUtil().getText(resourceBundleFilename, Locale.getDefault());
            String promptTextKey = String.format("%s.%s", "PromptAndResponsePanelSpec", promptTextTag);
            String formattedPromptText = null;
            formattedPromptText = toFormattedString(resourceBundle, 
                promptTextKey, 
                promptAndResponseModel != null ? Optional.ofNullable(promptAndResponseModel.getArguments()) : Optional.empty()
            );
            optPromptText = Optional.ofNullable(formattedPromptText);
        } catch (Exception ex) {
            logger.error("Failed to get promptText for {}", uiModel.getModel().getClass());
        }
        return optPromptText;
    }
    
    protected String retrieveCommonText(String propName, Optional<String> defaultValue) {
        String commonText = defaultValue.isPresent() ? 
                this.legacyPOSBeanService.getLegacyUIUtilities().retrieveCommonText(propName, defaultValue.get()) : 
                this.legacyPOSBeanService.getLegacyUIUtilities().retrieveCommonText(propName);
        return commonText;
    }
    
    protected String toFormattedString(Properties resourceBundle, String propertyName, Optional<String[]> args) {
        String text = (String) resourceBundle.get(propertyName);
        if (args.isPresent() && isNotBlank(text)) {
            text = this.toFormattedString(text, args.get());
        }
        return text;
    }

    protected String toFormattedString(String stringToFormat, String[] args) {
        String formattedText = this.legacyPOSBeanService.getLegacyLocaleUtilities().formatComplexMessage(stringToFormat, args);
        return formattedText;
    }
    
    protected String toFormattedString(Properties resourceBundle, String propertyName, String[] args) {
        return toFormattedString(resourceBundle, propertyName, Optional.ofNullable(args));
    }
    
    protected ILegacyAssignmentSpec getLegacyAssignmentSpec(String panelKey) {
        ILegacyAssignmentSpec spec = this.legacyPOSBeanService.getLegacyAssignmentSpec(this.legacyScreen, panelKey);
        return spec;
    }

    @Override
    protected void chooseScreenName() {
        String screenName = null;
        ILegacyStatusBeanModel statusModel = this.getLegacyPOSBeanService().getLegacyStatusBeanModel(legacyScreen);
        if (statusModel != null && statusModel.getScreenName() != null) {
            screenName = statusModel.getScreenName();
        } else {
            String labelTag = getSpecPropertyValue(getLegacyAssignmentSpec(STATUS_PANEL_KEY), "screenNameTag", null);
            if (labelTag != null) {
                screenName = this.getLegacyUtilityManager().retrieveText("StatusPanelSpec", getResourceBundleFilename(), labelTag, labelTag);
            }
        }
        
        if (getScreen().getName() == null) {
            getScreen().setName(screenName);
        }
    }

    protected Map<String, Boolean> parseButtonStates(ILegacyAssignmentSpec spec) {
        Map<String, Boolean> states = new HashMap<>();
        String propValue = spec.getPropertyValue("buttonStates");
        if (propValue != null) {
            String stateString = propValue;
            String[] tokens = stateString.split(",");
            for (String token : tokens) {
                token = token.trim();
                String key = token.substring(0, token.indexOf("["));
                String value = token.substring(token.indexOf("[") + 1, token.indexOf("]"));
                states.put(key, new Boolean(value));
            }
        }
        return states;
    }

    protected  <A extends IUIAction> List<A> generateUIActionsForLocalNavButtons(Class<A> actionClass) {
        return generateUIActionsForLocalNavButtons(actionClass, true);
    }
    
    protected  <A extends IUIAction> List<A> generateUIActionsForLocalNavButtons(Class<A> actionClass, boolean filterDisabledButtons) {
        List<ILegacyButtonSpec> allLocalNavButtons = this.getPanelButtons(LOCAL_NAV_PANEL_KEY, Optional.of(filterDisabledButtons));
        ILegacyAssignmentSpec localNavPanel = this.getLegacyAssignmentSpec(LOCAL_NAV_PANEL_KEY);

        // Some Buttons/Options might be disabled dynamically.
        //POSBaseBeanModel posModel = (POSBaseBeanModel) this.legacyScreen.getModel();
        List<ILegacyButtonSpec> modifiedButtons = new ArrayList<>();
        ILegacyNavigationButtonBeanModel navigationButtonBeanModel = this.legacyPOSBeanService.getLegacyPOSBaseBeanModel(legacyScreen).getLegacyLocalButtonBeanModel();
        if (navigationButtonBeanModel != null) {
            if (navigationButtonBeanModel.getModifyButtons() != null) {
                modifiedButtons.addAll(Arrays.asList(navigationButtonBeanModel.getModifyButtons()));
            }
        }
        final List<A> generatedActions = new ArrayList<A>();
        allLocalNavButtons.stream()
            .forEachOrdered(localNavButton -> {
                ILegacyButtonSpec possibleModifiedSpec = modifiedButtons.stream().filter(
                    modifiedButton -> modifiedButton.getActionName().equals(localNavButton.getActionName())
                ).findFirst().orElse(localNavButton);
                
                // If the labelTag is null, don't add the button. (That seems to be the way some sites don't show buttons)
                String labelTag = localNavButton.getLabelTag();
                if (possibleModifiedSpec.getLabelTag() != null) {
                    labelTag = possibleModifiedSpec.getLabelTag();
                }
                Boolean enabledFlag = localNavButton.getEnabledFlag();
                if (possibleModifiedSpec.getEnabledFlag() != null) {
                    enabledFlag = possibleModifiedSpec.getEnabledFlag();
                }
                // Skip adding the button if the modified spec doesn't have a value for enabled and the label tag. (this seems
                // to be the way OrPOS sites omit buttons)
                if (possibleModifiedSpec.getEnabledFlag() != null || possibleModifiedSpec.getLabelTag() != null) {
                    String buttonText = this.getLegacyUtilityManager().retrieveText(localNavPanel.getBeanSpecName(), getResourceBundleFilename(), labelTag, labelTag);
                    A actionItem;
                    try {
                        actionItem = actionClass.newInstance();
                        actionItem.setTitle(buttonText);
                        actionItem.setEnabled(enabledFlag);
                        actionItem.setAction(possibleModifiedSpec.getActionName());
                        generatedActions.add(actionItem);
                    } catch (InstantiationException | IllegalAccessException e) {
                        logger.error( String.format("Failed to create action of type %s for action '%s'", actionClass.getName(), possibleModifiedSpec.getActionName()), e);
                    }
                }
            }
        );

        return generatedActions;
        
    }
   
}
